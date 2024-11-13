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
import java.text.SimpleDateFormat;

public class FragmentInstitute extends Fragment {
    private String endPoint = "/institutes";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private TextView namaInstitusi, deskripsiInstitusi, notelpLembaga, emailLembaga, nomorFax, websiteLembaga, status, jenisLembaga, NoVin, thnBerdiri, namaPimpinan, kepemilikan, noSotk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_institute, container, false);

        ImageView backArrow = view.findViewById(R.id.imageArrow);
        backArrow.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToDashboard();
        });

        namaInstitusi = view.findViewById(R.id.nama_institusi);
        deskripsiInstitusi = view.findViewById(R.id.deskripsiInstitusi);
        notelpLembaga = view.findViewById(R.id.notelpLembaga);
        emailLembaga = view.findViewById(R.id.emailLembaga);
        nomorFax = view.findViewById(R.id.nomorFax);
        websiteLembaga = view.findViewById(R.id.website);
        status = view.findViewById(R.id.status_beroperasi);
        jenisLembaga = view.findViewById(R.id.tipe);
        NoVin = view.findViewById(R.id.no_vin);
        thnBerdiri = view.findViewById(R.id.tahun_berdiri);
        namaPimpinan = view.findViewById(R.id.nama_pimpinan);
        kepemilikan = view.findViewById(R.id.kepemilikan);
        noSotk = view.findViewById(R.id.no_sotk);

        fetchData();

        return view;
    }

    public void fetchData() {
        // Get token dari shared preference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "Token Tidak ditemukan");
        ApiClient apiClient = new ApiClient();

        // get data
        apiClient.fetchInstitusi(endPoint, token, new ApiClient.InstituteHelper() {
            @Override
            public void onSuccess(String[] data) {
                requireActivity().runOnUiThread(() -> {
                    // Set teks dari data JSON
                    namaInstitusi.setText(data[1]);
                    deskripsiInstitusi.setText(data[12]);
                    notelpLembaga.setText(data[6]);
                    emailLembaga.setText(data[5]);
                    nomorFax.setText(data[10]);
                    websiteLembaga.setText(data[11]);
                    jenisLembaga.setText(data[9]);
                    NoVin.setText(data[7]);
                    thnBerdiri.setText(data[13]);
                    namaPimpinan.setText(data[2]);
                    kepemilikan.setText(data[3]);
                    noSotk.setText(data[8]);
                    status.setText(data[4]);
                });
            }

            @Override
            public void onFailed(IOException e) {
                Log.d("Failed", "onFailed: " + e.getMessage());
            }
        });
    }
}