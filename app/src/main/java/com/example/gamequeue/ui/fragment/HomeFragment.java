package com.example.gamequeue.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.data.sharedViewModel.ConsoleSharedViewModel;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.data.sharedViewModel.ReservationSharedViewModel;
import com.example.gamequeue.data.model.SharedProfileModel;
import com.example.gamequeue.ui.adapter.ConsoleAdapter;
import com.example.gamequeue.ui.main.ProfileActivity;
import com.example.gamequeue.utils.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView statusReservationText;
    private NestedScrollView scrollContainer;
    private LinearLayout contentHolder;
    private ConsoleAdapter adapter;
    private ArrayList<ConsoleModel> consoleList;
    private ArrayList<ReservationModel> reservationList;
    private CardView recommendationCard;
    private ImageView recommendedImage;
    private ImageButton searchButton;
    private EditText searchField;
    private TextView greetingText, recommendedTitle, recommendedStatus, recommendedSpecificationOne, recommendedSpecificationTwo, recommendedSpecificationThree;
    private RadioGroup radioGroup;
    private RadioButton radioPending, radioCompleted, radioCanceled;
    private int currentFilterId = -1;
    private ConsoleSharedViewModel consoleSharedViewModel;
    private ReservationSharedViewModel reservationSharedViewModel;

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
        profileButton = view.findViewById(R.id.imageButton2);
        greetingText = view.findViewById(R.id.text_greeting);
        searchButton = view.findViewById(R.id.searchButton);
        searchField = view.findViewById(R.id.edit_search);
        recommendationCard = view.findViewById(R.id.card_recommendation);
        recommendedImage = view.findViewById(R.id.recommendedImage);
        recommendedTitle = view.findViewById(R.id.recommendedTitle);
        recommendedStatus = view.findViewById(R.id.recommendedStatus);
        recommendedSpecificationOne = view.findViewById(R.id.recommendedSpecificationOne);
        recommendedSpecificationTwo = view.findViewById(R.id.recommendedSpecificationTwo);
        recommendedSpecificationThree = view.findViewById(R.id.recommendedSpecificationThree);
        radioGroup = view.findViewById(R.id.filter_radio_group);
        radioPending = view.findViewById(R.id.radio_button_pending);
        radioCompleted = view.findViewById(R.id.radio_button_completed);
        radioCanceled = view.findViewById(R.id.radio_button_canceled);
        consoleSharedViewModel = new ViewModelProvider(requireActivity()).get(ConsoleSharedViewModel.class);

        // TODO: Check for Admin Case --------------------------
        reservationSharedViewModel = new ViewModelProvider(requireActivity()).get(ReservationSharedViewModel.class);

        // Change Greeting
        setupGreeting();

        // Setup profile button click listener
        setupProfileButton();

        // Some Setup because RecyclerView is being an a**
        setupRecycler();

        // Load Data
        loadData();

        // Set Adapter
        // TODO: Check for Admin Case --------------------------
        adapter = new ConsoleAdapter(getContext(), R.layout.card_item_one, consoleList, reservationList, consoleSharedViewModel, null);
        recyclerView.setAdapter(adapter);

        // Set up filter function
        // TODO: Check for Admin Case --------------------------
        setupFilterer();
    }

    private void setupGreeting() {
        greetingText.setText("Hai, " + SharedProfileModel.getName() + "!");
    }

    private void setupProfileButton() {
        profileButton.setOnClickListener(v -> {
            // Navigate to User Profile Activity
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void loadData() {
        consoleSharedViewModel.getConsoleListLive().observe(getViewLifecycleOwner(), consoleModels -> {
            if(consoleModels == null || consoleModels.isEmpty()) {
                return;
            }

            consoleList.clear();
            consoleList.addAll(consoleModels);
            loadRecommendedData();
        });

        reservationSharedViewModel.getFilteredReservationListLiveOne().observe(getViewLifecycleOwner(), reservationModels -> {
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
            int filterButtonsHeight = radioGroup.getHeight();
            ViewGroup.MarginLayoutParams filterMargins = (ViewGroup.MarginLayoutParams) radioGroup.getLayoutParams();
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
            }
        });
    }

    private void loadRecommendedData() {
        if (consoleList == null) {
            return;
        }

        Optional<ConsoleModel> firstMatch = consoleList.stream().filter(consoleModel -> consoleModel != null && consoleModel.getAvailabilityStatus()).findFirst();

        if (firstMatch.isEmpty()) {
            return;
        }

        recommendedTitle.setText(firstMatch.get().getTitle());
        recommendedStatus.setText("Available");
        recommendedSpecificationOne.setText(firstMatch.get().getSpecificationOne());
        recommendedSpecificationTwo.setText(firstMatch.get().getSpecificationTwo());
        recommendedSpecificationThree.setText(firstMatch.get().getSpecificationThree());

        recommendationCard.setVisibility(View.VISIBLE);

        recommendationCard.setOnClickListener(v -> {
            startActivity(ConsoleAdapter.getReservationProcessIntent(getContext(), firstMatch.get()));
        });
    }

    private void setupFilterer() {
        // Create OnClickListener Instance
        View.OnClickListener radioBtnListener = v -> {
          RadioButton clickedButton = (RadioButton) v;

          if(clickedButton.isChecked() && clickedButton.getId() == currentFilterId) {
              radioGroup.clearCheck();
          }

          currentFilterId = radioGroup.getCheckedRadioButtonId();
        };

        // Apply Listeners
        radioPending.setOnClickListener(radioBtnListener);
        radioCompleted.setOnClickListener(radioBtnListener);
        radioCanceled.setOnClickListener(radioBtnListener);

        // Check Changed Listener
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            applyFilter(checkedId);
        });

        // Search Field Listener
        searchField.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                // If the event is a key-down event on the Enter key
                // (This part is to handle cases where actionId might not be set, e.g., on some hardware keyboards)
                if (keyEvent != null && keyEvent.getAction() != KeyEvent.ACTION_DOWN) {
                    return true;
                }

                if(searchField.getText().toString().isEmpty()) {
                    searchFilter(new ArrayList<>());
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
                    searchFilter(new ArrayList<>(Arrays.asList(searchText)));
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
            searchFilter(new ArrayList<>(Arrays.asList(searchText)));
        });
    }

    // TODO: IMPLEMENT THIS
    private void searchFilter(ArrayList<String> searchText) {
        if(radioGroup.getCheckedRadioButtonId() != -1) {
            // Get Which Status is clicked
            int checkedId = radioGroup.getCheckedRadioButtonId();
            String status = "";

            if(checkedId == R.id.radio_button_pending) {
                status = "pending";
            } else if (checkedId == R.id.radio_button_completed) {
                status = "completed";
            } else if (checkedId == R.id.radio_button_canceled) {
                status = "canceled";
            }

            // Filter by status AND word
            reservationSharedViewModel.setFilterStatusOne(status);
            reservationSharedViewModel.setFilterWordOne(searchText);

            // Update is already handled by observer
        } else {
            reservationSharedViewModel.setFilterWordOne(searchText);
        }

        adapter.notifyDataSetChanged();
    }

    // TODO: IMPLEMENT THIS
    private void applyFilter(int checkedId) {
        // See if there's ongoing filter
        String[] searchText = searchField.getText().toString().trim().split("\\s+");

        // Apply Filtering
        if(checkedId == R.id.radio_button_pending) {
            reservationSharedViewModel.setFilterStatusOne("pending");
            reservationSharedViewModel.setFilterWordOne(new ArrayList<>(Arrays.asList(searchText)));
        } else if(checkedId == R.id.radio_button_completed) {
            reservationSharedViewModel.setFilterStatusOne("completed");
            reservationSharedViewModel.setFilterWordOne(new ArrayList<>(Arrays.asList(searchText)));
        } else if(checkedId == R.id.radio_button_canceled) {
            reservationSharedViewModel.setFilterStatusOne("canceled");
            reservationSharedViewModel.setFilterWordOne(new ArrayList<>(Arrays.asList(searchText)));
        } else if(checkedId == -1) {
            reservationSharedViewModel.setFilterStatusOne("");
            reservationSharedViewModel.setFilterWordOne(new ArrayList<>(Arrays.asList(searchText)));
        }

        adapter.notifyDataSetChanged();
    }
}