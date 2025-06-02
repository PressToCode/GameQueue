package com.example.gamequeue.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.data.model.SharedViewModel;
import com.example.gamequeue.ui.adapter.ConsoleAdapter;
import com.example.gamequeue.ui.main.UserProfileActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView statusReservationText;
    private NestedScrollView scrollContainer;
    private LinearLayout contentHolder, filterButtons;
    private ConsoleAdapter adapter;
    private ArrayList<ConsoleModel> consoleList;
    private SharedViewModel viewModel;

    // Add profile button variable
    private ImageButton profileButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Data fetching should run here
        consoleList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialization
        recyclerView = view.findViewById(R.id.recycler_view_reservations);
        statusReservationText = view.findViewById(R.id.statusReservationText);
        scrollContainer = view.findViewById(R.id.scroll_container);
        contentHolder = view.findViewById(R.id.scroll_container_content);
        filterButtons = view.findViewById(R.id.filter_radio_group);
        profileButton = view.findViewById(R.id.imageButton2); // Profile button
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Setup profile button click listener
        setupProfileButton();

        // Some Setup because RecyclerView is being an a**
        setupRecycler();

        // Set Adapter
        adapter = new ConsoleAdapter(getContext(), R.layout.card_item_one, consoleList);
        recyclerView.setAdapter(adapter);

        // Load Dummy Data
        loadDummyData();
    }

    private void setupProfileButton() {
        profileButton.setOnClickListener(v -> {
            // Navigate to User Profile Activity
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            startActivity(intent);
        });
    }

    private void loadDummyData() {
        // Dummy data loading logic
        consoleList.clear();
        consoleList.addAll(viewModel.getConsoleList());
    }

    private void setupRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        scrollContainer.post(() -> {
            if (getContext() == null) return;

            // 1. Get the actual available height INSIDE the NestedScrollView's viewport
            // This accounts for scrollContainer's own padding, if any.
            int availableHeightInScrollContainer = scrollContainer.getHeight()
                    - scrollContainer.getPaddingTop()
                    - scrollContainer.getPaddingBottom();

            // 2. The contentHolder LinearLayout is supposed to match this.
            // Let's verify its padding if it affects calculations for RV.
            int contentHolderPaddingTop = contentHolder.getPaddingTop();
            int contentHolderPaddingBottom = contentHolder.getPaddingBottom();

            // 3. Get height of TextView status reservation
            int statusReservationTextHeight = statusReservationText.getHeight();
            ViewGroup.MarginLayoutParams statusReservationTextMargins = (ViewGroup.MarginLayoutParams) statusReservationText.getLayoutParams();
            int statusReservationTextTotalHeightWithMargins = statusReservationTextHeight + statusReservationTextMargins.topMargin + statusReservationTextMargins.bottomMargin;

            // 4. Get height of filter buttons (including its margins)
            int filterButtonsHeight = filterButtons.getHeight();
            ViewGroup.MarginLayoutParams filterMargins = (ViewGroup.MarginLayoutParams) filterButtons.getLayoutParams();
            int filterButtonsTotalHeightWithMargins = filterButtonsHeight + filterMargins.topMargin + filterMargins.bottomMargin;

            // 5. Calculate target height for RecyclerView
            // This is the space within contentHolder, after filters and contentHolder's own padding.
            int recyclerViewTargetHeight = availableHeightInScrollContainer // Total space for contentHolder
                    - contentHolderPaddingTop      // Subtract contentHolder's top padding
                    - statusReservationTextTotalHeightWithMargins // Subtract statusReservationText
                    - filterButtonsTotalHeightWithMargins // Subtract filters
                    - contentHolderPaddingBottom;  // Subtract contentHolder's bottom padding


            if (recyclerViewTargetHeight < 0) {
                recyclerViewTargetHeight = 0;
            }

            ViewGroup.LayoutParams rvLayoutParams = recyclerView.getLayoutParams();
            if (rvLayoutParams.height != recyclerViewTargetHeight) {
                rvLayoutParams.height = recyclerViewTargetHeight;
                recyclerView.setLayoutParams(rvLayoutParams);
//                 Log.d("RV_RESIZE", "Set RV height to: " + recyclerViewTargetHeight);
            } else {
//                 Log.d("RV_RESIZE", "RV height already correct: " + rvLayoutParams.height);
            }
        });
    }
}