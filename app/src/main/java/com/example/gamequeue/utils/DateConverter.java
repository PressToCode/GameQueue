package com.example.gamequeue.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateConverter {
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
