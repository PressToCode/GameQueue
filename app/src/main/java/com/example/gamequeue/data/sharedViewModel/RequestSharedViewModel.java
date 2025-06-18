package com.example.gamequeue.data.sharedViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.data.model.RequestModel;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.data.repository.DatabaseRepository;
import com.example.gamequeue.utils.CustomCallbackWithType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * We need to observe request only
 * since all changes made by user to reservation
 * will also change request
 *
 * We only filter by words
 *
 * Migration Plan:
 * 1) Get Request Data
 * 2) Get ReservationId and UserId
 * 3) Get Reservation Data
 * 4) Attach Reservation Data to Request Data using HashMap
 *
 * RequestListLive should indicate how much item in RecyclerView to Display
 * But, since the amount of request is equal to the amount of reservation data fetched
 * we can use reservation data count directly
 *
 * We then can filter it by reservation
 * (Basically we're going in circle to a makeshift ReservationSharedViewModel)
 */
public class RequestSharedViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<RequestModel>> requestListLive = new MutableLiveData<>();
    private final MediatorLiveData<ArrayList<ReservationModel>> reservationListLive = new MediatorLiveData<>();
    private final ArrayList<ReservationModel> temporaryReservationList = new ArrayList<>();
    private final MediatorLiveData<ArrayList<ReservationModel>> filteredReservationListLive = new MediatorLiveData<>();
    private final MutableLiveData<ArrayList<String>> filterWordLive = new MutableLiveData<>();
    private final DatabaseReference requestRef = FirebaseUtil.getRequestRef();
    private ValueEventListener requestEventListener;

    public RequestSharedViewModel() {
        // Add Event Listener to Request
        attachRequestListener();

        // Listen to Request Change then fetch reservationList
        reservationListLive.addSource(requestListLive, this::fetchReservations);

        // Mirror original list
        filteredReservationListLive.addSource(reservationListLive, requestModels -> {
            applyWordsFilter(requestModels, filterWordLive.getValue());
        });

        // Apply Filter
        filteredReservationListLive.addSource(filterWordLive, words -> {
            applyWordsFilter(reservationListLive.getValue(), words);
        });

        // Initialize filter values
        filterWordLive.setValue(new ArrayList<>());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        detachRequestListener();
    }

    public LiveData<ArrayList<ReservationModel>> getFilteredReservationListLive() { return filteredReservationListLive; }

    public void setFilterWord(ArrayList<String> words) { filterWordLive.postValue(words); }

    public String getUserIdByReservationId(String reservationId) {
        for (RequestModel request : requestListLive.getValue()) {
            if (request.getReservationId().equals(reservationId)) {
                return request.getUserId();
            }
        }

        return null;
    }

    private void attachRequestListener() {
        if (requestEventListener == null) {
            requestEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<RequestModel> requestList = new ArrayList<>();

                    // Get all data under "request"
                    for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                        try {
                            RequestModel request = requestSnapshot.getValue(RequestModel.class);
                            if (request != null) {
                                request.setReservationId(requestSnapshot.getKey());
                                requestList.add(request);
                            }
                        } catch (Exception e) {
                            Log.d("[RequestSharedViewModel]", "onDataChange: " + e.getMessage());
                        }
                    }

                    requestListLive.postValue(new ArrayList<>(requestList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("[RequestSharedViewModel]", "onCancelled: " + databaseError.getMessage());
                }
            };

            // Add listener
            requestRef.addValueEventListener(requestEventListener);
        }
    }

    private void detachRequestListener() {
        if (requestEventListener != null) {
            requestRef.removeEventListener(requestEventListener);
            requestEventListener = null;
        }
    }

    private void fetchReservations(ArrayList<RequestModel> requestList) {
        // Get Current Time and Date
        LocalDate today = LocalDate.now();

        // Get current time MINUS one hour
        LocalTime currentTime = LocalTime.now().minusHours(1);

        // Clear List
        temporaryReservationList.clear();
        reservationListLive.postValue(new ArrayList<>(temporaryReservationList));

        // Counter to avoid race condition
        AtomicInteger pendingFetches = new AtomicInteger(requestList.size());

        // For Each Request, Fetch Reservation
        requestList.forEach(requestModel -> {
            DatabaseRepository.getUserReservationById(requestModel.getReservationId(), requestModel.getUserId(), new CustomCallbackWithType<>() {
                @Override
                public void onSuccess(ReservationModel reservation) {
                    if (reservation != null) {
                        // Blacklist Status to not show
                        String[] blacklistedStatus = {"completed", "canceled", "rejected"};

                        // Check if reservation is already past their due
                        LocalDate reservationDate = LocalDate.parse(reservation.getDate());

                        if (reservationDate.isBefore(today)) {
                            // Call DatabaseRepository to update status to "Completed"
                            DatabaseRepository.updateReservationStatus(reservation, null);
                            return;
                        }

                        // Check also for the time today
                        LocalTime reservationTime = LocalTime.parse(reservation.getTime());

                        // If it's today and the reservation is already expired (past 1 hour after the reserved time)
                        if (reservationDate.isEqual(today) && (reservationTime.isBefore(currentTime) && currentTime.getHour() != 23)) {
                            // Call DatabaseRepository to update status to "Completed"
                            DatabaseRepository.updateReservationStatus(reservation, null);
                            return;
                        }

                        if (Arrays.asList(blacklistedStatus).contains(reservation.getStatus().toLowerCase())) {
                            return;
                        }

                        temporaryReservationList.add(reservation);
                    }

                    if (pendingFetches.decrementAndGet() <= 0) {
                        reservationListLive.postValue(new ArrayList<>(temporaryReservationList));
                    }
                }

                @Override
                public void onError(String error) {
                    Log.d("[RequestSharedViewModel]", "fetchReservations: " + error);

                    if (pendingFetches.decrementAndGet() <= 0) {
                        // All fetches completed (some might have failed)
                        reservationListLive.postValue(new ArrayList<>(temporaryReservationList));
                    }
                }
            });
        });
    }

    private void applyWordsFilter(ArrayList<ReservationModel> reservationList, ArrayList<String> words) {
        if (reservationList == null) {
            filteredReservationListLive.postValue(new ArrayList<>()); // Post empty if source is null
            return;
        }

        // Make copies of filter strings to avoid null issues, treat null as empty
        final ArrayList<String> currentWordsFilter = words == null ? new ArrayList<>() : words;

        // If both filters are empty, return a copy of the original list
        if (currentWordsFilter.isEmpty()) {
            filteredReservationListLive.postValue(new ArrayList<>(reservationList));
            return;
        }

        // Start with the full list and apply filters sequentially
        Stream<ReservationModel> stream = reservationList.stream();

        if (currentWordsFilter != null && !currentWordsFilter.isEmpty()) {
            stream = stream.filter(reservation -> {
                // Guard 1
                if (reservation.getConsoleName() == null) {
                    return false;
                }

                // Guard 2
                if (reservation.getLenderName() == null) {
                    return false;
                }

                // Guard 3
                if (reservation.getStatus() == null) {
                    return false;
                }

                String consoleNameToLower = reservation.getConsoleName().toLowerCase();
                String lenderNameToLower = reservation.getLenderName().toLowerCase();
                String statusToLower = reservation.getStatus().toLowerCase();

                // Filter for console name
                for (String filterWord : currentWordsFilter) {
                    if (consoleNameToLower.contains(filterWord.toLowerCase())) {
                        return true;
                    }
                }

                // Filter for user name
                for (String filterWord : currentWordsFilter) {
                    if (lenderNameToLower.contains(filterWord.toLowerCase())) {
                        return true;
                    }
                }

                // Filter for status name
                for (String filterWord : currentWordsFilter) {
                    if (statusToLower.contains(filterWord.toLowerCase())) {
                        return true;
                    }
                }

                return false;
            });
        }

        ArrayList<ReservationModel> result = stream.collect(Collectors.toCollection(ArrayList::new));
        filteredReservationListLive.postValue(result);
    }
}
