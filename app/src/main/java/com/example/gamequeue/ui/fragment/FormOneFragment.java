package com.example.gamequeue.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.data.model.ConsoleSharedViewModel;
import com.example.gamequeue.data.model.ReservationSharedViewModel;
import com.example.gamequeue.ui.adapter.ViewHolders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FormOneFragment extends Fragment {
    private List<LinearLayout> buttonDayCard;
    private List<RadioButton> buttonTime;
    private ReservationSharedViewModel sharedViewModel;
    private TextView consoleName, status, specificationOne, specificationTwo, specificationThree;
    private ConsoleModel currentConsole;

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
        sharedViewModel = new ViewModelProvider(requireActivity()).get(ReservationSharedViewModel.class);

        // Set UI Visual
        fetchConsole();
        setupCards();
        setupListener();
    }

    // TODO: CHANGE IMPLEMENTATION TO FETCH THROUGH DATABASE
    private void fetchConsole() {
        // Ensure ID is passed from Adapter
        int id = getActivity().getIntent().getIntExtra("id", -1);

        if (id == -1) {
            return;
        }

        currentConsole = ConsoleSharedViewModel.getConsoleList().stream().filter(it -> it.getId() == id).findFirst().orElse(null);

        if (currentConsole != null) {
            consoleName.setText(currentConsole.getTitle());
            status.setText(currentConsole.getStatus());
            ViewHolders.statusChanger(status);
            specificationOne.setText(currentConsole.getSpecificationOne());
            specificationTwo.setText(currentConsole.getSpecificationTwo());
            specificationThree.setText(currentConsole.getSpecificationThree());

            // Pass it to ReservationFormModel
            sharedViewModel.getReservationForm().getValue().setConsoleId(id);
        }
    }

    private void setupCards() {
        // Set Default Selected According to the nearest date 1-5
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            setDaySelected(0);
        } else {
            setDaySelected(dayOfWeek - 1);
        }

        // Get the date of the month to setup the date visual
        // Get Monday First
        LocalDate monday = LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        for (int i = 0; i < 5; i++) {
            LocalDate dayToShow = monday.plusDays(i);
            ((TextView) buttonDayCard.get(i).findViewById(R.id.tvDate)).setText(String.valueOf(dayToShow.getDayOfMonth()));
        }

        // TODO: Setup Rollover weekdays

        ((TextView) buttonDayCard.get(1).findViewById(R.id.tvDay)).setText("Selasa");
        ((TextView) buttonDayCard.get(2).findViewById(R.id.tvDay)).setText("Rabu");
        ((TextView) buttonDayCard.get(3).findViewById(R.id.tvDay)).setText("Kamis");
        ((TextView) buttonDayCard.get(4).findViewById(R.id.tvDay)).setText("Jumat");
    }

    private void setupListener() {
        // Setup Listener for ButtonDayCard
        for (int i = 0; i < buttonDayCard.size(); i++) {
            final int index = i;
            buttonDayCard.get(i).setOnClickListener(v -> setDaySelected(index));
        }

        // Setup Listener for ButtonTime
        buttonTime.forEach(button -> {
            button.setOnClickListener(v -> {
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
        TextView date = buttonDayCard.get(selectedIndex).findViewById(R.id.tvDate);
        sharedViewModel.getReservationForm().getValue().setDate(date.getText().toString());
    }
}