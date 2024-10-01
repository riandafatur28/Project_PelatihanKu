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
        View view = inflater.inflate(R.layout.fragment_notifikasi_belum_dibaca, container, false);

        // Inisialisasi ListView
        ListView listView = view.findViewById(R.id.listViewNotifikasiBelumDibaca);

        // Data dummy untuk notifikasi belum dibaca
        String[] notifikasiBelumDibaca = {"Notifikasi Belum Dibaca 1", "Notifikasi Belum Dibaca 2"};

        // Menggunakan ArrayAdapter untuk menampilkan list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, notifikasiBelumDibaca);
        listView.setAdapter(adapter);

        return view;
    }
}
