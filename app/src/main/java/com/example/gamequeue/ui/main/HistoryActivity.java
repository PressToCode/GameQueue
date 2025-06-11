package com.example.gamequeue.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamequeue.R;
import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.ui.adapter.HistoryAdapter;
import com.example.gamequeue.utils.CustomCallbackWithType;
import com.example.gamequeue.utils.DateConverter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {
    // Variables
    private ImageButton backBtn;
    private RecyclerView historyList;
    private HistoryAdapter historyAdapter;
    private ValueEventListener historyListener;
    private DatabaseReference reservationRef;
    private Context context = this;
    private ArrayList<String> sortedIsoDateKeys;
    private HashMap<String, ArrayList<ReservationModel>> historyListMap;
    /*
     * Structure:
     * Map<String, ArrayList<ReservationModel>>
     *
     * Key: Date, Time (String)
     * Value: List of Items under that date (ArrayList)
     *
     * Database Structure:
     * Date: EEEE, dd MMMM yyyy (Day, date month year)
     * Convert it into dd MMMM yyyy
     *
     * Time: HH:mm (Hour:Minute)
     * Can just use directly
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        // Initialization
        backBtn = findViewById(R.id.historyBackButton);
        historyList = findViewById(R.id.historyList);
        historyList.setLayoutManager(new LinearLayoutManager(this));

        // Load Data
        sortedIsoDateKeys = new ArrayList<>();
        historyListMap = new HashMap<>();
        loadData();

        // Setup Adapter
        historyAdapter = new HistoryAdapter(context, historyListMap, sortedIsoDateKeys);
        historyList.setAdapter(historyAdapter);

        // Setup Listeners
        setupListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(historyListener != null) {
            reservationRef.removeEventListener(historyListener);
            historyListener = null;
        }
    }

    private void setupListener() {
        backBtn.setOnClickListener(v -> {
            if(historyListener != null) {
                reservationRef.removeEventListener(historyListener);
                historyListener = null;
            }
            finish();
        });
    }

    private void loadData() {
        reservationRef = FirebaseUtil.getReservationsRef();

        if (historyListener != null) {
            reservationRef.removeEventListener(historyListener);
        }

        historyListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyListMap.clear();

                if (!snapshot.exists()) {
                    if (historyAdapter != null) {
                        historyAdapter.notifyDataSetChanged();
                    }
                    return;
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReservationModel reservation = dataSnapshot.getValue(ReservationModel.class);

                    if (reservation != null) {
                        reservation.setId(dataSnapshot.getKey());
                        String dateString = reservation.getDate();

                        if (dateString == null || dateString.isEmpty()) {
                            return;
                        }

                        if (historyListMap.containsKey(dateString)) {
                            historyListMap.get(dateString).add(reservation);
                        } else {
                            ArrayList<ReservationModel> list = new ArrayList<>();
                            list.add(reservation);
                            historyListMap.put(dateString, list);
                        }
                    }
                }

                if (!historyListMap.isEmpty()) {
                    sortedIsoDateKeys.addAll(historyListMap.keySet());
                    Collections.sort(sortedIsoDateKeys, Collections.reverseOrder());

                    for (ArrayList<ReservationModel> list : historyListMap.values()) {
                        Collections.sort(list, (r1, r2) -> {
                            if (r1.getTime() == null || r2.getTime() == null) {
                                return r1.getTime().compareTo(r2.getTime());
                            }
                            return 0;
                        });
                    }
                }

                if (historyAdapter != null) {
                    historyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("[HistoryActivity]", "Error: " + error.getMessage());
                historyListMap.clear();
                sortedIsoDateKeys.clear();
                if (historyAdapter != null) {
                    historyAdapter.notifyDataSetChanged();
                }
            }
        };

        reservationRef.child(FirebaseUtil.getAuth().getCurrentUser().getUid()).addValueEventListener(historyListener);
    }
}