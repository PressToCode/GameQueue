package com.example.gamequeue.data.sharedViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.data.repository.DatabaseRepository;
import com.example.gamequeue.utils.ApplicationContext;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Fetch User Reservations Here
public class ReservationSharedViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ReservationModel>> reservationListLive = new MutableLiveData<>();
    private final MediatorLiveData<ArrayList<ReservationModel>> filteredReservationListLiveOne = new MediatorLiveData<>();
    private final MediatorLiveData<ArrayList<ReservationModel>> filteredReservationListLiveTwo = new MediatorLiveData<>();
    private final MutableLiveData<String> filterStatusLiveOne = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> filterWordLiveOne = new MutableLiveData<>();
    private final MutableLiveData<String> filterStatusLiveTwo = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> filterWordLiveTwo = new MutableLiveData<>();
    private final ArrayList<ReservationModel> reservationList = new ArrayList<>();
    private final DatabaseReference reservationRef = FirebaseUtil.getReservationsRef();
    private ValueEventListener reservationEventListener;

    public ReservationSharedViewModel() {
        attachDatabaseListener();

        // Mirror original list and apply filter immediately if any
        filteredReservationListLiveOne.addSource(reservationListLive, list -> {
            applyAllFilters(list, filterStatusLiveOne.getValue(), filterWordLiveOne.getValue(), filteredReservationListLiveOne);
        });

        filteredReservationListLiveOne.addSource(filterStatusLiveOne, status -> {
            applyAllFilters(reservationListLive.getValue(), status, filterWordLiveOne.getValue(), filteredReservationListLiveOne);
        });

        filteredReservationListLiveOne.addSource(filterWordLiveOne, words -> {
            applyAllFilters(reservationListLive.getValue(), filterStatusLiveOne.getValue(), words, filteredReservationListLiveOne);
        });

        // For another list
        filteredReservationListLiveTwo.addSource(reservationListLive, list -> {
            applyAllFilters(list, filterStatusLiveTwo.getValue(), filterWordLiveTwo.getValue(), filteredReservationListLiveTwo);
        });

        filteredReservationListLiveTwo.addSource(filterStatusLiveTwo, status -> {
            applyAllFilters(reservationListLive.getValue(), status, filterWordLiveTwo.getValue(), filteredReservationListLiveTwo);
        });

        filteredReservationListLiveTwo.addSource(filterWordLiveTwo, words -> {
            applyAllFilters(reservationListLive.getValue(), filterStatusLiveTwo.getValue(), words, filteredReservationListLiveTwo);
        });

        // Initialize filter values
        filterStatusLiveOne.setValue("");
        filterWordLiveOne.setValue(new ArrayList<>());
        filterStatusLiveTwo.setValue("");
        filterWordLiveTwo.setValue(new ArrayList<>());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        detachDatabaseReadListener(); // Important for cleanup
    }

    public LiveData<ArrayList<ReservationModel>> getFilteredReservationListLiveOne() { return filteredReservationListLiveOne; }

    public LiveData<ArrayList<ReservationModel>> getFilteredReservationListLiveTwo() { return filteredReservationListLiveTwo; }

    public void setFilterStatusOne(String status) {
        filterStatusLiveOne.postValue(status);
    }

    public void setFilterStatusTwo(String status) {
        filterStatusLiveTwo.postValue(status);
    }

    public void setFilterWordOne(ArrayList<String> words) {
        filterWordLiveOne.postValue(words);
    }

    public void setFilterWordTwo(ArrayList<String> words) {
        filterWordLiveTwo.postValue(words);
    }

    public void clearAllFiltersOne() {
        filterStatusLiveOne.postValue("");
        filterWordLiveOne.postValue(new ArrayList<>());
    }

    public void clearAllFiltersTwo() {
        filterStatusLiveTwo.postValue("");
        filterWordLiveTwo.postValue(new ArrayList<>());
    }

    private void attachDatabaseListener() {
        // Get Current Time and Date
        LocalDate today = LocalDate.now();

        // Get current time MINUS one hour
        LocalTime actualTime = LocalTime.now();
        LocalTime currentTime = actualTime.minusHours(1);

        if (reservationEventListener == null) {
            reservationEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    reservationList.clear();

                    // Get all data under "reservations"
                    for (DataSnapshot reservationSnapshot : dataSnapshot.getChildren()) {
                        try {
                            ReservationModel reservation = reservationSnapshot.getValue(ReservationModel.class);
                            if (reservation != null) {
                                reservation.setId(reservationSnapshot.getKey());

                                // Blacklist Status to not show
                                String[] blacklistedStatus = {"completed", "canceled", "rejected"};

                                if (Arrays.asList(blacklistedStatus).contains(reservation.getStatus().toLowerCase())) {
                                    continue;
                                }

                                // Check if reservation is already past their due
                                LocalDate reservationDate = LocalDate.parse(reservation.getDate());

                                if (reservationDate.isBefore(today)) {
                                    // Call DatabaseRepository to update status to "Completed"
                                    DatabaseRepository.updateReservationStatus(reservation);
                                    return;
                                }

                                // Check also for the time today
                                LocalTime reservationTime = LocalTime.parse(reservation.getTime());

                                // If it's today and the reservation is already expired (past 1 hour after the reserved time)
                                if (reservationDate.isEqual(today) && (reservationTime.isBefore(currentTime) && currentTime.getHour() != 23)) {
                                    // Call DatabaseRepository to update status to "Completed"
                                    DatabaseRepository.updateReservationStatus(reservation);
                                    return;
                                }

                                reservationList.add(reservation);
                            }
                        } catch (Exception e) {
                            Log.d("[ReservationSharedViewModel]", "onDataChange: " + e.getMessage());
                        }
                    }
                    // Post the updated list to LiveData.
                    // If you're on a background thread (which ValueEventListener callbacks can be),
                    // use postValue. If you're sure you're on the main thread, use setValue.
                    // For Firebase listeners, it's safer to use postValue as they might not always
                    // guarantee main thread execution for all scenarios, though typically they do.
                    reservationListLive.postValue(new ArrayList<>(reservationList)); // Post a new copy
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("[ReservationSharedViewModel]", "onCancelled: " + databaseError.getMessage());
                }
            };

            // Add listener
            reservationRef.child(FirebaseUtil.getAuth().getCurrentUser().getUid()).addValueEventListener(reservationEventListener);
        }
    }

    public void detachDatabaseReadListener() {
        if (reservationEventListener != null) {
            reservationRef.removeEventListener(reservationEventListener);
            reservationEventListener = null;
        }
    }

    // Status Filter - "Pending", "Completed", "Rejected"
    private void applyAllFilters(ArrayList<ReservationModel> currentReservations, String status, ArrayList<String> words, MediatorLiveData<ArrayList<ReservationModel>> filteredReservationsLive) {
        if (currentReservations == null) {
            filteredReservationsLive.postValue(new ArrayList<>()); // Post empty if source is null
            return;
        }

        // Make copies of filter strings to avoid null issues, treat null as empty
        final ArrayList<String> currentTitleFilter = words == null ? new ArrayList<>() : words;
        final String currentStatusFilter = status == null ? "" : status.toLowerCase().trim();

        // If both filters are empty, return a copy of the original list
        if (currentTitleFilter.isEmpty() && currentStatusFilter.isEmpty()) {
            filteredReservationsLive.postValue(new ArrayList<>(currentReservations));
            return;
        }

        // Start with the full list and apply filters sequentially
        Stream<ReservationModel> stream = currentReservations.stream();

        if (currentTitleFilter != null && !currentTitleFilter.isEmpty()) {
            stream = stream.filter(reservation -> {
                if (reservation.getConsoleName() == null) {
                    return false;
                }

                if (reservation.getStatus() == null) {
                    return false;
                }

                if (reservation.getTime() == null) {
                    return false;
                }

                String consoleNameToLower = reservation.getConsoleName().toLowerCase();
                String statusToLower = reservation.getStatus().toLowerCase();

                for (String filterWord : currentTitleFilter) {
                    if (consoleNameToLower.contains(filterWord.toLowerCase())) {
                        return true;
                    }
                }

                for (String filterWord : currentTitleFilter) {
                    if (statusToLower.contains(filterWord.toLowerCase())) {
                        return true;
                    }
                }

                for (String filterWord : currentTitleFilter) {
                    if (reservation.getTime().contains(filterWord.toLowerCase())) {
                        return true;
                    }
                }

                return false;
            });
        }

        if (!currentStatusFilter.isEmpty()) {
            stream = stream.filter(reservation ->
                    reservation.getStatus() != null &&
                            reservation.getStatus().toLowerCase().equals(currentStatusFilter) // Exact match for status
            );
        }

        ArrayList<ReservationModel> result = stream.collect(Collectors.toCollection(ArrayList::new));
        filteredReservationsLive.postValue(result);
    }
}
