package com.example.gamequeue.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.utils.CardOneID;

public class ViewHolders {
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
            // Logic maybe?
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
