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
import com.example.gamequeue.data.model.MainSharedViewModel;
import com.example.gamequeue.ui.adapter.ConsoleAdapter;

import java.util.ArrayList;

public class ReservationFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ConsoleModel> consoleList;
    private MainSharedViewModel viewModel;

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
        viewModel = new ViewModelProvider(requireActivity()).get(MainSharedViewModel.class);

        // Set Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ConsoleAdapter adapter = new ConsoleAdapter(getContext(), R.layout.card_item_two, consoleList);
        recyclerView.setAdapter(adapter);

        // Load Dummy Data
        loadDummyData();
    }

    private void loadDummyData() {
        consoleList.clear();
        consoleList.addAll(viewModel.getConsoleList());
    }
}