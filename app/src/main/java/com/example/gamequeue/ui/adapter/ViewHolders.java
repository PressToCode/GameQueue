package com.example.gamequeue.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.data.repository.DatabaseRepository;
import com.example.gamequeue.ui.main.ReservationDetailActivity;
import com.example.gamequeue.utils.ApplicationContext;
import com.example.gamequeue.utils.CardFourID;
import com.example.gamequeue.utils.CardOneID;
import com.example.gamequeue.utils.CardThreeID;
import com.example.gamequeue.utils.CardTwoID;
import com.example.gamequeue.utils.CustomCallback;
import com.example.gamequeue.utils.CustomCallbackWithType;
import com.example.gamequeue.utils.DateConverter;
import com.example.gamequeue.utils.WidgetModifier;

import java.util.Objects;

public class ViewHolders {
    // For Home Fragment
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

        public void bind(ReservationModel reservation, @Nullable ConsoleModel consoleModel) {
            if (reservation == null) return;

            if (consoleModel != null) {
                title.setText(consoleModel.getTitle());
                convertDate(reservation.getDate(), date);
                time.setText(reservation.getTime() + " WIB");
            } else {
                title.setText("...");
                date.setText("...");
                time.setText("...");
            }

            status.setText(reservation.getStatus());
            WidgetModifier.statusChanger(status);
        }
    }

    // For Reservation Fragment
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
            status.setText(consoleModel.getLendingStatus() ? "Tidak Tersedia" : "Tersedia");
            WidgetModifier.statusChanger(status);
            specificationOne.setText(consoleModel.getSpecificationOne());
            specificationTwo.setText(consoleModel.getSpecificationTwo());
            specificationThree.setText(consoleModel.getSpecificationThree());
        }
    }

    // For Status Fragment
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

        public void bind(ReservationModel reservation, ConsoleModel consoleModel, Context context) {
            if (reservation == null) return;

            if (consoleModel != null) {
                title.setText(consoleModel.getTitle());
                convertDate(reservation.getDate(), date);
                time.setText(reservation.getTime() + " WIB");
            } else {
                title.setText("...");
                date.setText("...");
                time.setText("...");
            }

            status.setText(reservation.getStatus());
            WidgetModifier.statusChanger(status);

            detailBtn.setOnClickListener(v -> {
                Intent intent = new Intent(context, ReservationDetailActivity.class);
                intent.putExtra("id", reservation.getId());
                intent.putExtra("console_name", reservation.getConsoleName());
                context.startActivity(intent);
            });

            removeBtn.setOnClickListener(v -> {
                DatabaseRepository.removeUserReservationById(reservation.getId(), consoleModel.getId(), 0, new CustomCallback() {
                    @Override
                    public void onSuccess() {
                        // In theory, it should update the observer in status fragment
                        // And also change the status to "Canceled"
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

    public static class ViewHolderFour extends RecyclerView.ViewHolder {
        // Variables
        ImageView image;
        TextView consoleName, userName, status, email, phone;

        public ViewHolderFour(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(CardFourID.image);
            consoleName = itemView.findViewById(CardFourID.consoleName);
            userName = itemView.findViewById(CardFourID.userName);
            status = itemView.findViewById(CardFourID.status);
            email = itemView.findViewById(CardFourID.email);
            phone = itemView.findViewById(CardFourID.phone);
        }

        public void bind(ReservationModel reservation) {
            if (reservation == null) return;

            consoleName.setText(reservation.getConsoleName());
            userName.setText(reservation.getLenderName());
            status.setText(reservation.getStatus());
            email.setText(reservation.getLenderEmail());
            phone.setText(reservation.getLenderPhone());

            WidgetModifier.statusChanger(status);
        }
    }

    // For History Activity
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        // Variables
        TextView historyDate;
        LinearLayout innerCardContainer;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyDate = itemView.findViewById(R.id.historyDate);
            innerCardContainer = itemView.findViewById(R.id.innerCardContainer);
        }
    }

    private static void convertDate(String date, TextView dateWidget) {
        dateWidget.setText(DateConverter.convertDateToIndonesianLocale(date, 1));
    }
}
