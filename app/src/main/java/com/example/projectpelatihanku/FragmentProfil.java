package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentProfil extends Fragment {
    private TextView namaUser, namaProfil, jkProfil, ttlProfil, emailProfil, noTelpProfil, alamatProfil;
    private ImageView imageProfil;
    private static final String PREFS_NAME = "UserPrefs";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        // Inisialisasi komponen UI
        namaUser = view.findViewById(R.id.namaUser);
        namaProfil = view.findViewById(R.id.namaProfil);
        jkProfil = view.findViewById(R.id.JKProfil);
        ttlProfil = view.findViewById(R.id.TTLProfil);
        emailProfil = view.findViewById(R.id.emailProfil);
        noTelpProfil = view.findViewById(R.id.noTelpProfil);
        alamatProfil = view.findViewById(R.id.alamatProfil);
        imageProfil = view.findViewById(R.id.imageProfil);

        // Set up tombol update profil
        Button buttonPerbaruiProfil = view.findViewById(R.id.buttonPerbaruiProfil);
        buttonPerbaruiProfil.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToEditProfil();
            }
        });

        // Set up tombol keluar
        LinearLayout txtKembali = view.findViewById(R.id.txtKembali);
        txtKembali.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });

        // Load data pengguna dari SharedPreferences
        loadUserData();
        return view;
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString("username", "Nama tidak tersedia");
        String gender = sharedPreferences.getString("gender", "Tidak diketahui");
        String tanggalLahir = sharedPreferences.getString("tanggalLahir", "Tanggal Lahir tidak tersedia");
        String email = sharedPreferences.getString("email", "Email tidak tersedia");
        String nomorTelepon = sharedPreferences.getString("nomorTelepon", "Nomor telepon tidak tersedia");
        String alamat = sharedPreferences.getString("alamat", "Alamat tidak tersedia");
        String imageUriString = sharedPreferences.getString("image_uri", null);

        namaUser.setText(nama);
        namaProfil.setText(nama);
        jkProfil.setText(gender);
        ttlProfil.setText(tanggalLahir);
        emailProfil.setText(email);
        noTelpProfil.setText(nomorTelepon);
        alamatProfil.setText(alamat);

        // Set gambar profil jika URI gambar ada
        if (imageUriString != null) {
            imageProfil.setImageURI(Uri.parse(imageUriString));
        } else {
            // Set gambar default berdasarkan jenis kelamin
            if ("Laki-laki".equals(gender)) {
                imageProfil.setImageResource(R.drawable.men);
            } else if ("Perempuan".equals(gender)) {
                imageProfil.setImageResource(R.drawable.women);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data profil saat fragment aktif kembali
        loadUserData();

        // Tampilkan kembali BottomNavigationView
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNavigation();
        }
    }

    // Fungsi untuk menyimpan URI gambar ke SharedPreferences dari fragment lain
    public void saveImageUriToPreferences(Uri imageUri) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image_uri", imageUri.toString());
        editor.apply();
    }
}
