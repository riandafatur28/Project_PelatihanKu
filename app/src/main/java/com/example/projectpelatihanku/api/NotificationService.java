package com.example.projectpelatihanku.api;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.projectpelatihanku.R;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreferencesHelper.saveString(getApplicationContext(), "notifications", "fcmToken", token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String id = remoteMessage.getData().get("id");
        String is_read = remoteMessage.getData().get("is_read");

        int idNotif = 0;
        int is_readNotif = 0;

        try {
            idNotif = Integer.parseInt(id);
            is_readNotif = Integer.parseInt(is_read);
        } catch (Exception e) {
            Log.d("Gagal", "Gagal saat parsing: " + e.getMessage());
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "notification";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "notification", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.anouncement_images);;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(idNotif, builder.build());

        Intent intent = new Intent("NEW_NOTIFICATION");
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("id", idNotif);
        intent.putExtra("is_read", is_readNotif);
        sendBroadcast(intent);

    }
}
