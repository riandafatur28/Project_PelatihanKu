package com.example.projectpelatihanku;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentNotifikasiBelumDibaca extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifikasi_belum_dibaca, container, false);

        ListView listViewNotifikasiBelumDibaca = view.findViewById(R.id.listViewNotifikasiBelumDibaca);

        // Example unread notifications data
        String[] unreadNotifications = {
                "Unread Notification 1",
                "Unread Notification 2",
                "Unread Notification 3"
        };

        // Create an ArrayAdapter to display the unread notifications in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, unreadNotifications);
        listViewNotifikasiBelumDibaca.setAdapter(adapter);

        return view;
    }
}
