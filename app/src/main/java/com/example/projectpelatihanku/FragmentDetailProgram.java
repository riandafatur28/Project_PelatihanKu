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

import androidx.fragment.app.Fragment;

import com.example.projectpelatihanku.api.ApiClient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FragmentDetailProgram extends Fragment {
    private TextView namaProgram, deskripsiProgram, namaKejuruan, standar, peserta, gedung;
    private TextView idInstructor, namaInstructor, kontakInstructor, alamatInstructor;
    private TextView status, tglPendaftaran;
    private String programId;
    private String departmentId;
    private URL url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_program, container, false);

        // Initialize UI components
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

        // Retrieve programId and departmentId from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("programPrefs", Context.MODE_PRIVATE);
        programId = sharedPreferences.getString("programId", null);
        departmentId = sharedPreferences.getString("departmentId", null);

        Log.d("FragmentDetailProgram", "programId: " + programId);
        Log.d("FragmentDetailProgram", "departmentId: " + departmentId);

        // Check if both programId and departmentId are available
        if (programId != null && departmentId != null) {
            fetchDetailProgram(departmentId, programId);
        } else {
            Log.e("FragmentDetailProgram", "Program ID or Department ID not found in SharedPreferences");
        }

        // Tombol "Lebih Banyak"
        ImageView imageView = view.findViewById(R.id.imageArrow3);
        imageView.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToProgram();
            }
        });

        return view;
    }

    private void fetchDetailProgram(String departmentId, String programId) {
        ApiClient api = new ApiClient();
        if (departmentId != null && programId != null) {
            String endPoint = "/api/v1/public/departments/" + departmentId + "/programs/" + programId;
            Log.d("Endpoint", "fetchDetailProgram: " + ApiClient.BASE_URL + ApiClient.BASE_URL_PUBLIC + endPoint);
            Request request = new Request.Builder().url(url).build();
            OkHttpClient client = new OkHttpClient();
            // Call the API to fetch program details
            api.fetchDetailProgram(departmentId, new ApiClient.DetailProgramHelper() {
                @Override
                public void onSuccess(ArrayList<DetailProgram> data) {
                    requireActivity().runOnUiThread(() -> {
                        if (isAdded() && data != null && !data.isEmpty()) {
                            DetailProgram detailProgram = data.get(0);

                            // Display program details on UI
                            namaProgram.setText(detailProgram.getNama());
                            deskripsiProgram.setText(detailProgram.getDeskripsi());
                            namaKejuruan.setText(detailProgram.getStandar());
                            standar.setText(detailProgram.getStandar());
                            peserta.setText(detailProgram.getPeserta());
                            gedung.setText(detailProgram.getIdBuilding());
                            status.setText(detailProgram.getStatusPendaftaran());
                            tglPendaftaran.setText(detailProgram.getTanggalMulai() + " - " + detailProgram.getTanggalAkhir());

                            // Display instructor details
                            namaInstructor.setText(detailProgram.getIdInstructor());
                            kontakInstructor.setText(detailProgram.getIdInstructor());
                            alamatInstructor.setText(detailProgram.getIdInstructor());
                        }
                    });
                }

                @Override
                public void onFailed(IOException e) {
                    Log.d("FragmentDetailProgram", "Failed to fetch data: " + e.getMessage());
                }
            }, programId);
        }
    }
}
