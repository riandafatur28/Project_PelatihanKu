package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;

public class FragmentDetailProgram extends Fragment {
    private String endPoint = "/institutes";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private TextView namaProgram, deskripsiProgram, namaKejuruan, standar, peserta, gedung, idInstructor, namaInstructor, alamatInstructor, kontakInstructor, status, tglPendaftaran;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_program, container, false);

        ImageView backArrow = view.findViewById(R.id.imageArrow);
        backArrow.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToDashboard();
        });

        namaProgram = view.findViewById(R.id.namaProgram);
        deskripsiProgram = view.findViewById(R.id.deskripsiProgram);
        namaKejuruan = view.findViewById(R.id.namaKejuruan);
        standar = view.findViewById(R.id.standar);
        peserta = view.findViewById(R.id.peserta);
        gedung = view.findViewById(R.id.gedung);
        status = view.findViewById(R.id.statusPendaftaran);
        idInstructor = view.findViewById(R.id.idInstructor);
        namaInstructor = view.findViewById(R.id.namaInstructor);
        kontakInstructor = view.findViewById(R.id.kontakInstructor);
        alamatInstructor = view.findViewById(R.id.alamatInstructor);
        tglPendaftaran = view.findViewById(R.id.tanggalPendaftaran);
  

        fetchData();

        return view;
    }

    private void fetchData() {
    }
}
