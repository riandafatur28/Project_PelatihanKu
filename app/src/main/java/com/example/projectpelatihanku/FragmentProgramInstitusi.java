package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView; // Pastikan untuk mengimpor TextView
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentProgramInstitusi extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout untuk fragment
        View view = inflater.inflate(R.layout.fragment_program_institusi, container, false); // Ganti dengan layout Anda

        // Inisialisasi ImageView untuk navigasi ke FragmentInstitusi
        ImageView imageArrow = view.findViewById(R.id.imageArrow2);
        imageArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menggunakan metode dari MainActivity untuk berpindah ke FragmentInstitusi
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.navigateToInstitusi(); // Memanggil metode navigasi ke FragmentInstitusi
                }
            }
        });

        // Inisialisasi TextView untuk navigasi ke FragmentDetailProgram
        TextView btnDetail = view.findViewById(R.id.btn_detail); // Pastikan ID ini sesuai dengan layout XML
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.navigateToDetailProgram(); // Memanggil metode untuk berpindah ke FragmentDetailProgram
                }
            }
        });

        return view;
    }
}