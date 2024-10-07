package com.example.projectpelatihanku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<MyNotification> {
    private final List<MyNotification> notifications; // Daftar notifikasi
    private final Context context; // Context untuk akses sumber daya
    private final OnNotificationCheckedChangeListener listener; // Menambahkan listener

    // Constructor untuk adapter
    public NotificationAdapter(@NonNull Context context, List<MyNotification> notifications, OnNotificationCheckedChangeListener listener) {
        super(context, 0, notifications);
        this.context = context;
        this.notifications = notifications;
        this.listener = listener; // Menginisialisasi listener
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Memastikan view yang ada atau membuat view baru
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_with_indicator, parent, false);
        }

        // Mendapatkan referensi elemen dari layout item
        TextView notificationText = convertView.findViewById(R.id.notificationText);
        View indicator = convertView.findViewById(R.id.indicator);
        CheckBox checkBoxHapus = convertView.findViewById(R.id.checkBox);

        // Mendapatkan notifikasi untuk posisi saat ini
        MyNotification notification = notifications.get(position);
        notificationText.setText(notification.getTitle());

        // Mengatur warna indikator berdasarkan status
        if (notification.isRead()) {
            indicator.setBackground(ContextCompat.getDrawable(context, R.drawable.indicator_read));
        } else {
            indicator.setBackground(ContextCompat.getDrawable(context, R.drawable.indicator_unread));
        }

        // Mengatur status CheckBox
        checkBoxHapus.setChecked(notification.isChecked());

        // Menghindari pemanggilan listener ketika adapter diperbarui
        checkBoxHapus.setOnCheckedChangeListener(null); // Hapus listener sebelumnya
        checkBoxHapus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notification.setChecked(isChecked); // Menyimpan status checkbox ke dalam objek notifikasi
            if (listener != null) {
                listener.onNotificationCheckedChange(); // Panggil metode pada listener
            }
        });

        return convertView; // Kembalikan tampilan yang telah disiapkan
    }
}