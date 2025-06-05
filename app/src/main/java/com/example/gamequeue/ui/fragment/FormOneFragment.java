package com.example.gamequeue.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gamequeue.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FormOneFragment extends Fragment {
    private List<LinearLayout> buttonDayCard;

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

        // Setup Listener
        for (int i = 0; i < buttonDayCard.size(); i++) {
            final int index = i;
            buttonDayCard.get(i).setOnClickListener(v -> setDaySelected(index));
        }

        // Set UI Visual
        setupCards();
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
    }
}