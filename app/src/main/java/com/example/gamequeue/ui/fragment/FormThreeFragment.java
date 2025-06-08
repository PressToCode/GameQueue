package com.example.gamequeue.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
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
import android.widget.Toast;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ReservationSharedViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;

public class FormThreeFragment extends Fragment {
    // Variables
    private FrameLayout uploadBox;
    private LinearLayout placeholderLayout;
    private ImageView imagePreview;
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
        // Update UI and Form Data
        ActivityResultLauncher<Intent> imagePickerLauncher;

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
            int resultCode = result.getResultCode();
            Intent data = result.getData();

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                Uri fileUri = data.getData();

                // Update UI and Form Data
                imagePreview.setImageURI(fileUri);
                imagePreview.setVisibility(View.VISIBLE);
                placeholderLayout.setVisibility(View.GONE);
                sharedViewModel.getReservationForm().getValue().setDocument(fileUri);
                sharedViewModel.setFormThreeFilled(true);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                imagePreview.setVisibility(View.GONE);
                placeholderLayout.setVisibility(View.VISIBLE);
                sharedViewModel.setFormThreeFilled(false);
            } else {
                Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                imagePreview.setVisibility(View.GONE);
                placeholderLayout.setVisibility(View.VISIBLE);
                sharedViewModel.setFormThreeFilled(false);
            }
        });

        // On Click Listener
        uploadBox.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop(16f, 9f)
                    .compress(2048)
                    .createIntent( intent -> {
                        imagePickerLauncher.launch(intent);
                        return Unit.INSTANCE;
                    });
        });

        imagePreview.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop(16f, 9f)
                    .compress(2048)
                    .createIntent( intent -> {
                        imagePickerLauncher.launch(intent);
                        return Unit.INSTANCE;
                    });
        });

    }
}