package com.example.gamequeue.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.data.model.MainSharedViewModel;
import com.example.gamequeue.ui.adapter.ConsoleAdapter;

import java.util.ArrayList;

public class StatusFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ConsoleModel> consoleList;
    private MainSharedViewModel viewModel;
    private ConsoleAdapter adapter;
    private RadioGroup radioGroup;
    private RadioButton radioPending, radioCompleted, radioCanceled;
    private int currentFilterId = -1;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consoleList = new ArrayList<>();
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
        viewModel = new ViewModelProvider(requireActivity()).get(MainSharedViewModel.class);

        // Set Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ConsoleAdapter(getContext(), R.layout.card_item_three, consoleList);
        recyclerView.setAdapter(adapter);

        // Load Dummy Data
        loadDummyData();

        // Setup Filterer
        setupFilterer();
    }

    private void loadDummyData() {
        // Dummy data loading logic
        consoleList.clear();
        consoleList.addAll(viewModel.getConsoleList());
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

    private void applyFilter(int checkedId) {
        // Clear List
        consoleList.clear();

        // Apply Filtering
        if(checkedId == R.id.radio_button_pending_status) {
            consoleList.addAll(viewModel.getPendingConsoleList());
        } else if(checkedId == R.id.radio_button_completed_status) {
            consoleList.addAll(viewModel.getCompletedConsoleList());
        } else if(checkedId == R.id.radio_button_canceled_status) {
            consoleList.addAll(viewModel.getCanceledConsoleList());
        } else if(checkedId == -1) {
            consoleList.addAll(viewModel.getConsoleList());
        }

        adapter.notifyDataSetChanged();
    }
}