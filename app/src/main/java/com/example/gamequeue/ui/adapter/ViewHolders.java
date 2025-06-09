package com.example.gamequeue.ui.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.utils.CardOneID;
import com.example.gamequeue.utils.CardThreeID;
import com.example.gamequeue.utils.CardTwoID;

import java.util.Objects;

public class ViewHolders {

    private final static String confirmedBgColor = "#ACE66A";
    private final static String confirmedTxtColor = "#4CAF50";
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
            statusChanger(status);
            date.setText(consoleModel.getDate());
            time.setText(consoleModel.getTime());
        }
    }

    public static class ViewHolderTwo extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, status, specificationOne, specificationTwo, specificationThree;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(CardTwoID.image);
            title = itemView.findViewById(CardTwoID.title);
            status = itemView.findViewById(CardTwoID.status);
            specificationOne = itemView.findViewById(CardTwoID.specificationOne);
            specificationTwo = itemView.findViewById(CardTwoID.specificationTwo);
            specificationThree = itemView.findViewById(CardTwoID.specificationThree);
        }

        public void bind(ConsoleModel consoleModel) {
            if (consoleModel == null) return;

//            imageView.setImageResource(consoleModel.getImage());
            title.setText(consoleModel.getTitle());
            status.setText(consoleModel.getStatus());
            statusChanger(status);
            specificationOne.setText(consoleModel.getSpecificationOne());
            specificationTwo.setText(consoleModel.getSpecificationTwo());
            specificationThree.setText(consoleModel.getSpecificationThree());
        }
    }

    public static class ViewHolderThree extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, status, date, time;
        Button detailBtn, removeBtn;

        public ViewHolderThree(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(CardThreeID.image);
            title = itemView.findViewById(CardThreeID.title);
            status = itemView.findViewById(CardThreeID.status);
            date = itemView.findViewById(CardThreeID.date);
            time = itemView.findViewById(CardThreeID.time);
            detailBtn = itemView.findViewById(CardThreeID.detailBtn);
            removeBtn = itemView.findViewById(CardThreeID.removeBtn);
        }

        public void bind(ConsoleModel consoleModel) {
            if (consoleModel == null) return;

//            imageView.setImageResource(consoleModel.getImage());
            title.setText(consoleModel.getTitle());
            status.setText(consoleModel.getStatus());
            statusChanger(status);
            date.setText(consoleModel.getDate());
            time.setText(consoleModel.getTime());
        }
    }

    public static void statusChanger(@NonNull TextView status) {
        if(Objects.equals(status.getText(), "Pending")) {
            status.setTextColor(Color.parseColor(pendingTxtColor));
            status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pendingBgColor)));
        }

        if (Objects.equals(status.getText(), "Confirmed")) {
            status.setTextColor(Color.parseColor(confirmedTxtColor));
            status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(confirmedBgColor)));
        }

        if (Objects.equals(status.getText(), "Rejected")) {
            status.setTextColor(Color.parseColor(rejectedTxtColor));
            status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(rejectedBgColor)));
        }
    }
}
