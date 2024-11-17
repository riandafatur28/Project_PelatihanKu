package com.example.projectpelatihanku;

import static com.example.projectpelatihanku.DashboardFragment.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.api.ApiClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class FragmentProfil extends Fragment {

    // Deklarasi Variabel
    private TextView namaUser, namaProfil, jkProfil, ttlProfil, emailProfil, noTelpProfil, alamatProfil;
    private ImageView imageProfil;
    private final String endPoint = "/users/";
    private int userId;
    public static String username;
    public static String gender;
    public static String birth;
    public static String phone;
    public static String userEmail;
    public static String address;
    private String token;
    private SharedPreferences sharedPreferences;

    /**
     * @see #backButtonHandler(View)
     * @see #updateButtonHandler(View)
     *
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        namaUser = view.findViewById(R.id.namaUser);
        namaProfil = view.findViewById(R.id.namaProfil);
        jkProfil = view.findViewById(R.id.JKProfil);
        ttlProfil = view.findViewById(R.id.TTLProfil);
        emailProfil = view.findViewById(R.id.emailProfil);
        noTelpProfil = view.findViewById(R.id.noTelpProfil);
        alamatProfil = view.findViewById(R.id.alamatProfil);
        imageProfil = view.findViewById(R.id.imageProfil);

        // Panggil Metode untuk Handler Button dan load data profile
        loadUserData(new ApiClient());
        backButtonHandler(view);
        updateButtonHandler(view);
        return view;
    }

    /**
     * Ambil data profile user
     * @param api ApiClient untuk mengambil data dari server
     */
    private void loadUserData(ApiClient api) {
        sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "Token Tidak ditemukan");
        JWT jwt = new JWT(token);
        Double userIdDouble = (Double) jwt.getClaim("users").asObject(Map.class).get("id");
        userId = userIdDouble.intValue();

        api.FetchProfile(userId, token, endPoint + userId, new ApiClient.ProfileHelper() {
            @Override
            public void onSuccess(String name, String jk, String ttl, String tlp, String email, String alamat) {
                getActivity().runOnUiThread(() -> {
                    String tanggalLahir = formatDate(ttl);

                    // Menampilkan data ke UI
                    namaUser.setText(name);
                    namaProfil.setText(name);
                    jkProfil.setText(jk);
                    ttlProfil.setText(tanggalLahir);
                    noTelpProfil.setText(tlp);
                    emailProfil.setText(email);
                    alamatProfil.setText(alamat);

                    // Menyimpan value ke variable Statis
                    username = name;
                    phone = tlp;
                    gender = jk;
                    birth = ttl;
                    userEmail = email;
                    address = alamat;

                    // Menyimpan gender ke SharedPreferences
                    sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("gender", jk);
                    editor.apply();

                    // Log to confirm the data is saved
                    Log.d("DashboardFragment", "Gender saved: " + jk);

                    // Memuat gambar profil
                    loadProfileImage(jk);
                });
            }

            @Override
            public void onFailed(IOException e) {

            }
        });
    }

    /**
     * @param gender
     */
    private void loadProfileImage(String gender) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String imageUriString = sharedPreferences.getString("image_uri", null);

        if (imageUriString != null) {
            try {
                imageProfil.setImageURI(Uri.parse(imageUriString));
            } catch (Exception e) {
                imageProfil.setImageResource(R.drawable.gambar_user);
            }
        } else {
            setProfileByGender(gender);
        }
    }

    /**
     * Mengatur gambar profil berdasarkan gender.
     *
     * @param gender Genger users untuk set foto profile jika foto profile berlum diperbarui
     */
    private void setProfileByGender(String gender) {
        if ("Laki-laki".equalsIgnoreCase(gender)) {
            imageProfil.setImageResource(R.drawable.img_men);
        } else if ("Perempuan".equalsIgnoreCase(gender)) {
            imageProfil.setImageResource(R.drawable.img_women);
        } else {
            imageProfil.setImageResource(R.drawable.gambar_user);
        }
    }

    /**
     * Mengubah format tanggal dari yyyy-MM-dd ke dd-MM-yyyy.
     *
     * @param ttl tempat tanggal lahir yang akan diformat
     * @return ttl yang telah diformat atau tanggal original jika gagal.
     */
    private String formatDate(String ttl) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date date = inputFormat.parse(ttl);
            return outputFormat.format(date);

        } catch (ParseException e) {
            Toast.makeText(requireActivity(), "Gagal Parsing Tanggal", Toast.LENGTH_SHORT).show();
            return ttl;
        }
    }

    /**
     * Handler untuk tombol kembali.
     * @param view profile
     */
    private void backButtonHandler(View view){
        LinearLayout txtKembali = view.findViewById(R.id.txtKembali);
        txtKembali.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });
    }

    /**
     * Handler untuk tombol perbarui profil.
     * @param view profile
     */
    private void updateButtonHandler(View view){
        Button buttonPerbaruiProfil = view.findViewById(R.id.buttonPerbaruiProfil);
        buttonPerbaruiProfil.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToEditProfil();
            }
        });
    }
}
