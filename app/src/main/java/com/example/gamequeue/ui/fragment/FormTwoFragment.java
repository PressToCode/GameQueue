package com.example.gamequeue.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.data.model.ReservationFormSharedViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class FormTwoFragment extends Fragment {
    // Variables
    private TextInputEditText formNameField, formNIMField, formPhoneField;
    private List<TextInputEditText> fieldList;
    private MaterialAutoCompleteTextView formProdiDropdown;
    private ReservationFormSharedViewModel sharedViewModel;

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
        fieldList = List.of(formNameField, formNIMField, formPhoneField);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(ReservationFormSharedViewModel.class);

        // Set Listener
        setupListener();

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

    private void setupListener() {
        fieldList.forEach(field -> {
            field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    inputFormData(field);
                    checkFilled();
                }
            });
        });

        formProdiDropdown.setOnItemClickListener((parent, view, position, id) -> {
            inputFormData(formProdiDropdown);
            checkFilled();
        });
    }

    private void inputFormData(TextView textView) {
        String text = textView.getText().toString();
        if(text == null || text.isEmpty()) {
            return;
        }

        ReservationModel form = sharedViewModel.getReservationForm().getValue();

        if(textView.getId() == R.id.formProdiDropdown) {
            form.setLenderProdi(text);
            return;
        }

        if(textView.getId() == R.id.formNameField) {
            form.setLenderName(text);
            return;
        }

        if(textView.getId() == R.id.formNIMField) {
            form.setLenderNIM(text);
            return;
        }

        if(textView.getId() == R.id.formPhoneField) {
            form.setLenderPhone(text);
        }
    }

    private void checkFilled() {
        boolean isFilled = true;

        for(TextInputEditText field : fieldList) {
            if(field.getText().toString().isEmpty()) {
                isFilled = false;
                break;
            }
        }

        if(formProdiDropdown.getText().toString().isEmpty()) {
            isFilled = false;
        }

        sharedViewModel.setFormTwoFilled(isFilled);
    }
}