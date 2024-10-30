package com.example.projectpelatihanku;

import static com.example.projectpelatihanku.api.ApiClient.BASE_URL;

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

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        String userName = sharedPreferences.getString(KEY_USER_NAME, "User");  // "User" adalah default jika nama tidak ditemukan
        namaUser.setText(FragmentLogin.sayName);
        namaProfil.setText(FragmentLogin.sayName);
        jkProfil.setText(FragmentLogin.sayJenisKelamin);
        ttlProfil.setText(FragmentLogin.sayTtl);
        emailProfil.setText(FragmentLogin.sayEmail);
        noTelpProfil.setText(FragmentLogin.sayNoTelp);
        alamatProfil.setText(FragmentLogin.sayAlamat);

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
        String endPoint = "user/getUserData"; // Ganti dengan endpoint yang sesuai
        OkHttpClient client = new OkHttpClient();

        // Mengambil token dari SharedPreferences jika diperlukan
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null); // Misalnya token disimpan saat login

        Request request = new Request.Builder()
                .url(BASE_URL + endPoint) // BASE_URL didefinisikan di kelas ApiClient Anda
                .addHeader("Authorization", "Bearer " + token) // Menambahkan header untuk otentikasi jika diperlukan
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Network Error", "Error fetching user data: " + e.getMessage());
                // Tampilkan pesan kesalahan ke pengguna jika perlu
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String nama = jsonObject.getString("username");
                        String jenisKelamin = jsonObject.getString("jenisKelamin");
                        String ttl = jsonObject.getString("ttl");
                        String email = jsonObject.getString("email");
                        String tlp = jsonObject.getString("tlp");
                        String alamat = jsonObject.getString("alamat");
                        String imageUriString = jsonObject.optString("image_uri", null);

                        // Update UI di thread utama
                        requireActivity().runOnUiThread(() -> {
                            namaUser.setText(nama);
                            namaProfil.setText(nama);
                            jkProfil.setText(jenisKelamin);
                            ttlProfil.setText(ttl);
                            emailProfil.setText(email);
                            noTelpProfil.setText(tlp);
                            alamatProfil.setText(alamat);
                            // Jika imageUriString tidak null, set image view
                            if (imageUriString != null) {
                                // Contoh set image ke ImageView menggunakan Glide atau library lain
                                Glide.with(requireContext()).load(imageUriString).into(imageProfil);
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("JSON Error", "Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    Log.e("Response Error", "Response code: " + response.code());
                }
            }
        });
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
