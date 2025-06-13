package com.example.gamequeue.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamequeue.R;
import com.example.gamequeue.data.sharedViewModel.ConsoleSharedViewModel;
import com.example.gamequeue.data.sharedViewModel.RequestSharedViewModel;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.data.model.SharedProfileModel;
import com.example.gamequeue.ui.adapter.ConsoleAdapter;
import com.example.gamequeue.ui.main.ProfileActivity;

import java.util.ArrayList;
import java.util.Arrays;


/*
 * This version offer no recommendation card
 * No radio filters either (since it won't make sense)
 */
public class AdminHomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView statusReservationText;
    private NestedScrollView scrollContainer;
    private LinearLayout contentHolder;
    private ConsoleAdapter adapter;
    private CardView recommendationCard;
    private ImageButton searchButton, profileButton;
    private EditText searchField;
    private TextView greetingText;
    private RadioGroup radioGroup;
    private ArrayList<ReservationModel> reservationList;
    private RequestSharedViewModel requestSharedViewModel;
    private ConsoleSharedViewModel consoleSharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Lists
        reservationList = new ArrayList<>();
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
        greetingText = view.findViewById(R.id.text_greeting);
        searchButton = view.findViewById(R.id.searchButton);
        profileButton = view.findViewById(R.id.imageButton2);
        searchField = view.findViewById(R.id.edit_search);
        recommendationCard = view.findViewById(R.id.card_recommendation);
        radioGroup = view.findViewById(R.id.filter_radio_group);

        // Remove Things & Change Things
        statusReservationText.setText("Reservasi Masuk");
        recommendationCard.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);

        // Data fetching
        requestSharedViewModel = new ViewModelProvider(requireActivity()).get(RequestSharedViewModel.class);
        consoleSharedViewModel = new ViewModelProvider(requireActivity()).get(ConsoleSharedViewModel.class);

        // Setup Top-part (Incl. Profile & Greeting)
        setupTopContainer();

        // Some Setup because RecyclerView is being an a**
        setupRecycler();

        // Load Data
        loadData();

        // Create Adapter Here
        adapter = new ConsoleAdapter(getContext(), R.layout.card_item_four, null, reservationList, consoleSharedViewModel, requestSharedViewModel);

        // Setup Adapter
        recyclerView.setAdapter(adapter);

        // Setup Filterer
        setupFilterer();
    }

    private void setupTopContainer() {
        // Set the name
        greetingText.setText("Hai, " + SharedProfileModel.getName() + "!");

        // Setup profile button click listener
        profileButton.setOnClickListener(v -> {
            // Navigate to User Profile Activity
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        // Setup Hint for Search Bar
        searchField.setHint("Cari Request Reservasi");
    }

    private void loadData() {
        // TODO: CHANGE THIS
        requestSharedViewModel.getFilteredReservationListLive().observe(getViewLifecycleOwner(), reservationModels -> {
            reservationList.clear();

            if(reservationModels != null || !reservationModels.isEmpty()) {
                reservationList.addAll(reservationModels);
            }

            adapter.notifyDataSetChanged();
        });
    }

    private void setupRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        scrollContainer.post(() -> {
            if (getContext() == null) return;

            // Scroll Container
            int availableHeightInScrollContainer = scrollContainer.getHeight()
                    - scrollContainer.getPaddingTop()
                    - scrollContainer.getPaddingBottom();

            // Content Holder (LinearLayout)
            int contentHolderPaddingTop = contentHolder.getPaddingTop();
            int contentHolderPaddingBottom = contentHolder.getPaddingBottom();

            // Status Text
            int statusReservationTextHeight = statusReservationText.getHeight();
            ViewGroup.MarginLayoutParams statusReservationTextMargins = (ViewGroup.MarginLayoutParams) statusReservationText.getLayoutParams();
            int statusReservationTextTotalHeightWithMargins = statusReservationTextHeight + statusReservationTextMargins.topMargin + statusReservationTextMargins.bottomMargin;

            // Calculate Space
            int recyclerViewTargetHeight = availableHeightInScrollContainer
                    - contentHolderPaddingTop
                    - statusReservationTextTotalHeightWithMargins
                    - contentHolderPaddingBottom;

            // Prevent Negative Size Render
            if (recyclerViewTargetHeight < 0) {
                recyclerViewTargetHeight = 0;
            }

            ViewGroup.LayoutParams rvLayoutParams = recyclerView.getLayoutParams();
            if (rvLayoutParams.height != recyclerViewTargetHeight) {
                rvLayoutParams.height = recyclerViewTargetHeight;
                recyclerView.setLayoutParams(rvLayoutParams);
            }
        });
    }

    private void setupFilterer() {
        searchField.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                if (keyEvent != null && keyEvent.getAction() != KeyEvent.ACTION_DOWN) {
                    return true;
                }

                if(searchField.getText().toString().isEmpty()) {
                    requestSharedViewModel.setFilterWord(new ArrayList<>());
                    return true;
                }

                // Get Search Filters
                String[] searchText = searchField.getText().toString().trim().split("\\s+");

                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                if (imm != null && requireActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
                }
                searchField.clearFocus();

                if (searchText.length == 1 && searchText[0].isEmpty()) {
                    return true;
                }

                if(searchText.length != 0) {
                    requestSharedViewModel.setFilterWord(new ArrayList<>(Arrays.asList(searchText)));
                }
                return true;
            }
            return false;
        });

        // Search Button Listener
        searchButton.setOnClickListener(v -> {
            if(searchField.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Search Field is Empty", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] searchText = searchField.getText().toString().trim().split("\\s+");
            requestSharedViewModel.setFilterWord(new ArrayList<>(Arrays.asList(searchText)));
        });
    }
}
