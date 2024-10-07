package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.Timestamp;
import java.util.Calendar;

public class FragmentTentang extends Fragment {

    private FirebaseFirestore db;

    public FragmentTentang() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tentang, container, false);

        db = FirebaseFirestore.getInstance();

        TextView textViewLeader = view.findViewById(R.id.text_line2_info);
        TextView textViewVin = view.findViewById(R.id.text_noVin);
        TextView textViewYearEstablished = view.findViewById(R.id.text_line4_info);
        TextView textViewStatus = view.findViewById(R.id.text_line5_info);
        TextView textViewLine1 = view.findViewById(R.id.text_line1_info);
        TextView textViewTentangLembaga = view.findViewById(R.id.textTentangLembaga);
        TextView textViewKepemilikan = view.findViewById(R.id.text_line1_jenisLembaga);
        TextView textViewTipe = view.findViewById(R.id.text_line2_jenisLembaga);
        TextView textViewNomorSOTK = view.findViewById(R.id.text_line3_jenisLembaga);

//        fetchDataFromFirestore(textViewLeader, textViewVin, textViewYearEstablished, textViewStatus, textViewLine1, textViewTentangLembaga,
//                textViewKepemilikan, textViewTipe, textViewNomorSOTK);

        ImageView backArrow = view.findViewById(R.id.imageArrow);
        backArrow.setOnClickListener(v -> {

            ((MainActivity) requireActivity()).navigateToDashboard();
        });

        return view;
    }

//    private void fetchDataFromFirestore(TextView textViewPimpinan, TextView textViewVin, TextView textViewYearEstablished,
//                                        TextView textViewStatus, TextView textViewLine1, TextView textViewTentangLembaga,
//                                        TextView textViewKepemilikan, TextView textViewTipe, TextView textViewNomorSOTK) {

//        db.collection("institute")
//                .document("tCFu4kf6uu3gMfZV3b8t")
//                .get(Source.CACHE) // Mengambil data dari cache untuk respons cepat
//                .addOnSuccessListener(documentSnapshot -> {
//                    // Log isi dokumen untuk debugging
//                    Log.d("Firestore", "Document data: " + documentSnapshot.getData());

//                    if (documentSnapshot.exists()) {
//                        // Ambil data dari Firestore
//                        String pimpinan = documentSnapshot.getString("pimpinan");
//                        Number nomorVin = (Number) documentSnapshot.get("nomor_vin");
//                        Timestamp tahunBerdiriTimestamp = documentSnapshot.getTimestamp("tahun_berdiri");
//                        String statusBeropeasi = documentSnapshot.getString("status_beroperasi");
//                        String institusi = documentSnapshot.getString("nama_institusi");
//                        String tentangLembaga = documentSnapshot.getString("tentang_lembaga");
//                        String kepemilikan = documentSnapshot.getString("kepemilikan");
//                        String tipe_institusi = documentSnapshot.getString("tipe_institusi");
//                        String no_sotk = documentSnapshot.getString("no_sotk");
//
//                        // Ubah nomorVin dan tahunBerdiri ke String jika tidak null
//                        String nomorVinStr = nomorVin != null ? nomorVin.toString() : "N/A";
//
//                        String tahunBerdiriStr;
//                        if (tahunBerdiriTimestamp != null) {
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.setTime(tahunBerdiriTimestamp.toDate());
//                            tahunBerdiriStr = String.valueOf(calendar.get(Calendar.YEAR));
//                        } else {
//                            tahunBerdiriStr = "N/A";
//                        }
//
//                        // Set data ke TextView
//                        textViewPimpinan.setText("Pimpinan     : " + pimpinan);
//                        textViewVin.setText("Nomor VIN  :  " + nomorVinStr);
//                        textViewYearEstablished.setText("Tahun Berdiri : " + tahunBerdiriStr);
//                        textViewStatus.setText("Status            : " + statusBeropeasi);
//                        textViewLine1.setText(institusi != null ? institusi : "UPT BLK Nganjuk");
//                        textViewTentangLembaga.setText(tentangLembaga != null ? tentangLembaga : "Berdirinya UPT Balai Latihan Kerja (BLK) Nganjuk pada tanggal 07 Juli 1983 dengan nama KLK (Kursus Latihan Kerja). Pada tahun 1985 berubah menjadi BLKIP LLK-UKM (Lokal Latihan Kerja Usaha Kecil, Menengah). Pada era otonomi daerah tahun 2000 menjadi BLKUKM Disnaker Provinsi Jawa Timur berdasarkan perda No. 35/2000. Berdasarkan Peraturan Gubernur No. 62/2018 berganti nama menjadi Unit Pelaksana Teknis Balai Latihan Kerja (BLK) Nganjuk Disnakertrans Provinsi Jawa Timur.");
//                        textViewKepemilikan.setText("Kepemilikan : " + (kepemilikan != null ? kepemilikan : "N/A"));
//                        textViewTipe.setText("Tipe : " + tipe_institusi);
//                        textViewNomorSOTK.setText("Nomor SOTK dan Tanda Daftar : " + no_sotk);
//                    } else {
//                        Log.d("Firestore", "Document does not exist!");
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.d("Firestore", "Error fetching document", e);
//                });
    }

