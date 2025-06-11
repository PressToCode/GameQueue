package com.example.gamequeue.utils;

import android.content.res.ColorStateList;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.gamequeue.R;

public class WidgetModifier {
    public static void statusChanger(@NonNull TextView status) {
        String text = status.getText().toString();

        if(text.equalsIgnoreCase("Pending")) {
            status.setTextColor(ContextCompat.getColor(ApplicationContext.getAppContext(), R.color.pending_fg));
            status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ApplicationContext.getAppContext(), R.color.pending_bg)));
        }

        if (text.equalsIgnoreCase("Completed") || text.equalsIgnoreCase("Approved") || text.equalsIgnoreCase("Tersedia")) {
            status.setTextColor(ContextCompat.getColor(ApplicationContext.getAppContext(), R.color.confirmed_fg));
            status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ApplicationContext.getAppContext(), R.color.confirmed_bg)));
        }

        if (text.equalsIgnoreCase("Tidak Tersedia") || text.equalsIgnoreCase("Rejected") || text.equalsIgnoreCase("Canceled")) {
            status.setTextColor(ContextCompat.getColor(ApplicationContext.getAppContext(), R.color.rejected_fg));
            status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ApplicationContext.getAppContext(), R.color.rejected_bg)));
        }
    }
}
