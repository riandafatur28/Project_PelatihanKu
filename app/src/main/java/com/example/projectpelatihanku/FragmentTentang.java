package com.example.projectpelatihanku;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTentang extends Fragment {

    public FragmentTentang() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tentang, container, false);

        // Menangani klik pada tombol panah kembali
        ImageView backArrow = view.findViewById(R.id.imageArrow);
        backArrow.setOnClickListener(v -> {
            // Navigasi kembali ke DashboardFragment
            ((MainActivity) requireActivity()).navigateToDashboard();
        });

        return view;
    }
}
