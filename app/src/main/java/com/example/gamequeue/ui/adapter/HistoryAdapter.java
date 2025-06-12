package com.example.gamequeue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamequeue.R;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.utils.CardLayoutConst;
import com.example.gamequeue.utils.CardOneID;
import com.example.gamequeue.utils.DateConverter;
import com.example.gamequeue.utils.WidgetModifier;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryAdapter extends RecyclerView.Adapter<ViewHolders.HistoryViewHolder> {
    // Variables
    private final Context context;
    private final HashMap<String, ArrayList<ReservationModel>> historyListMap;
    private final ArrayList<String> sortedKeys;

    public HistoryAdapter(Context context, HashMap<String, ArrayList<ReservationModel>> historyListMap, ArrayList<String> sortedKeys) {
        this.context = context;
        this.historyListMap = historyListMap;
        this.sortedKeys = sortedKeys;
    }


    @NonNull
    @Override
    public ViewHolders.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.container_history, parent, false);
        return new ViewHolders.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders.HistoryViewHolder holder, int position) {
        String date = sortedKeys.get(position);
        ArrayList<ReservationModel> reservations = historyListMap.get(date);

        holder.historyDate.setText(date);
        holder.innerCardContainer.removeAllViews();

        for (ReservationModel reservation : reservations) {
            View card = LayoutInflater.from(context).inflate(CardLayoutConst.LAYOUT_ONE, holder.innerCardContainer, false);
            TextView cardTitle = card.findViewById(CardOneID.title);
            TextView cardStatus = card.findViewById(CardOneID.status);
            TextView cardDate = card.findViewById(CardOneID.date);
            TextView cardTime = card.findViewById(CardOneID.time);

            cardTitle.setText(reservation.getConsoleName());
            cardStatus.setText(reservation.getStatus());
            WidgetModifier.statusChanger(cardStatus);
            cardDate.setText(DateConverter.convertDateToIndonesianLocale(reservation.getDate(), 2));
            cardTime.setText(reservation.getTime());

            holder.innerCardContainer.addView(card);
        }
    }

    @Override
    public int getItemCount() {
        return historyListMap.size();
    }
}
