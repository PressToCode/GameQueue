package com.example.gamequeue.data.repository;

import androidx.annotation.NonNull;

import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.utils.CustomCallbackWithString;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DatabaseRepository {
    // Firebase Utilities Singletons Alias
    private static final FirebaseUser user = FirebaseUtil.getAuth().getCurrentUser();
    private static final DatabaseReference profileRef = FirebaseUtil.getProfilesRef();

    // Firebase Username from Database is Async
    public static void getFirebaseDatabaseUserName(CustomCallbackWithString callback) {
         profileRef.child(user.getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dbName = snapshot.getValue(String.class);

                if(dbName != null || !dbName.isEmpty()) {
                    callback.onSuccess(dbName);
                } else {
                    callback.onError("No Name found");
                }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
             }
         });
    }
}
