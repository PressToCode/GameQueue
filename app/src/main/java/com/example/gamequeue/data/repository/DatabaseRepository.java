package com.example.gamequeue.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.data.model.RequestModel;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.utils.ApplicationContext;
import com.example.gamequeue.utils.CustomCallback;
import com.example.gamequeue.utils.CustomCallbackWithType;
import com.example.gamequeue.utils.RandomGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
 * Important Note:
 * All database function is async
 * and should be treated as such
 * therefore, do not run on UI thread
 */
public class DatabaseRepository {
    // Firebase Utilities Singletons Alias
    private static final FirebaseAuth auth;
    private static final DatabaseReference consolesRef, reservationsRef, requestRef, adminsRef;

    static {
        auth = FirebaseUtil.getAuth();
        consolesRef = FirebaseUtil.getConsolesRef();
        reservationsRef = FirebaseUtil.getReservationsRef();
        requestRef = FirebaseUtil.getRequestRef();
        adminsRef = FirebaseUtil.getAdminsRef();
    }

    // This should NOT be called ANYWHERE unless NEEDED
    // Used to populate Firebase Realtime Database
    // For console data
    private static void addConsolesDataToFirebase(CustomCallback callback) {
        ArrayList<ConsoleModel> models = new ArrayList<>();
        models.add(new ConsoleModel("Playstation (1)", "PlayStation 5", "4k HDR Refresh Rate 120Hz", "Wireless Dualsense Controller"));
        models.add(new ConsoleModel("Playstation (2)", "PlayStation 5 Digital Edition", "1080p 144Hz Gaming Projector", "Wireless Dualsense Edge Controller"));
        models.add(new ConsoleModel("Playstation (3)", "PlayStation 4 Pro", "4K TV with HDR", "DualShock 4 Wireless Controller"));
        models.add(new ConsoleModel("Xbox (1)", "Xbox Series X", "4K OLED TV 120Hz VRR", "Xbox Wireless Controller (Carbon Black)"));
        models.add(new ConsoleModel("Xbox (2)", "Xbox Series S", "1440p QHD Monitor 120Hz", "Xbox Wireless Controller (Robot White)"));
        models.add(new ConsoleModel("PC (1)", "Alienware Aurora R15 Desktop", "34-inch Ultrawide QHD G-Sync Monitor", "Logitech G Pro X Superlight Mouse & Mechanical Keyboard"));
        models.add(new ConsoleModel("PC (2)", "Custom Built Gaming Rig (Ryzen 9, RTX 4080)", "Dual 27-inch 1440p IPS Monitors", "Razer Viper Ultimate Mouse & Huntsman Elite Keyboard"));
        models.add(new ConsoleModel("Nintendo Switch (1)", "Nintendo Switch OLED Model", "Built-in 7-inch OLED Screen / Docked to 1080p TV", "Joy-Con Controllers (Neon Red/Blue)"));
        models.add(new ConsoleModel("Nintendo Switch (2)", "Nintendo Switch Lite (Coral)", "Built-in 5.5-inch LCD Screen", "Integrated Controls"));
        models.add(new ConsoleModel("Steam Deck (1)", "Steam Deck 512GB NVMe", "Built-in 7-inch LCD / Docked to 1440p Monitor", "Integrated Controls / Xbox Wireless Controller via Bluetooth"));

        // Assuming there is 10 consoles or devices
        for (int i = 0; i < models.size(); i++) {
            models.get(i).setLending(false, "");
            String id = consolesRef.push().getKey();
            consolesRef.child(id).setValue(models.get(i));
        }

        callback.onSuccess();
    }

