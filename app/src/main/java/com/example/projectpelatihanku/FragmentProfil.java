package com.example.projectpelatihanku;

import static com.example.projectpelatihanku.api.ApiClient.BASE_URL;

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
import com.bumptech.glide.Glide;
import com.example.projectpelatihanku.api.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentProfil extends Fragment implements ApiClient.OnDataFetchedListener {
    // Deklarasi variabel untuk komponen UI
    private TextView namaUser, namaProfil, jkProfil, ttlProfil, emailProfil, noTelpProfil, alamatProfil;
    private ImageView imageProfil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Mengatur tampilan fragment dari layout XML
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        // Inisialisasi komponen UI dengan elemen dari layout
        namaUser = view.findViewById(R.id.namaUser);
        namaProfil = view.findViewById(R.id.namaProfil);
        jkProfil = view.findViewById(R.id.JKProfil);
        ttlProfil = view.findViewById(R.id.TTLProfil);
        emailProfil = view.findViewById(R.id.emailProfil);
        noTelpProfil = view.findViewById(R.id.noTelpProfil);
        alamatProfil = view.findViewById(R.id.alamatProfil);
        imageProfil = view.findViewById(R.id.imageProfil);

        // Memuat data pengguna dari server setiap kali fragment dibuka
        loadUserData();

        // Menyiapkan tombol untuk memperbarui profil
        Button buttonPerbaruiProfil = view.findViewById(R.id.buttonPerbaruiProfil);
        buttonPerbaruiProfil.setOnClickListener(v -> {
            // Navigasi ke halaman edit profil jika di dalam MainActivity
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToEditProfil();
            }
        });

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
        String endPoint = "/login"; // Endpoint untuk mengambil data pengguna
        OkHttpClient client = new OkHttpClient();

        // Membuat permintaan untuk mengambil data pengguna
        Request request = new Request.Builder()
                .url(BASE_URL + endPoint) // Menggabungkan BASE_URL dengan endpoint
                .build();

        // Mengirim permintaan secara asinkron
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Network Error", "Error fetching user data: " + e.getMessage());
                onProfileFetchFailure("Gagal memuat data pengguna. Silakan coba lagi.");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("Response Data", responseData); // Log respons untuk debugging
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String nama = jsonObject.getString("username");
                        String jenisKelamin = jsonObject.getString("jenisKelamin");
                        String ttl = jsonObject.getString("ttl");
                        String email = jsonObject.getString("email");
                        String tlp = jsonObject.getString("tlp");
                        String alamat = jsonObject.getString("alamat");
                        String imageUriString = jsonObject.optString("image_uri", null);

                        // Panggil metode untuk meng-update UI
                        onProfileFetchSuccess(nama, email, ttl, tlp, jenisKelamin, alamat);

                        // Memuat gambar jika ada
                        if (imageUriString != null) {
                            requireActivity().runOnUiThread(() -> {
                                Glide.with(requireContext()).load(imageUriString).into(imageProfil);
                            });
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Error", "Error parsing JSON: " + e.getMessage());
                        onProfileFetchFailure("Error parsing data pengguna.");
                    }
                } else {
                    Log.e("Response Error", "Response code: " + response.code());
                    onProfileFetchFailure("Gagal mengambil data dari server.");
                }
            }
        });
    }

    @Override
    public void onProfileFetchSuccess(String name, String email, String ttl, String tlp, String jenisKelamin, String alamat) {
        requireActivity().runOnUiThread(() -> {
            namaUser.setText(name);
            namaProfil.setText(name);
            jkProfil.setText(jenisKelamin);
            ttlProfil.setText(ttl);
            emailProfil.setText(email);
            noTelpProfil.setText(tlp);
            alamatProfil.setText(alamat);
        });
    }

    @Override
    public void onProfileFetchFailure(String errorMessage) {
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNavigation();
        }
    }

    @Override
    public void onSuccess(List<MyNotification> notifications) {

    }

    @Override
    public void onFailure(IOException e) {

    }
}
