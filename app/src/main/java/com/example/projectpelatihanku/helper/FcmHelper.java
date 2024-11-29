package com.example.projectpelatihanku.helper;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Kelas helper untuk fungsional terkait firebase cloud messaging (FCM).
 */
public class FcmHelper {
    static String token = null;

    /**
     * Mengambil token FCM dari Firebase.
     * @return token FCM atau null jika gagal mengambil token.
     */
    public static String getToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        String messages = task.isSuccessful() ? "Berhasil mengambil token fcm" :
                                "Gagal mengambil token fcm";
                        Log.d("Messages", messages);
                    }
                    token = task.getResult();
                });
        return token;
    }

    /**
     * Menambahkan/subscribe pengguna ke topik "all_users" untuk menerima notifikasi yang
     * bersifat general dari aplikasi.
     */
    public static void subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("all_users")
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Gagal Subscribe Topic", task.getException());
                        return;
                    }
                    Log.d(TAG, "Berhasil Subscribe Topic");
                });
    }
}
