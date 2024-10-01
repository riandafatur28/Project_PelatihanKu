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
        View view = inflater.inflate(R.layout.fragment_semua_notifikasi, container, false);

        // Inisialisasi ListView
        ListView listView = view.findViewById(R.id.listViewSemuaNotifikasi);

        // Data dummy untuk notifikasi
        String[] notifikasi = {"Notifikasi 1", "Notifikasi 2", "Notifikasi 3", "Notifikasi 4"};

        // Menggunakan ArrayAdapter untuk menampilkan list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, notifikasi);
        listView.setAdapter(adapter);

        return view;
    }
}
