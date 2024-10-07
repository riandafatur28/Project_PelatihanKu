package com.example.projectpelatihanku;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentNotifikasi extends Fragment implements OnNotificationCheckedChangeListener {

    private NotificationAdapter adapter; // Menambahkan referensi adapter
    private List<MyNotification> notifications; // Menyimpan daftar notifikasi
    private Button buttonHapus; // Tombol untuk menghapus notifikasi

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);

        ListView listViewNotifikasi = view.findViewById(R.id.listViewNotifikasi);
        buttonHapus = view.findViewById(R.id.buttonHapus);

        // Membuat daftar notifikasi menggunakan objek Notification
        notifications = new ArrayList<>();
        notifications.add(new MyNotification("Pendaftaran telah dibuka!", false)); // belum dibaca
        notifications.add(new MyNotification("Jadwal kelas telah diperbarui.", false)); // sudah dibaca
        notifications.add(new MyNotification("Event baru akan segera berlangsung.", false));
        notifications.add(new MyNotification("Pengumuman hasil seleksi.", false)); // sudah dibaca
        notifications.add(new MyNotification("Sistem akan diperbarui malam ini.", false));

        // Inisialisasi adapter dan set ke ListView
        adapter = new NotificationAdapter(requireContext(), notifications, this); // Meneruskan listener
        listViewNotifikasi.setAdapter(adapter);

        // Set OnItemClickListener untuk membuka FragmentDetailNotifikasi
        listViewNotifikasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyNotification selectedNotification = notifications.get(position);
                selectedNotification.markAsRead(); // Menandai sebagai sudah dibaca

                // Memperbarui tampilan adapter
                adapter.notifyDataSetChanged(); // Memanggil notifyDataSetChanged() untuk memperbarui tampilan

                String notificationDetail = "Detail untuk notifikasi: " + selectedNotification.getTitle();

                // Buat FragmentDetailNotifikasi dan kirimkan data
                FragmentDetailNotifikasi detailFragment = FragmentDetailNotifikasi.newInstance(selectedNotification.getTitle(), notificationDetail);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.layerNotifikasi, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Tombol Hapus Notifikasi
        buttonHapus.setOnClickListener(v -> deleteSelectedNotifications());

        // Update visibility of the delete button based on checkbox state
        updateDeleteButtonVisibility();

        return view;
    }

    @Override
    public void onNotificationCheckedChange() {
        updateDeleteButtonVisibility(); // Memperbarui visibilitas tombol hapus
    }

    private void deleteSelectedNotifications() {
        // Menghapus notifikasi yang tercentang
        Iterator<MyNotification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            MyNotification notification = iterator.next();
            if (notification.isChecked()) {
                iterator.remove(); // Hapus notifikasi dari daftar
            }
        }
        adapter.notifyDataSetChanged(); // Memperbarui tampilan adapter

        // Perbarui visibilitas tombol hapus setelah penghapusan
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
        if (anyChecked) {
            buttonHapus.setVisibility(View.VISIBLE);
            buttonHapus.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            buttonHapus.setVisibility(View.GONE);
        }
    }

}
