package com.example.gamequeue.ui.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.utils.CardOneID;

import java.util.Objects;

public class ViewHolders {
    private final static String pendingBgColor = "#FFF5CC";
    private final static String pendingTxtColor = "#CCA300";
    private final static String rejectedBgColor = "#FACCCC";
    private final static String rejectedTxtColor = "#E50000";
    public static class ViewHolderOne extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, status, date, time;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(CardOneID.image);
            title = itemView.findViewById(CardOneID.title);
            status = itemView.findViewById(CardOneID.status);
            date = itemView.findViewById(CardOneID.date);
            time = itemView.findViewById(CardOneID.time);
        }

        public void bind(ConsoleModel consoleModel) {
            if (consoleModel == null) return;

//            imageView.setImageResource(consoleModel.getImage());
            title.setText(consoleModel.getTitle());
            status.setText(consoleModel.getStatus());
            if(Objects.equals(consoleModel.getStatus(), "Pending")) {
                status.setTextColor(Color.parseColor(pendingTxtColor));
                status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pendingBgColor)));
            }

            if (Objects.equals(consoleModel.getStatus(), "Rejected")) {
                status.setTextColor(Color.parseColor(rejectedTxtColor));
                status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(rejectedBgColor)));
            }
            date.setText(consoleModel.getDate());
            time.setText(consoleModel.getTime());
        }
    }

    public static class ViewHolderTwo extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, status, date, time;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolderThree extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, status, date, time;

        public ViewHolderThree(@NonNull View itemView) {
            super(itemView);
        }
    }
}
