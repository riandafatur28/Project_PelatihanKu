package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectpelatihanku.api.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentProgram extends Fragment {

    private String endPoint;
    private RecyclerView recyclerView;
    private ProgramAdapter adapter;
    private List<Program> programList = new ArrayList<>();

    // Constructor untuk FragmentProgram, endpoint di-set sesuai dengan departmentId
    public FragmentProgram(String departmentId) {
        this.endPoint = "/departments/" + departmentId + "/programs";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout untuk fragment ini
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        // Ambil nama institusi dari Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String namaInstitusi = bundle.getString("namaInstitusi");

            // Tampilkan teks "Program" dan nama institusi di UI (misalnya di TextView)
            TextView textNamaInstitusi = view.findViewById(R.id.texttentang);

            // Menambahkan teks "Program" sebelum nama institusi
            String displayText = "Program di Kejuruan " + namaInstitusi;
            textNamaInstitusi.setText(displayText);
        }

        // Sembunyikan BottomNavigationView saat FragmentProgram aktif
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomview);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        // Inisialisasi RecyclerView dan Adapter
        recyclerView = view.findViewById(R.id.recyclerViewProgramInstitusi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProgramAdapter(programList, getContext());
        recyclerView.setAdapter(adapter);

        // Tombol panah untuk kembali ke FragmentDepartment
        ImageView imageArrow2 = view.findViewById(R.id.imageArrow2);
        imageArrow2.setOnClickListener(v -> navigateToDepartment());

        // Panggil API untuk mendapatkan data program
        fetchData();

        return view;
    }

    // Navigasi kembali ke FragmentDepartment
    private void navigateToDepartment() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            // Tampilkan kembali BottomNavigationView
            BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomview);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }

            FragmentDepartment fragmentDepartment = new FragmentDepartment();
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.layoutprogram, fragmentDepartment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    // Ambil data program dari API
    public void fetchData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "Token Tidak ditemukan");

        ApiClient apiClient = new ApiClient();
        apiClient.fetchProgram(token, endPoint, new ApiClient.ProgramHelper() {
            @Override
            public void onSuccess(ArrayList<Program> data) {
                requireActivity().runOnUiThread(() -> {
                    Log.d("FragmentProgram", "Jumlah data diterima: " + data.size());
                    if (data != null && !data.isEmpty()) {
                        programList.clear();
                        programList.addAll(data);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("FragmentProgram", "Data kosong atau tidak ditemukan.");
                    }
                });
            }

            @Override
            public void onFailed(IOException e) {
                Log.d("FragmentProgram", "Gagal mengambil data: " + e.getMessage());
            }
        });
    }
}
