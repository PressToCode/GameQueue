package com.example.gamequeue.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.data.model.ConsoleSharedViewModel;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.data.model.ReservationSharedViewModel;
import com.example.gamequeue.data.repository.DatabaseRepository;
import com.example.gamequeue.ui.adapter.ConsoleAdapter;
import com.example.gamequeue.ui.main.HistoryActivity;
import com.example.gamequeue.utils.ApplicationContext;
import com.example.gamequeue.utils.CustomCallbackWithType;

import java.util.ArrayList;

public class StatusFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ConsoleModel> consoleList;
    private ArrayList<ReservationModel> reservationList;
    private ConsoleAdapter adapter;
    private RadioGroup radioGroup;
    private RadioButton radioPending, radioCompleted, radioCanceled;
    private int currentFilterId = -1;
    private ConsoleSharedViewModel consoleSharedViewModel;
    private ReservationSharedViewModel reservationSharedViewModel;
    private ImageButton toHistoryBtn;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consoleList = new ArrayList<>();
        reservationList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialization
        recyclerView = view.findViewById(R.id.statusRecycler);
        radioGroup = view.findViewById(R.id.filter_radio_group_status);
        radioPending = view.findViewById(R.id.radio_button_pending_status);
        radioCompleted = view.findViewById(R.id.radio_button_completed_status);
        radioCanceled = view.findViewById(R.id.radio_button_canceled_status);
        toHistoryBtn = view.findViewById(R.id.toHistoryBtn);
        consoleSharedViewModel = new ViewModelProvider(requireActivity()).get(ConsoleSharedViewModel.class);

        // TODO: Check for Admin Case --------------------------
        reservationSharedViewModel = new ViewModelProvider(requireActivity()).get(ReservationSharedViewModel.class);

        // Load Data
        // TODO: Check for Admin Case --------------------------
        loadData();

        // Set Adapter
        // TODO: Check for Admin Case --------------------------
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ConsoleAdapter(getContext(), R.layout.card_item_three, consoleList, reservationList, consoleSharedViewModel);
        recyclerView.setAdapter(adapter);

        // Setup Filterer
        setupFilterer();

        // Redirect to History Activity
        toHistoryBtn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), HistoryActivity.class));
        });
    }

    private void loadData() {
        consoleSharedViewModel.getConsoleListLive().observe(getViewLifecycleOwner(), consoleModels -> {
            if(consoleModels == null || consoleModels.isEmpty()) {
                return;
            }

            consoleList.clear();
            consoleList.addAll(consoleModels);
        });

        if(!ApplicationContext.getDevMode()) {
            reservationSharedViewModel.getFilteredReservationListLiveTwo().observe(getViewLifecycleOwner(), reservationModels -> {
                reservationList.clear();

                if(reservationModels != null && !reservationModels.isEmpty()) {
                    reservationList.addAll(reservationModels);
                }

                adapter.notifyDataSetChanged();
            });
        }
    }

    private void setupFilterer() {
        View.OnClickListener radioBtnListener = v -> {
            RadioButton clickedButton = (RadioButton) v;

            if(clickedButton.isChecked() && clickedButton.getId() == currentFilterId) {
                radioGroup.clearCheck();
            }

            currentFilterId = radioGroup.getCheckedRadioButtonId();
        };

        radioPending.setOnClickListener(radioBtnListener);
        radioCompleted.setOnClickListener(radioBtnListener);
        radioCanceled.setOnClickListener(radioBtnListener);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            applyFilter(checkedId);
        });
    }

    // TODO: IMPLEMENT THIS
    private void applyFilter(int checkedId) {
        // Apply Filtering
        if(checkedId == R.id.radio_button_pending_status) {
            reservationSharedViewModel.setFilterStatusTwo("pending");
        } else if(checkedId == R.id.radio_button_completed_status) {
            reservationSharedViewModel.setFilterStatusTwo("completed");
        } else if(checkedId == R.id.radio_button_canceled_status) {
            reservationSharedViewModel.setFilterStatusTwo("canceled");
        } else if(checkedId == -1) {
            reservationSharedViewModel.setFilterStatusTwo("");
        }

        adapter.notifyDataSetChanged();
    }
}