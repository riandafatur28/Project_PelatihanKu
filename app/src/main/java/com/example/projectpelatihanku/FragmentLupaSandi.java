package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.widget.EditText;

public class FragmentLupaSandi extends Fragment {
    private Button buttonKirim;
    private LinearLayout txtKembali;

    // Simpan key untuk SharedPreferences
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "email"; // Kunci untuk email

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lupa_sandi, container, false);

        buttonKirim = view.findViewById(R.id.button_kirim);
        txtKembali = view.findViewById(R.id.txtKembali);

        // Set listener untuk Button Kirim
        buttonKirim.setOnClickListener(v -> {
            String email = ((EditText) view.findViewById(R.id.inputEmail)).getText().toString();
            // Kirim OTP ke email acak
            sendOtpToEmail(email);
            navigateToOtpFragment(); // Navigasi ke fragment OTP
        });

        // Set listener untuk TextView Kembali
        txtKembali.setOnClickListener(v -> {
            // Kembali ke Fragment Login
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            } else {
                System.out.println("MainActivity not found");
            }
        });

        return view;
    }

    private void sendOtpToEmail(String email) {
        // Logika untuk mengirim OTP ke email
        // Misalnya, menggunakan API atau server backend Anda
        Toast.makeText(getContext(), "OTP telah dikirim ke " + email, Toast.LENGTH_SHORT).show();
    }

    private void navigateToOtpFragment() {
        // Navigasi ke Fragment OTP
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FragmentOTP()) // Ganti dengan nama fragment OTP Anda
                .addToBackStack(null)
                .commit();
    }
}
