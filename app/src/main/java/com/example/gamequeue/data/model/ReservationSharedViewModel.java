package com.example.gamequeue.data.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReservationSharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> isFormOneFilled = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isFormTwoFilled = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isFormThreeFilled = new MutableLiveData<>(false);
    private MutableLiveData<ReservationFormModel> reservationForm = new MutableLiveData<>(new ReservationFormModel());

    public MutableLiveData<ReservationFormModel> getReservationForm() {
        return reservationForm;
    }
    public void setFormOneFilled(boolean filled) {
        isFormOneFilled.setValue(filled);
    }

    public void setFormTwoFilled(boolean filled) {
        isFormTwoFilled.setValue(filled);
    }

    public void setFormThreeFilled(boolean filled) {
        isFormThreeFilled.setValue(filled);
    }

    public MutableLiveData<Boolean> getFormOneFilled() {
        return isFormOneFilled;
    }

    public MutableLiveData<Boolean> getFormTwoFilled() {
        return isFormTwoFilled;
    }

    public MutableLiveData<Boolean> getFormThreeFilled() {
        return isFormThreeFilled;
    }
}
