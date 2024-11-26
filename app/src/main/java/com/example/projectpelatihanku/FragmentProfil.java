package com.example.projectpelatihanku;

import static com.example.projectpelatihanku.MainActivity.hideBottomNavigationView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.api.WebSocketService;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.GlideHelper;
import com.example.projectpelatihanku.helper.JwtHelper;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class FragmentProfil extends Fragment {

    private TextView namaUser, namaProfil, jkProfil, ttlProfil, emailProfil, noTelpProfil, alamatProfil;
    private ImageView imageProfil;
    private final String endPoint = "/users/";
    public static int userId;
    public static String username;
    public static String gender;
    public static String birth;
    public static String phone;
    public static String userEmail;
    public static String address;
    public static String imagePath;
    private String token;

    /**
     * @see #backButtonHandler(View)
     * @see #updateButtonHandler(View)
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

        loadUserData(new ApiClient());
        backButtonHandler(view);
        updateButtonHandler(view);
        return view;
    }

    /**
     * Ambil data profile user
     *
     * @param api Class Service untuk mengirim permintaan ke server
     * @see ApiClient#FetchProfile(String, String, ApiClient.ProfileHelper)
     * @see SharedPreferencesHelper#getToken(Context)
     * @see GlideHelper#loadImage(Context, ImageView, String)
     */
    private void loadUserData(ApiClient api) {
        token = SharedPreferencesHelper.getToken(getContext());
        JWT jwt = new JWT(token);
        Double userIdDouble = 0.0;
        try {
            userIdDouble = Double.parseDouble((String) jwt.getClaim("users").asObject(Map.class).get("id"));
        } catch (Exception e) {
            String id = JwtHelper.getUserData(SharedPreferencesHelper.getToken(getContext()), "users", "id");
            userIdDouble = Double.parseDouble(id);
        }
        int userId = userIdDouble.intValue();

        api.FetchProfile(token, endPoint + userId, new ApiClient.ProfileHelper() {
            @Override
            public void onSuccess(String name, String jk, String ttl, String tlp, String email, String alamat, String imageUri) {
                getActivity().runOnUiThread(() -> {
                    String tanggalLahir = formatDate(ttl);
                    namaUser.setText(name);
                    namaProfil.setText(name);
                    jkProfil.setText(jk);
                    ttlProfil.setText(tanggalLahir);
                    noTelpProfil.setText(tlp);
                    emailProfil.setText(email);
                    alamatProfil.setText(alamat);

                    username = name;
                    phone = tlp;
                    gender = jk;
                    birth = ttl;
                    userEmail = email;
                    address = alamat;
                    imagePath = imageUri;
                    GlideHelper.loadImage(getContext(), imageProfil, imageUri);

                });
            }

            @Override
            public void onFailed(IOException e) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
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
     * Handler untuk tombol logout dan navigasi ke fragment login.
     *
     * @param view profile
     * @see WebSocketService
     * @see SharedPreferencesHelper#clearToken(Context)
     * @see FragmentHelper#navigateToFragment(FragmentActivity, int, Fragment, boolean, String)
     * @see MainActivity#hideBottomNavigationView()
     */
    private void backButtonHandler(View view) {
        Button txtKembali = view.findViewById(R.id.btnBack);
        txtKembali.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(requireContext(), WebSocketService.class);
            requireContext().stopService(serviceIntent);
            SharedPreferencesHelper.clearToken(requireContext());
            FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity,
                    new FragmentLogin(), true, null);
            hideBottomNavigationView();
        });
    }

    /**
     * Handler untuk tombol perbarui profil.
     *
     * @param view profile
     * @see FragmentHelper#navigateToFragment(FragmentActivity, int, Fragment, boolean, String)
     */
    private void updateButtonHandler(View view) {
        Button buttonPerbaruiProfil = view.findViewById(R.id.buttonPerbaruiProfil);
        buttonPerbaruiProfil.setOnClickListener(v -> {
            FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity,
                    new FragmentEditProfil(), true, null);
            hideBottomNavigationView();
        });
    }
}
