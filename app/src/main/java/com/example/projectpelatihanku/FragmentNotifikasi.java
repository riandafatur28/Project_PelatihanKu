package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.projectpelatihanku.Models.Notification;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentNotifikasi extends Fragment {
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private List<Notification> notifications = new ArrayList<>();
    private ApiClient apiClient;
    private static String token;

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
        connectWebSocket();

        return view;
    }

    /**
     * Digunakan untuk melakukan koneksi ke websocket
     *
     * @see ApiClient#fetchNotification(String, int, String, ApiClient.WebSocketCallback)
     */
    private void connectWebSocket() {
        JWT jwt = new JWT(token);
        Double userIdDouble = (Double) jwt.getClaim("users").asObject(Map.class).get("id");
        int userId = userIdDouble.intValue();
        apiClient = new ApiClient();
        apiClient.fetchNotification(token, userId, "ws://192.168.100.4:8000/notifications",
                new ApiClient.WebSocketCallback() {
                    @Override
                    public void onMessageReceived(List<Notification> data) {
                        requireActivity().runOnUiThread(() -> {
                            for (Notification newNotification : data) {
                                boolean isDuplicate = false;
                                for (Notification existingNotification : notifications) {
                                    if (existingNotification.getId().equals(newNotification.getId())) {
                                        isDuplicate = true;
                                        break;
                                    }
                                }
                                if (!isDuplicate) {
                                    notifications.add(0, newNotification);
                                }
                            }
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
     * @see ApiClient#isReadNotification(String, String, ApiClient.notificationUpdateHelper)
     */
    public static void updateIsRead(Notification notification, ApiClient apiClient, Context context) {
        String nofiticationId = notification.getId();
        String endPoint = "/notifications/is-read/" + nofiticationId;
        apiClient.isReadNotification(token, endPoint, new ApiClient.notificationUpdateHelper() {
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
     * @see ApiClient#isDeletedNotification(String, String, ApiClient.notificationUpdateHelper)
     */
    public static void updateIsDeleted(Notification notification, ApiClient apiClient,
                                       Context context) {
        String nofiticationId = notification.getId();
        String endPoint = "/notifications/is-deleted/" + nofiticationId;
        apiClient.isDeletedNotification(token, endPoint, new ApiClient.notificationUpdateHelper() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (apiClient != null) {
            apiClient.disconnectWebSocket();
        }
    }
}
