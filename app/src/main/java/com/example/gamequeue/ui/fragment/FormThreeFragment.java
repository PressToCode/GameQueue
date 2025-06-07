package com.example.gamequeue.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ReservationSharedViewModel;

public class FormThreeFragment extends Fragment {
    // Variables
    private FrameLayout uploadBox;
    private LinearLayout placeholderLayout;
    private ImageView imagePreview;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private ReservationSharedViewModel sharedViewModel;


    public FormThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_three, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialization
        uploadBox = view.findViewById(R.id.uploadBox);
        placeholderLayout = view.findViewById(R.id.placeholderLayout);
        imagePreview = view.findViewById(R.id.imagePreview);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(ReservationSharedViewModel.class);

        // Setup Listener
        setupListeners();
    }

    private void setupListeners() {
        // Register launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imagePreview.setImageURI(uri);
                        imagePreview.setVisibility(View.VISIBLE);
                        placeholderLayout.setVisibility(View.GONE);
                        sharedViewModel.getReservationForm().getValue().setDocument(uri);
                        sharedViewModel.setFormThreeFilled(true);
                    } else {
                        imagePreview.setVisibility(View.GONE);
                        placeholderLayout.setVisibility(View.VISIBLE);
                        sharedViewModel.setFormThreeFilled(false);
                    }
                }
        );

        // On Click Listener
        uploadBox.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

        imagePreview.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

    }
}