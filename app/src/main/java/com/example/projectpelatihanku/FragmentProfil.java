package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentProfil extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        // Mengambil referensi ke tombol keluar
        LinearLayout buttonKeluar = view.findViewById(R.id.buttonKeluar);
        buttonKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke Fragment Dashboard
                Fragment fragmentDashboard = new DashboardFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction(); // Menggunakan getParentFragmentManager

                // Mengganti fragment utama dengan DashboardFragment
                transaction.replace(R.id.containerProfil, fragmentDashboard); // Pastikan ini adalah ID container yang sesuai
                transaction.addToBackStack(null); // Menghapus back stack jika tidak ingin kembali

                // Menyelesaikan semua fragment di belakang
                getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                transaction.commit();
            }
        });

        return view;
    }
}