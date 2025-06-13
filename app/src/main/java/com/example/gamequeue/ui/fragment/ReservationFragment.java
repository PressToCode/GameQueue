package com.example.gamequeue.ui.fragment;

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

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.data.sharedViewModel.ConsoleSharedViewModel;
import com.example.gamequeue.ui.adapter.ConsoleAdapter;

import java.util.ArrayList;

public class ReservationFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ConsoleModel> consoleList;
    private ConsoleSharedViewModel consoleSharedViewModel;
    private ConsoleAdapter adapter;

    public ReservationFragment() {
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
        return inflater.inflate(R.layout.fragment_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialization
        recyclerView = view.findViewById(R.id.reservationRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        consoleSharedViewModel = new ViewModelProvider(requireActivity()).get(ConsoleSharedViewModel.class);

        // Load Data
        loadData();

        adapter = new ConsoleAdapter(getContext(), R.layout.card_item_two, consoleList, null, consoleSharedViewModel);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        consoleList.clear();
        consoleSharedViewModel.getConsoleListLive().observe(getViewLifecycleOwner(), consoleModels -> {
            if(consoleModels == null || consoleModels.isEmpty()) {
                consoleList.clear();
                return;
            }

            consoleList.clear();
            consoleList.addAll(consoleModels);
            adapter.notifyDataSetChanged();
        });
    }
}