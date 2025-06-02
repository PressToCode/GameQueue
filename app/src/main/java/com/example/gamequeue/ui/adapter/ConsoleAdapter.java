package com.example.gamequeue.ui.adapter;

import com.example.gamequeue.utils.CardLayoutConst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamequeue.data.model.ConsoleModel;

import java.util.ArrayList;

public class ConsoleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final int ChosenLayout;
    private final ArrayList<ConsoleModel> consoleList;

    public ConsoleAdapter(Context context, int ChosenLayout, ArrayList<ConsoleModel> consoleList) {
        this.context = context;
        this.ChosenLayout = ChosenLayout;
        this.consoleList = consoleList;
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
        ConsoleModel currentItem = consoleList.get(position);
        if (holder instanceof ViewHolders.ViewHolderOne) {
            ((ViewHolders.ViewHolderOne) holder).bind(currentItem);
            // Logic
        } else if (holder instanceof ViewHolders.ViewHolderTwo) {
            ((ViewHolders.ViewHolderTwo) holder).bind(currentItem);
            // Logic
        } else if (holder instanceof ViewHolders.ViewHolderThree) {
            ((ViewHolders.ViewHolderThree) holder).bind(currentItem);
            // Logic
        }
    }

    @Override
    public int getItemCount() {
        return consoleList.size();
    }
}
