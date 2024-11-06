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

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.api.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FragmentNotifikasi extends Fragment implements OnNotificationCheckedChangeListener {
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String KEY_NOTIFICATIONS = "cachedNotifications";
    private String endPoint = "/notifications";
    private NotificationAdapter adapter;
    private List<MyNotification> notifications = new ArrayList<>();
    private Button buttonHapus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);

        ListView listViewNotifikasi = view.findViewById(R.id.listViewNotifikasi);
        buttonHapus = view.findViewById(R.id.buttonHapus);

        // Set adapter dengan data yang ada
        adapter = new NotificationAdapter(requireContext(), notifications, this);
        listViewNotifikasi.setAdapter(adapter);

        // Fetch data terbaru di latar belakang untuk memperbarui notifikasi

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

        fetchData();

        return view;
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

    public void fetchData() {
        // Get token dari shared preference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "Token Tidak ditemukan");
        ApiClient apiClient = new ApiClient();

        // get data
        apiClient.fetchNotifikasi(token, endPoint, new ApiClient.NotifikasiHelper() {

            @Override
            public void onSuccess(ArrayList<MyNotification> data) {
                requireActivity().runOnUiThread(() -> {
                    notifications.clear();
                    notifications.addAll(data);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailed(IOException e) {

            }
        });
    }
}
