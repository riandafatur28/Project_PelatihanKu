package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.projectpelatihanku.api.ApiClient;

import java.io.IOException;

public class FragmentForgotPassword extends Fragment {

    private Button buttonKirim;
    private LinearLayout txtKembali;
    private EditText editTextEmail;
    private ApiClient apiClient;
    private TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        apiClient = new ApiClient();
        tokenManager = new TokenManager(getActivity()); // Inisialisasi TokenManager

        buttonKirim = view.findViewById(R.id.button_kirim);
        txtKembali = view.findViewById(R.id.txtKembali);
        editTextEmail = view.findViewById(R.id.inputEmail);

        buttonKirim.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(getActivity(), "Email tidak boleh kosong.", Toast.LENGTH_LONG).show();
            } else if (!email.endsWith("@gmail.com")) {
                Toast.makeText(getActivity(), "Masukkan email yang valid dengan domain @gmail.com", Toast.LENGTH_LONG).show();
            } else {
                sendPasswordResetRequest(email);
            }
        });

        txtKembali.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });

        return view;
    }

    private void sendPasswordResetRequest(String email) {
        // Dapatkan token dan cek kadaluarsa
        String token = tokenManager.getToken();

        apiClient.requestPasswordReset(email, new ApiClient.PasswordResetHelper() {
            @Override
            public void onSuccess(String tempToken) {
                tokenManager.saveToken(tempToken); // Simpan token baru jika sukses

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Kode OTP dikirim, cek email Anda.", Toast.LENGTH_LONG).show();
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).navigateToOTP();
                        }
                    });
                }
            }

            @Override
            public void onFailed(IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Request gagal: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }
        });
    }
}
