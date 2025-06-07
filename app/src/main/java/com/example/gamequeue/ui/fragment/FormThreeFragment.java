package com.example.gamequeue.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gamequeue.R;

public class FormThreeFragment extends Fragment {
    // Variables
    private FrameLayout uploadBox;
    private LinearLayout placeholderLayout;
    private ImageView imagePreview;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;


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

        // Setup Listener
        setupListeners();
    }

    private void setupListeners() {
        // Register launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri; // Later for data passing
                        imagePreview.setImageURI(uri);
                        imagePreview.setVisibility(View.VISIBLE);
                        placeholderLayout.setVisibility(View.GONE);
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