package com.example.gamequeue.ui.adapter;

import com.example.gamequeue.data.model.ConsoleSharedViewModel;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.ui.main.ReservationDetailActivity;
import com.example.gamequeue.ui.main.ReservationProcessActivity;
import com.example.gamequeue.utils.CardLayoutConst;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamequeue.data.model.ConsoleModel;

import java.util.ArrayList;

public class ConsoleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final int ChosenLayout;
    private final ArrayList<ConsoleModel> consoleList;
    private final ArrayList<ReservationModel> reservationList;
    private final ConsoleSharedViewModel consoleViewModel;

    public ConsoleAdapter(Context context, int ChosenLayout, ArrayList<ConsoleModel> consoleList, @Nullable ArrayList<ReservationModel> reservationList, ConsoleSharedViewModel consoleViewModel) {
        this.context = context;
        this.ChosenLayout = ChosenLayout;
        this.consoleList = consoleList;
        this.reservationList = reservationList;
        this.consoleViewModel = consoleViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(ChosenLayout, parent, false);
        if (ChosenLayout == CardLayoutConst.LAYOUT_THREE) {
            return new ViewHolders.ViewHolderThree(view);
        } else if (ChosenLayout == CardLayoutConst.LAYOUT_TWO) {
            return new ViewHolders.ViewHolderTwo(view);
        } else {
            return new ViewHolders.ViewHolderOne(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolders.ViewHolderOne) {
            ReservationModel currentReservation = reservationList.get(position);
            ConsoleModel currentConsole = consoleViewModel.getConsoleById(currentReservation.getConsoleId());

            // Bind
            if (currentConsole != null) {
                ((ViewHolders.ViewHolderOne) holder).bind(currentReservation, currentConsole);

                // Set click listener to status page
                if (currentReservation.getId() != null && currentReservation.getConsoleName() != null) {
                    Intent intent = new Intent(context, ReservationDetailActivity.class);
                    intent.putExtra("id", currentReservation.getId());
                    intent.putExtra("console_name", currentReservation.getConsoleName());
                    holder.itemView.setOnClickListener(v -> context.startActivity(intent));
                }
                return;
            }

            // Initial Bind
            ((ViewHolders.ViewHolderOne) holder).bind(currentReservation, null);
        } else if (holder instanceof ViewHolders.ViewHolderTwo) {
            ConsoleModel currentConsole = consoleList.get(position);
            ((ViewHolders.ViewHolderTwo) holder).bind(currentConsole);

            // Only currently not lended item can be clicked
            if(!currentConsole.getLendingStatus()) {
                Intent intent = getReservationProcessIntent(currentConsole);
                holder.itemView.setOnClickListener(v -> context.startActivity(intent));
            }
        } else if (holder instanceof ViewHolders.ViewHolderThree) {
            ReservationModel currentReservation = reservationList.get(position);
            ConsoleModel currentConsole = consoleViewModel.getConsoleById(currentReservation.getConsoleId());

            // Bind
            if (currentConsole != null) {
                ((ViewHolders.ViewHolderThree) holder).bind(currentReservation, currentConsole, context);
                return;
            }

            // Initial Bind
            ((ViewHolders.ViewHolderThree) holder).bind(currentReservation, null, context);
        }
    }

    @NonNull
    private Intent getReservationProcessIntent(ConsoleModel currentConsole) {
        Intent intent = new Intent(context, ReservationProcessActivity.class);
        intent.putExtra("id", currentConsole.getId());
        intent.putExtra("title", currentConsole.getTitle());
        intent.putExtra("status", currentConsole.getLendingStatus() ? "Tidak Tersedia" : "Tersedia");
        intent.putExtra("specificationOne", currentConsole.getSpecificationOne());
        intent.putExtra("specificationTwo", currentConsole.getSpecificationTwo());
        intent.putExtra("specificationThree", currentConsole.getSpecificationThree());
        return intent;
    }

    @Override
    public int getItemCount() {
        if (ChosenLayout == CardLayoutConst.LAYOUT_THREE || ChosenLayout == CardLayoutConst.LAYOUT_ONE) {
            return reservationList.size();
        } else if (ChosenLayout == CardLayoutConst.LAYOUT_TWO) {
            return consoleList.size();
        }

        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return ChosenLayout;
    }
}
