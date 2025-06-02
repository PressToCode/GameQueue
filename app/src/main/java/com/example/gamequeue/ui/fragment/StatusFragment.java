package com.example.gamequeue.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.ui.adapter.ConsoleAdapter;

import java.util.ArrayList;

public class StatusFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ConsoleModel> consoleList;

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

        // Set Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ConsoleAdapter adapter = new ConsoleAdapter(getContext(), R.layout.card_item_three, consoleList);
        recyclerView.setAdapter(adapter);

        // Load Dummy Data
        loadDummyData();
    }

    private void loadDummyData() {
        // Dummy data loading logic
        consoleList.clear();
        consoleList.add(new ConsoleModel("XBOX", 0, "Day, DD MM YY", "Time Timezone"));
        consoleList.add(new ConsoleModel("Playstation 5", 1, "Day, DD MM YY", "Time Timezone"));
        consoleList.add(new ConsoleModel("Desktop PC", 2, "Day, DD MM YY", "Time Timezone"));
    }
}