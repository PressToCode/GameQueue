package com.example.gamequeue.data.repository;

import androidx.annotation.NonNull;

import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.utils.CustomCallbackWithString;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/*
 * Important Note:
 * All database function is async
 * and should be treated as such
 * therefore, do not run on UI thread
 */
public class DatabaseRepository {
    // Firebase Utilities Singletons Alias
    private static final FirebaseAuth auth;
    private static final DatabaseReference gamesRef, reservationsRef;

    static {
        auth = FirebaseUtil.getAuth();
        gamesRef = FirebaseUtil.getGamesRef();
        reservationsRef = FirebaseUtil.getReservationsRef();
    }
}
