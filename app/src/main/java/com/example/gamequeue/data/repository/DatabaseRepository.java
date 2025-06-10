package com.example.gamequeue.data.repository;

import com.example.gamequeue.data.firebase.FirebaseUtil;
import com.example.gamequeue.data.model.ConsoleModel;
import com.example.gamequeue.data.model.RequestModel;
import com.example.gamequeue.data.model.ReservationModel;
import com.example.gamequeue.utils.CustomCallback;
import com.example.gamequeue.utils.CustomCallbackWithType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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
    private static final DatabaseReference consolesRef, reservationsRef, requestRef;

    static {
        auth = FirebaseUtil.getAuth();
        consolesRef = FirebaseUtil.getConsolesRef();
        reservationsRef = FirebaseUtil.getReservationsRef();
        requestRef = FirebaseUtil.getRequestRef();
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

    public static void submitForm(ReservationModel form) {
        String reservationId = reservationsRef.child(auth.getCurrentUser().getUid()).push().getKey();
        String userId = auth.getCurrentUser().getUid();
        String consoleId = form.getConsoleId();

        // Reservation is unique for each user
        reservationsRef.child(auth.getCurrentUser().getUid()).child(reservationId).setValue(form);

        // Update console to reserved status
        consolesRef.child(form.getConsoleId()).child("lendingStatus").setValue(true);
        consolesRef.child(form.getConsoleId()).child("lenderUid").setValue(userId);

        // Send request to admin
        requestRef.child(reservationId).setValue(new RequestModel(userId, reservationId));
    }

    public static void getUserReservations(CustomCallbackWithType<ArrayList<ReservationModel>> callback) {
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
}