    // To reset All
    // SHOULD NOT BE USED IN NORMAL CIRCUMTANCES
    public static void resetAll(CustomCallback callback) {
        consolesRef.get().addOnCompleteListener(task -> {
           if (!task.isSuccessful()) {
               callback.onError(task.getException().getMessage());
               return;
           }

           task.getResult().getChildren().forEach(dataSnapshot -> {
               dataSnapshot.getRef().child("lendingStatus").setValue(false);
               dataSnapshot.getRef().child("lenderUid").setValue("");
           });
        });

        requestRef.removeValue().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onError(task.getException().getMessage());
                return;
            }
        });

        reservationsRef.removeValue().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onError(task.getException().getMessage());
                return;
            }

            callback.onSuccess();
        });
    }

    public static void submitForm(ReservationModel form, CustomCallbackWithType<String> callback) {
        String reservationId = reservationsRef.child(auth.getCurrentUser().getUid()).push().getKey();
        String userId = auth.getCurrentUser().getUid();
        String consoleId = form.getConsoleId();

        // Reservation is unique for each user
        if (reservationId == null) {
            callback.onError("reservation failed to generate");
            return;
        }

//        form.setVerificationCode(RandomGenerator.generateRandomString());
        form.setLenderEmail(auth.getCurrentUser().getEmail());
        form.setStatus("Pending");
        reservationsRef.child(auth.getCurrentUser().getUid()).child(reservationId).setValue(form);

        // Update console to reserved status
//        consolesRef.child(consoleId).child("lendingStatus").setValue(true);
//        consolesRef.child(consoleId).child("lenderUid").setValue(userId);

        // Send request to admin
        requestRef.child(reservationId).setValue(new RequestModel(userId, consoleId));
        callback.onSuccess(reservationId);
    }

    // For one time fetch, not recommended
    private static void getUserReservations(CustomCallbackWithType<ArrayList<ReservationModel>> callback) {
        reservationsRef.child(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onError(task.getException().getMessage());
                return;
            }

            ArrayList<ReservationModel> reservations = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                ReservationModel reservation = dataSnapshot.getValue(ReservationModel.class);
                reservations.add(reservation);
            });

            callback.onSuccess(reservations);
        });
    }

    public static void getUserReservationById(String id, @Nullable String userUid, CustomCallbackWithType<ReservationModel> callback) {
        String userId;

        if (userUid == null || userUid.isEmpty()) {
            userId = auth.getCurrentUser().getUid();
        } else {
            userId = userUid;
        }

        reservationsRef.child(userId).child(id).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onError(task.getException().getMessage());
                return;
            }

            ReservationModel reservation = task.getResult().getValue(ReservationModel.class);
            reservation.setId(task.getResult().getKey());
            callback.onSuccess(reservation);
        });
    }

    // type:
    // 0 = Canceled by User
    // 1 = Rejected by Admin
    public static void removeUserReservationById(String reservationId, String consoleId, int type, CustomCallback callback) {
        // Update Console FIRST
        consolesRef.child(consoleId).child("lendingStatus").setValue(false);
        consolesRef.child(consoleId).child("lenderUid").setValue("");

        // Remove Request
        requestRef.child(reservationId).removeValue();

        // Change Reservation Status
        reservationsRef.child(auth.getCurrentUser().getUid()).child(reservationId).child("status").setValue(type == 0 ? "Canceled" : "Rejected");
    }

    // There is two type
    // If approved, then the next thing is completed
    // If pending, then the next thing is cancelled
    public static void updateReservationStatus(ReservationModel reservation) {
        // Get current status
        String status = reservation.getStatus().toLowerCase();

        // Get reservation ID
        String reservationId = reservation.getId();

        // Update Status
        if (status.equals("pending")) {
            reservationsRef.child(auth.getCurrentUser().getUid()).child(reservationId).child("status").setValue("Canceled");
        } else if (status.equals("approved")) {
            reservationsRef.child(auth.getCurrentUser().getUid()).child(reservationId).child("status").setValue("Completed");
        }

        // Update Console to free up reservation
        consolesRef.child(reservationId).child("lendingStatus").setValue(false);
        consolesRef.child(reservationId).child("lenderUid").setValue("");
    }

    // Used to fetch ONCE in requestSharedViewModel
    // DO NOT USE ANYWHERE ELSE
    private static void getRequests(CustomCallbackWithType<ArrayList<RequestModel>> callback) {
        requestRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onError(task.getException().getMessage());
                return;
            }

            ArrayList<RequestModel> requests = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                RequestModel request = dataSnapshot.getValue(RequestModel.class);
                requests.add(request);
            });

            callback.onSuccess(requests);
        });
    }

    public static void checkAdmins(CustomCallback callback) {
        // Get list of admins and check if current user UID match
        adminsRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onError(task.getException().getMessage());
                return;
            }

            ArrayList<String> admins = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                admins.add(dataSnapshot.getKey());
            });

            if (admins.contains(auth.getCurrentUser().getUid())) {
                ApplicationContext.setAdminMode(true);
                callback.onSuccess();
                return;
            }

            ApplicationContext.setAdminMode(false);
            callback.onError("Not Admin");
        });
    }

    public static void updateReservationRequest(Boolean isAccepted, String reservationId, String userId, String consoleId) {
        if (isAccepted) {
            // Update Console
            consolesRef.child(consoleId).child("lendingStatus").setValue(true);
            consolesRef.child(consoleId).child("lenderUid").setValue(userId);

            // Update User's Reservation Status
            reservationsRef.child(userId).child(reservationId).child("status").setValue("Approved");
            reservationsRef.child(userId).child(reservationId).child("verificationCode").setValue(RandomGenerator.generateRandomString());

            // Lastly, Remove the Request
            requestRef.child(reservationId).removeValue();
        } else {
            // Update Console
            consolesRef.child(consoleId).child("lendingStatus").setValue(false);
            consolesRef.child(consoleId).child("lenderUid").setValue("");

            // Update User's Reservation Status
            reservationsRef.child(userId).child(reservationId).child("status").setValue("Rejected");

            // Lastly, Remove the Request
            requestRef.child(reservationId).removeValue();
        }
    }
}
