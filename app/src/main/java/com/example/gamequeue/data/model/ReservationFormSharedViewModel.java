package com.example.gamequeue.data.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReservationFormSharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> isFormOneFilled = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isFormTwoFilled = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isFormThreeFilled = new MutableLiveData<>(false);
    private MutableLiveData<ReservationModel> reservationForm = new MutableLiveData<>(new ReservationModel());

    public MutableLiveData<ReservationModel> getReservationForm() {
        return reservationForm;
    }
    public void setReservationForm(ReservationModel form) { reservationForm.setValue(form); }

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
