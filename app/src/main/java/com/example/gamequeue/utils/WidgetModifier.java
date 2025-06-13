package com.example.gamequeue.utils;

import android.content.res.ColorStateList;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.gamequeue.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public static String convertDateToIndonesianLocale(String date, int style) {
        LocalDate parsedDate = null;

        // Parse Date from String to LocalDate object
        try {
            parsedDate = LocalDate.parse(date);
        } catch (Exception e) {
            return null;
        }

        // Convert LocalDate to Indonesian Locale
        if (parsedDate != null) {
            Locale indonesianLocale = new Locale("in", "ID");
            DateTimeFormatter displayFormatter = null;

            if (style == 1) {
                displayFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", indonesianLocale);
            } else if (style == 2) {
                displayFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", indonesianLocale);
            }

            if (displayFormatter == null) return null;

            return parsedDate.format(displayFormatter);
        }

        return null;
    }
}
