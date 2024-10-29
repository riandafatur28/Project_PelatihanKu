package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.projectpelatihanku.api.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentNotifikasi extends Fragment implements OnNotificationCheckedChangeListener {
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String KEY_NOTIFICATIONS = "cachedNotifications";
    private String endPoint = "notification/getNotification";
    private NotificationAdapter adapter;
    private List<MyNotification> notifications;
    private Button buttonHapus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);

        ListView listViewNotifikasi = view.findViewById(R.id.listViewNotifikasi);
        buttonHapus = view.findViewById(R.id.buttonHapus);

        // Inisialisasi daftar notifikasi dari cache
        notifications = loadNotificationsFromCache();
        if (notifications == null) {
            notifications = new ArrayList<>(); // Buat list kosong jika cache kosong
        }

        // Set adapter dengan data yang ada
        adapter = new NotificationAdapter(requireContext(), notifications, this);
        listViewNotifikasi.setAdapter(adapter);

        // Fetch data terbaru di latar belakang untuk memperbarui notifikasi
        fetchNotifications();

        listViewNotifikasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyNotification selectedNotification = notifications.get(position);
                selectedNotification.markAsRead(); // Tandai sebagai sudah dibaca

                adapter.notifyDataSetChanged(); // Perbarui tampilan adapter

                FragmentDetailNotifikasi detailFragment = FragmentDetailNotifikasi.newInstance(
                        selectedNotification.getTitle(),
                        "Detail untuk notifikasi: " + selectedNotification.getTitle()
                );
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.layerNotifikasi, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        buttonHapus.setOnClickListener(v -> deleteSelectedNotifications());
        updateDeleteButtonVisibility();

        return view;
    }

    private void fetchNotifications() {
        ApiClient api = new ApiClient();
        api.fetchData(new ApiClient.OnDataFetchedListener() {
            @Override
            public void onSuccess(List<MyNotification> fetchedNotifications) {
                requireActivity().runOnUiThread(() -> {
                    notifications.clear();
                    notifications.addAll(fetchedNotifications);
                    adapter.notifyDataSetChanged(); // Perbarui tampilan adapter
                    saveNotificationsToCache(fetchedNotifications); // Simpan data ke cache
                });
            }

            @Override
            public void onFailure(IOException e) {
                Log.e("Fetch Data Error", e.getMessage());
            }
        }, endPoint);
    }

    private void saveNotificationsToCache(List<MyNotification> notifications) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(notifications);
        editor.putString(KEY_NOTIFICATIONS, json);
        editor.apply();
    }

    private List<MyNotification> loadNotificationsFromCache() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_NOTIFICATIONS, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<MyNotification>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return null;
    }

    @Override
    public void onNotificationCheckedChange() {
        updateDeleteButtonVisibility();
    }

    private void deleteSelectedNotifications() {
        Iterator<MyNotification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            MyNotification notification = iterator.next();
            if (notification.isChecked()) {
                iterator.remove();
            }
        }
        adapter.notifyDataSetChanged();
        updateDeleteButtonVisibility();
    }

    private void updateDeleteButtonVisibility() {
        boolean anyChecked = false;
        for (MyNotification notification : notifications) {
            if (notification.isChecked()) {
                anyChecked = true;
                break;
            }
        }
        buttonHapus.setVisibility(anyChecked ? View.VISIBLE : View.GONE);
    }
}
