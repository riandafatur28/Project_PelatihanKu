package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.api.ApiClient;

import java.io.IOException;
import java.util.Map;

public class FragmentProfil extends Fragment {

    // Deklarasi variabel untuk komponen UI
    private TextView namaUser, namaProfil, jkProfil, ttlProfil, emailProfil, noTelpProfil, alamatProfil;
    private ImageView imageProfil;
    private String userId;

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
        // Menyiapkan tombol untuk memperbarui profil
        Button buttonPerbaruiProfil = view.findViewById(R.id.buttonPerbaruiProfil);
        buttonPerbaruiProfil.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToEditProfil();
            }
        });

        loadUserData();
        // Menyiapkan tombol untuk kembali ke halaman login
        LinearLayout txtKembali = view.findViewById(R.id.txtKembali);
        txtKembali.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });

        return view; // Mengembalikan tampilan fragment
    }

    private void loadUserData() {
        ApiClient api = new ApiClient();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "Token Tidak ditemukan");
        JWT jwt = new JWT(token);
        userId = jwt.getClaim("users").asObject(Map.class).get("id").toString();
        String endPoint = "/users/" + userId;
        api.FetchProfile(userId, token, endPoint, new ApiClient.ProfileHelper() {
            @Override
            public void onSuccess(String name, String jk, String ttl, String tlp, String email, String alamat) {
                namaUser.setText(name);
                namaProfil.setText(name);
                jkProfil.setText(jk);
                ttlProfil.setText(ttl);
                noTelpProfil.setText(tlp);
                emailProfil.setText(email);
                alamatProfil.setText(alamat);
            }

            @Override
            public void onFailed(IOException e) {
                Log.d("Failed", "onFailed: " + e.getMessage());
            }
        });
    }
}
