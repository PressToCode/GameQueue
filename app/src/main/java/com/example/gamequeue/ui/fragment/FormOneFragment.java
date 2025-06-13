package com.example.gamequeue.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.gamequeue.R;
import com.example.gamequeue.data.sharedViewModel.ReservationFormSharedViewModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class FormOneFragment extends Fragment {
    private List<LinearLayout> buttonDayCard;
    private List<RadioButton> buttonTime;
    private ReservationFormSharedViewModel sharedViewModel;
    private TextView consoleName, status, specificationOne, specificationTwo, specificationThree;
    private LocalTime cutoffTime;

    public FormOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialization
        buttonDayCard = new ArrayList<>();
        buttonDayCard.add(view.findViewById(R.id.buttonDayCard1));
        buttonDayCard.add(view.findViewById(R.id.buttonDayCard2));
        buttonDayCard.add(view.findViewById(R.id.buttonDayCard3));
        buttonDayCard.add(view.findViewById(R.id.buttonDayCard4));
        buttonDayCard.add(view.findViewById(R.id.buttonDayCard5));
        buttonTime = new ArrayList<>();
        buttonTime.add(view.findViewById(R.id.radio_time1));
        buttonTime.add(view.findViewById(R.id.radio_time2));
        buttonTime.add(view.findViewById(R.id.radio_time3));
        buttonTime.add(view.findViewById(R.id.radio_time4));
        buttonTime.add(view.findViewById(R.id.radio_time5));
        buttonTime.add(view.findViewById(R.id.radio_time6));
        buttonTime.add(view.findViewById(R.id.radio_time7));
        buttonTime.add(view.findViewById(R.id.radio_time8));
        consoleName = view.findViewById(R.id.formCardConsoleName);
        status = view.findViewById(R.id.formCardStatus);
        specificationOne = view.findViewById(R.id.formCardSpecificationOne);
        specificationTwo = view.findViewById(R.id.formCardSpecificationTwo);
        specificationThree = view.findViewById(R.id.formCardSpecificationThree);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(ReservationFormSharedViewModel.class);
        cutoffTime = LocalTime.now();

        // Set UI Visual
        fetchConsole();
        setupCards();
        setupListener();
    }

    // TODO: CHANGE IMPLEMENTATION TO FETCH THROUGH DATABASE
    private void fetchConsole() {
        // Ensure ID is passed from Adapter
        String id = getActivity().getIntent().getStringExtra("id");

        if (id == null || id.isEmpty()) {
            return;
        }

        // Get Other Metadata
        String txtTitle, txtStatus, txtSpecificationOne, txtSpecificationTwo, txtSpecificationThree;
        txtTitle = getActivity().getIntent().getStringExtra("title");
        txtStatus = getActivity().getIntent().getStringExtra("status");
        txtSpecificationOne = getActivity().getIntent().getStringExtra("specificationOne");
        txtSpecificationTwo = getActivity().getIntent().getStringExtra("specificationTwo");
        txtSpecificationThree = getActivity().getIntent().getStringExtra("specificationThree");

        if (txtTitle != null && txtStatus != null && txtSpecificationOne != null && txtSpecificationTwo != null && txtSpecificationThree != null) {
            consoleName.setText(txtTitle);
            status.setText(txtStatus);
            specificationOne.setText(txtSpecificationOne);
            specificationTwo.setText(txtSpecificationTwo);
            specificationThree.setText(txtSpecificationThree);

            // Pass it to ReservationFormModel
            sharedViewModel.getReservationForm().getValue().setConsoleName(txtTitle);
            sharedViewModel.getReservationForm().getValue().setConsoleId(id);
        }
    }

    private void setupCards() {
        LocalDate today = LocalDate.now();

        // These represent the DayOfWeek enum values for Monday to Friday
        DayOfWeek[] weekdays = { DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY };

        for (int i = 0; i < 5; i++) { // For 5 cards, representing Mon (0) to Fri (4)
            DayOfWeek cardRepresentsDay = weekdays[i]; // The day of the week this card slot represents
            LocalDate dateForThisCard;

            if (today.getDayOfWeek() == DayOfWeek.SATURDAY || today.getDayOfWeek() == DayOfWeek.SUNDAY) {
                // If today is a weekend, all cards show next week's dates
                dateForThisCard = today.with(TemporalAdjusters.next(cardRepresentsDay));
            } else {
                // Today is a weekday
                if (cardRepresentsDay.getValue() < today.getDayOfWeek().getValue()) {
                    // If the card's day (e.g., Monday) is before today's day (e.g., Wednesday)
                    // then this card shows next week's date for that day.
                    dateForThisCard = today.with(TemporalAdjusters.next(cardRepresentsDay));
                } else {
                    // If the card's day is today or a future day in the current week,
                    // show the current week's date for that day.
                    dateForThisCard = today.with(TemporalAdjusters.nextOrSame(cardRepresentsDay));
                }
            }

            TextView tvDate = buttonDayCard.get(i).findViewById(R.id.tvDate);
            tvDate.setText(String.valueOf(dateForThisCard.getDayOfMonth()));
        }

        // Set Default Selected Card
        if (today.getDayOfWeek() == DayOfWeek.SATURDAY || today.getDayOfWeek() == DayOfWeek.SUNDAY) {
            setDaySelected(0);
        } else {
            setDaySelected(today.getDayOfWeek().getValue() - 1);
        }

        ((TextView) buttonDayCard.get(1).findViewById(R.id.tvDay)).setText("Selasa");
        ((TextView) buttonDayCard.get(2).findViewById(R.id.tvDay)).setText("Rabu");
        ((TextView) buttonDayCard.get(3).findViewById(R.id.tvDay)).setText("Kamis");
        ((TextView) buttonDayCard.get(4).findViewById(R.id.tvDay)).setText("Jumat");
    }

    private void setupListener() {
        // Setup Listener for ButtonDayCard
        for (int i = 0; i < buttonDayCard.size(); i++) {
            final int index = i;
            buttonDayCard.get(i).setOnClickListener(v -> {
                setDaySelected(index);
            });
        }



        // Setup Listener for ButtonTime
        buttonTime.forEach(button -> {
            button.setOnClickListener(v -> {
                if (!button.isChecked()) {
                    return;
                }

                buttonTime.forEach(it -> {
                    it.setChecked(false);
                    it.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                });

                button.setChecked(true);
                button.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

                sharedViewModel.getReservationForm().getValue().setTime(button.getText().toString());

                // Since this is radio button, and date is pre-filled
                sharedViewModel.setFormOneFilled(true);
            });
        });
    }

    private void setDaySelected(int selectedIndex) {
        for (int i = 0; i < buttonDayCard.size(); i++) {
            LinearLayout btn = buttonDayCard.get(i);
            boolean isSelected = (i == selectedIndex);

            btn.setBackgroundResource(isSelected ? R.drawable.button_day_card_selected : R.drawable.button_day_card_unselected);

            TextView tvDay = btn.findViewById(R.id.tvDay);
            TextView tvDate = btn.findViewById(R.id.tvDate);

            int color = ContextCompat.getColor(getContext(), isSelected ? R.color.light_blue : R.color.gray);
            tvDay.setTextColor(color);
            tvDate.setTextColor(color);
        }

        // Update Reservation Form
        TextView dayOfMonthTextView = buttonDayCard.get(selectedIndex).findViewById(R.id.tvDate);
        int dayOfMonth = Integer.parseInt(dayOfMonthTextView.getText().toString());
        String date = LocalDate.now().withDayOfMonth(dayOfMonth).format(DateTimeFormatter.ISO_LOCAL_DATE);
        sharedViewModel.getReservationForm().getValue().setDate(date);

        // --- Logic to enable/disable time buttons ---
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        boolean isSelectedDateToday = LocalDate.now().withDayOfMonth(dayOfMonth).isEqual(today);

        // Reset/clear any previously selected time in ViewModel if the date changes,
        // unless you want to try and preserve it. For simplicity, let's clear it.
        // Also, deselect the radio button visually.
        boolean aTimeSlotNeedsToBeUnchecked = false;

        for (int i = 0; i < buttonTime.size(); i++) {
            RadioButton timeRadButton = buttonTime.get(i);
            String timeSlotText = timeRadButton.getText().toString();

            // Attempt to parse the time slot from the button's text or your predefined array
            LocalTime timeSlot;
            try {
                // Assuming timeSlotText is in "HH:mm" format. Adjust if different.
                timeSlot = LocalTime.parse(timeSlotText, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (Exception e) {
                Log.e("FormOneFragment", "Could not parse time slot: " + timeSlotText, e);
                timeRadButton.setEnabled(true); // Default to enabled if parsing fails
                timeRadButton.setAlpha(1.0f);
                continue; // Skip to next button
            }

            if (isSelectedDateToday) {
                // It's today, check against current time and cutoff
                if (currentTime.isAfter(timeSlot) || currentTime.isAfter(cutoffTime) && timeSlot.isBefore(cutoffTime.plusMinutes(10))) {
                    timeRadButton.setEnabled(false);
                    timeRadButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    timeRadButton.setAlpha(0.5f); // Visually indicate disabled
                    if (timeRadButton.isChecked()) {
                        timeRadButton.setChecked(false); // Uncheck if it was selected and now disabled
                        aTimeSlotNeedsToBeUnchecked = true;
                    }
                } else {
                    timeRadButton.setEnabled(true);
                    timeRadButton.setAlpha(1.0f);
                }
            } else {
                // It's a future date, enable all time slots
                timeRadButton.setEnabled(true);
                timeRadButton.setAlpha(1.0f);
            }
        }

        if (aTimeSlotNeedsToBeUnchecked && sharedViewModel != null && sharedViewModel.getReservationForm().getValue() != null) {
            sharedViewModel.getReservationForm().getValue().setTime(null);
            sharedViewModel.setFormOneFilled(false); // Since time is now unselected
        }

        // After enabling/disabling, if a time was previously selected AND its button is still enabled, re-check it.
        // Otherwise, ensure no time button is checked if the new state makes the previous selection invalid.
        // This part can be tricky if you want to perfectly restore selection.
        // For simplicity, if the date changed and a previously selected time is now disabled,
        // the ViewModel's time should be cleared.

        // Re-evaluate form completeness
        if (sharedViewModel != null && sharedViewModel.getReservationForm().getValue() != null) {
            String currentDate = sharedViewModel.getReservationForm().getValue().getDate();
            String currentTimeSlot = sharedViewModel.getReservationForm().getValue().getTime();
            sharedViewModel.setFormOneFilled(currentDate != null && !currentDate.isEmpty() &&
                    currentTimeSlot != null && !currentTimeSlot.isEmpty());
        }
    }
}