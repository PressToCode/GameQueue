package com.example.gamequeue.data.sharedViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.data.model.ConsoleModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Used to pass data between activity & fragments
public class ConsoleSharedViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ConsoleModel>> consoleListLive = new MutableLiveData<>();
    private final ArrayList<ConsoleModel> consoleList = new ArrayList<>();
    private final DatabaseReference consoleRef = FirebaseUtil.getConsolesRef();
    private ValueEventListener consoleEventListener;

    public ConsoleSharedViewModel() {
        attachDatabaseListener();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        detachDatabaseReadListener(); // Important for cleanup
    }

    public LiveData<ArrayList<ConsoleModel>> getConsoleListLive() { return consoleListLive; }

    private void attachDatabaseListener() {
        if (consoleEventListener == null) {
            consoleEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    consoleList.clear();

                    // Get all data under "consoles"
                    for (DataSnapshot consoleSnapshot : dataSnapshot.getChildren()) {
                        try {
                            ConsoleModel console = consoleSnapshot.getValue(ConsoleModel.class);
                            if (console != null) {
                                console.setId(consoleSnapshot.getKey());
                                consoleList.add(console);
                            }
                        } catch (Exception e) {
                            Log.d("[ConsoleSharedViewModel]", "onDataChange: " + e.getMessage());
                        }
                    }
                    // Post the updated list to LiveData.
                    // If you're on a background thread (which ValueEventListener callbacks can be),
                    // use postValue. If you're sure you're on the main thread, use setValue.
                    // For Firebase listeners, it's safer to use postValue as they might not always
                    // guarantee main thread execution for all scenarios, though typically they do.
                    consoleListLive.postValue(new ArrayList<>(consoleList)); // Post a new copy
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("[ConsoleSharedViewModel]", "onCancelled: " + databaseError.getMessage());
                }
            };

            // Add listener
            consoleRef.addValueEventListener(consoleEventListener);
        }
    }

    public void detachDatabaseReadListener() {
        if (consoleEventListener != null) {
            consoleRef.removeEventListener(consoleEventListener);
            consoleEventListener = null;
        }
    }

    public ConsoleModel getConsoleById(String id) {
        for (ConsoleModel console : consoleList) {
            if (console.getId().equals(id)) {
                return console;
            }
        }

        return null;
    }
}
