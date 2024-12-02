package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.Adapter.NotificationAdapter;
import com.example.projectpelatihanku.Models.Notification;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.JwtHelper;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentNotifikasi extends Fragment {
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private List<Notification> notifications = new ArrayList<>();
    private static String token;
    private static int userId;
    private BroadcastReceiver notificationReceiver;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewNotifikasi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificationAdapter(getContext(), notifications);
        recyclerView.setAdapter(adapter);
        token = SharedPreferencesHelper.getToken(getContext());
        JWT jwt = new JWT(token);
        Double userIdDouble = 0.0;
        try {
            userIdDouble = Double.parseDouble((String) jwt.getClaim("users").asObject(Map.class).get("id"));
        } catch (Exception e) {
            String id = JwtHelper.getUserData(SharedPreferencesHelper.getToken(getContext()), "users", "id");
            userIdDouble = Double.parseDouble(id);
        }
        userId = userIdDouble.intValue();
        fetchNotifications(new ApiClient());

        receiverBroadcastHandler();

        return view;
    }

    private void fetchNotifications(ApiClient apiClient) {
        apiClient.fetchNotification(token, "/notification/" + userId, new ApiClient.NotificationsHelper() {
            @Override
            public void onMessageReceived(List<Notification> data) {
                requireActivity().runOnUiThread(() -> {
                    notifications.clear();
                    notifications.addAll(data);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Digunakan mengupdate status notifikasi is_read = 1 (true)
     *
     * @param notification Notifikasi yang akan diupdate
     * @param apiClient    Service class untuk melakukan request ke server
     * @param context      Context dari fragment yang memanggil method
     * @see ApiClient#isReadNotification(String, String, int, String, ApiClient.notificationUpdateHelper)
     */
    public static void updateIsRead(Notification notification, ApiClient apiClient, Context context) {
        String nofiticationId = notification.getId();
        String endPoint = "/notification/is-read";
        apiClient.isReadNotification(token, endPoint, userId, nofiticationId,
                new ApiClient.notificationUpdateHelper() {
            @Override
            public void onSuccess(String message) {
                ((FragmentActivity) context).runOnUiThread(() -> {
                    Log.d("Updated", "message: " + message);
                });
            }

            @Override
            public void onFailed(IOException e) {
                ((FragmentActivity) context).runOnUiThread(() -> {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Digunakan mengupdate status notifikasi is_deleted = 1 (true)
     *
     * @param notification Notifikasi yang akan diupdate
     * @param apiClient    Service class untuk melakukan request ke server
     * @param context      Context dari fragment yang memanggil method
     * @see ApiClient#isDeletedNotification(String, String, int, String, ApiClient.notificationUpdateHelper)
     */
    public static void updateIsDeleted(Notification notification, ApiClient apiClient,
                                       Context context) {
        String nofiticationId = notification.getId();
        String endPoint = "/notification/is-deleted";
        apiClient.isDeletedNotification(token, endPoint, userId, nofiticationId,
                new ApiClient.notificationUpdateHelper() {
            @Override
            public void onSuccess(String message) {
                ((FragmentActivity) context).runOnUiThread(() -> {
                    Log.d("Updated", "message: " + message);
                });
            }

            @Override
            public void onFailed(IOException e) {
                ((FragmentActivity) context).runOnUiThread(() -> {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void receiverBroadcastHandler(){
        notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String body = intent.getStringExtra("body");
                int id = intent.getIntExtra("id", 0);
                int isRead = intent.getIntExtra("is_read", 0);

                notifications.add(0, new Notification(String.valueOf(id), body, isRead));
                adapter.notifyItemInserted(0);
                recyclerView.smoothScrollToPosition(0);
            }
        };

        IntentFilter filter = new IntentFilter("NEW_NOTIFICATION");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getContext().registerReceiver(notificationReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getContext().registerReceiver(notificationReceiver, filter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (notificationReceiver != null) {
            getContext().unregisterReceiver(notificationReceiver);
        }
    }
}
