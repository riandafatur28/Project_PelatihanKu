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

    // Deklarasi variabel untuk komponen UI
    private TextView namaUser, namaProfil, jkProfil, ttlProfil, emailProfil, noTelpProfil, alamatProfil;
    private ImageView imageProfil;
    private String userId;
    public static String username;
    public static String gender;
    public static String birth;
    public static String phone;
    public static String userEmail;
    public static String address;

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
                getActivity().runOnUiThread(() -> {
                    // Mengubah format tanggal dari yyyy-MM-dd ke dd-MM-yyyy
                    String formattedDate = formatDate(ttl);

                    // Menampilkan data ke UI
                    namaUser.setText(name);
                    namaProfil.setText(name);
                    jkProfil.setText(jk);
                    ttlProfil.setText(formattedDate);  // Menampilkan tanggal yang telah diformat
                    noTelpProfil.setText(tlp);
                    emailProfil.setText(email);
                    alamatProfil.setText(alamat);

                    // Menyimpan data ke variabel statis
                    username = name;
                    phone = tlp;
                    gender = jk;
                    birth = ttl;
                    userEmail = email;
                    address = alamat;

                    // Menyimpan gender ke SharedPreferences
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("gender", jk);  // Menyimpan gender
                    editor.apply();  // Apply changes asynchronously

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

    // Memuat gambar profil berdasarkan kondisi
    private void loadProfileImage(String gender) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String imageUriString = sharedPreferences.getString("image_uri", null);

        if (imageUriString != null) {
            try {
                imageProfil.setImageURI(Uri.parse(imageUriString));  // Menampilkan gambar yang diunggah pengguna
            } catch (Exception e) {
                imageProfil.setImageResource(R.drawable.gambar_user);  // Gambar default jika ada kesalahan
                Log.e("DashboardFragment", "Gagal memuat gambar pengguna", e);
            }
        } else {
            Log.d("DashboardFragment", "Gender yang diambil: " + gender);

            // Menentukan gambar profil berdasarkan gender
            if ("Laki-laki".equalsIgnoreCase(gender)) {
                imageProfil.setImageResource(R.drawable.img_men);  // Gambar default untuk laki-laki
            } else if ("Perempuan".equalsIgnoreCase(gender)) {
                imageProfil.setImageResource(R.drawable.img_women);  // Gambar default untuk perempuan
            } else {
                imageProfil.setImageResource(R.drawable.gambar_user);  // Gambar default jika gender tidak diketahui
            }
        }
    }

    private void saveImageUriToPreferences(Uri imageUri) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image_uri", imageUri.toString()); // Simpan URI sebagai String
        editor.apply(); // Simpan perubahan secara asinkron
        Log.d("FragmentEditProfil", "Image URI saved to SharedPreferences: " + imageUri.toString());
    }



    // Metode untuk mengubah format tanggal
    private String formatDate(String ttl) {
        try {
            // Membuat SimpleDateFormat untuk format input (yyyy-MM-dd)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Membuat SimpleDateFormat untuk format output (dd-MM-yyyy)
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Mengubah String menjadi Date
            Date date = inputFormat.parse(ttl);

            // Mengubah Date menjadi String dengan format baru
            return outputFormat.format(date);

        } catch (ParseException e) {
            // Menangani error jika format tanggal tidak sesuai
            Log.e("DateFormatError", "Error parsing date: " + e.getMessage());
            return ttl;  // Jika gagal, kembalikan tanggal original
        }
    }
}
