package com.example.projectpelatihanku;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentSemuaNotifikasi extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_semua_notifikasi, container, false);

        ListView listViewSemuaNotifikasi = view.findViewById(R.id.listViewSemuaNotifikasi);

        // Example notifications data
        String[] notifications = {
                "Notification 1",
                "Notification 2",
                "Notification 3",
                "Notification 4",
                "Notification 5"
        };

        // Create an ArrayAdapter to display the notifications in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, notifications);
        listViewSemuaNotifikasi.setAdapter(adapter);

        return view;
    }
}
