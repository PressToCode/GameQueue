package com.example.gamequeue.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.gamequeue.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

public class FormTwoFragment extends Fragment {
    // Variables
    private TextInputEditText formNameField, formNIMField, formPhoneField;
    private MaterialAutoCompleteTextView formProdiDropdown;

    public FormTwoFragment() {
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
        return inflater.inflate(R.layout.fragment_form_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialization
        formNameField = view.findViewById(R.id.formNameField);
        formNIMField = view.findViewById(R.id.formNIMField);
        formPhoneField = view.findViewById(R.id.formPhoneField);
        formProdiDropdown = view.findViewById(R.id.formProdiDropdown);

        // Set Dropdown Items
        setDropdownItems();
    }

    private void setDropdownItems() {
        String[] menus = {
                "Teknologi Informasi",
                "Teknik Informatika",
                "Sistem Informasi",
                "Pendidikan Teknologi Informasi",
                "Teknik Komputer"
        };

        formProdiDropdown.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, menus));
    }
}