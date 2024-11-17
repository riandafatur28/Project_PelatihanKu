package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.Models.DetailProgram;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.GlideHelper;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Fragment untuk menampilkan detail program berdasarkan programId.
 * Noted : Persyaratan program dan foto instructor belum
 */
public class FragmentDetailProgram extends Fragment {
    private TextView namaProgram, deskripsiProgram, namaKejuruan, standar, peserta, namaGedung,
            idInstructor, namaInstructor, kontakInstructor, alamatInstructor, status, tglPendaftaran;
    private String programId;
    private String token;
    private ImageView programImage, instructorImage;

    /**
     * Default constructor
     * Gunakan Consturctor ini jika ingin membuat object jika tidak ingin mengirimkan parameter
     */
    public FragmentDetailProgram() {

    }

    /**
     * Constructor dengan programId
     * Gunakan Contructor ini jika ingin menampilkan detail program berdasarkan programId
     *
     * @param programId id program yang dipilih users
     */
    public FragmentDetailProgram(String programId) {
        this.programId = programId;
    }


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_program, container, false);

        namaProgram = view.findViewById(R.id.namaProgram);
        deskripsiProgram = view.findViewById(R.id.deskripsiProgram);
        namaKejuruan = view.findViewById(R.id.namaKejuruan);
        standar = view.findViewById(R.id.standar);
        peserta = view.findViewById(R.id.peserta);
        namaGedung = view.findViewById(R.id.gedung);
        status = view.findViewById(R.id.statusPendaftaran);
        idInstructor = view.findViewById(R.id.idInstructor);
        namaInstructor = view.findViewById(R.id.namaInstructor);
        kontakInstructor = view.findViewById(R.id.kontakInstructor);
        alamatInstructor = view.findViewById(R.id.alamatInstructor);
        tglPendaftaran = view.findViewById(R.id.tanggalPendaftaran);
        programImage = view.findViewById(R.id.imageDetailProgram);
        instructorImage = view.findViewById(R.id.imageInstructor);

        fetchDetailProgram(new ApiClient());
        backButtonHandler(view);

        return view;
    }

    /**
     * Metode untuk mengambil detail program berdasarkan programId
     *
     * @param apiClient Service Class untuk mengambil data dari API
     * @see ApiClient#fetchDetailProgram(String, String, ApiClient.DetailProgramHelper)
     * @see GlideHelper#loadImage(Context, ImageView, String)
     */
    private void fetchDetailProgram(ApiClient apiClient) {
        token = SharedPreferencesHelper.getToken(getContext());
        apiClient.fetchDetailProgram(token, "/programs/" + programId,
                new ApiClient.DetailProgramHelper() {
                    @Override
                    public void onSuccess(ArrayList<DetailProgram> data) {
                        if (isAdded() && getActivity() != null) {
                            requireActivity().runOnUiThread(() -> {
                                if (data != null && !data.isEmpty()) {
                                    DetailProgram detailProgram = data.get(0);

                                    // Detail Program
                                    namaProgram.setText(detailProgram.getNama());
                                    namaKejuruan.setText(detailProgram.getDepartmentName());
                                    standar.setText(detailProgram.getStandar());
                                    peserta.setText(detailProgram.getPeserta());
                                    namaGedung.setText(detailProgram.getBuildingName());
                                    deskripsiProgram.setText(detailProgram.getDeskripsi());
                                    status.setText("Status : " + detailProgram.getStatusPendaftaran());
                                    tglPendaftaran.setText(detailProgram.getTanggalMulai() + " - " + detailProgram.getTanggalAkhir());

                                    // Detail Instructor
                                    idInstructor.setText(detailProgram.getInstructorId());
                                    namaInstructor.setText(detailProgram.getInstructorName());
                                    kontakInstructor.setText(detailProgram.getInstructorContact());
                                    alamatInstructor.setText(detailProgram.getInstructorAddress());

                                    // Load Gambar
                                    GlideHelper.loadImage(getContext(), programImage,
                                            detailProgram.getProgramImageUri());
                                    GlideHelper.loadImage(getContext(), instructorImage,
                                            detailProgram.getInstructorImageUri());
                                }
                            });
                        }

                    }

                    @Override
                    public void onFailed(IOException e) {
                        if (isAdded() && getActivity() != null) {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
    }

    /**
     * Handler Metode untuk button kembali
     *
     * @see FragmentHelper#handleBackButton(FragmentActivity, ImageView)
     */
    private void backButtonHandler(View view) {
        ImageView backButton = view.findViewById(R.id.imageArrow3);
        FragmentHelper.handleBackButton(getActivity(), backButton);
    }
}
