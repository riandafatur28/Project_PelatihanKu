package com.example.projectpelatihanku.api;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.projectpelatihanku.MainActivity;
import com.example.projectpelatihanku.Models.Notification;
import com.example.projectpelatihanku.R;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service yang menangani koneksi WebSocket untuk menerima notifikasi.
 * WebSocketService bertanggung jawab untuk menjaga koneksi WebSocket tetap terbuka dan
 * menampilkan notifikasi untuk pesan baru yang diterima.
 */
public class WebSocketService extends Service {
    private static final String CHANNEL_ID = "WebSocketServiceChannel";
    private ApiClient apiClient;
    private String token;
    private int userId;

    private final Set<String> receivedNotificationIds = new HashSet<>();

    /**
     * Dipanggil saat service pertama kali dibuat. Menyiapkan saluran notifikasi dan menginisialisasi ApiClient.
     *
     * @see ApiClient
     */
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        apiClient = new ApiClient();
    }

    /**
     * Dipanggil saat service dimulai. Menginisialisasi WebSocket dan menampilkan notifikasi foreground.
     *
     * @param intent  Intent yang digunakan untuk menginisialisasi service
     * @param flags   Flag tambahan untuk konfigurasi service
     * @param startId ID yang diberikan oleh sistem untuk service yang dimulai
     * @return status dari service, menggunakan START_STICKY agar service tetap berjalan meskipun aplikasi ditutup
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        token = intent.getStringExtra("token");
        userId = intent.getIntExtra("userId", -1);

        connectWebSocket();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Pelatihanku Apps")
                .setContentText("Logout dari aplikasi untuk menghentikan layanan ini")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent);

        startForeground(1, notification.build());
        return START_STICKY;
    }

    /**
     * Menyambungkan ke server WebSocket untuk menerima notifikasi real-time.
     * Menggunakan ApiClient untuk berkomunikasi dengan server dan menerima data.
     *
     * @see ApiClient#fetchNotification(String, int, String, ApiClient.WebSocketCallback)
     */
    private void connectWebSocket() {
        apiClient.fetchNotification(token, userId, "ws://192.168.100.4:8000/notifications",
                new ApiClient.WebSocketCallback() {
                    /**
                     * Dipanggil ketika ada pesan baru yang diterima melalui WebSocket.
                     * Menampilkan notifikasi untuk setiap pesan baru yang belum diterima sebelumnya.
                     *
                     * @param data Daftar notifikasi yang diterima
                     */
                    @Override
                    public void onMessageReceived(List<Notification> data) {
                        for (Notification notification : data) {
                            if (!receivedNotificationIds.contains(notification.getId())) {
                                receivedNotificationIds.add(notification.getId());
                                showPushNotification(notification);
                            }
                        }
                    }

                    /**
                     * Dipanggil jika terjadi kesalahan saat menghubungkan atau menerima data melalui WebSocket.
                     *
                     * @param e Kesalahan yang terjadi
                     */
                    @Override
                    public void onFailure(IOException e) {
                        Log.e("WebSocketService", "WebSocket Error: " + e.getMessage());
                    }
                });
    }

    /**
     * Menampilkan notifikasi push kepada pengguna.
     * Notifikasi akan ditampilkan di status bar dan dapat diklik untuk membuka MainActivity.
     *
     * @param notification Notifikasi yang diterima untuk ditampilkan
     */
    private void showPushNotification(Notification notification) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Notifikasi Baru!")
                .setContentText(notification.getMessage())
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(createPendingIntentForNotification(notification));

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int notificationId = notification.getId().hashCode();
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    /**
     * Membuat PendingIntent untuk membuka MainActivity saat notifikasi diklik.
     *
     * @param notification Notifikasi yang diterima untuk mengaitkan dengan PendingIntent
     * @return PendingIntent untuk membuka MainActivity
     */
    private PendingIntent createPendingIntentForNotification(Notification notification) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notificationId", notification.getId());
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    /**
     * Membuat channel notifikasi untuk API 26 dan yang lebih baru.
     * Digunakan untuk mengelompokkan notifikasi yang berkaitan dengan service.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "WebSocket Service Channel",
                    NotificationManager.IMPORTANCE_MIN
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * Dipanggil saat service dihentikan. Untuk memastikan WebSocket ditutup.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (apiClient != null) {
            apiClient.disconnectWebSocket();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
