package com.example.projectpelatihanku;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.projectpelatihanku.api.ApiClient;

import java.io.IOException;

public class FragmentOTP extends Fragment {

    private TextView textKembali, textKirimUlang;
    private Button sendButton;
    private EditText[] otpFields;
    private boolean isNavigating = false;

    private ApiClient apiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        apiClient = new ApiClient();

        sendButton = view.findViewById(R.id.button_kirim);
        textKembali = view.findViewById(R.id.text_Kembali);
        textKirimUlang = view.findViewById(R.id.kirimUlang);

        otpFields = new EditText[]{
                view.findViewById(R.id.kodeOtp1),
                view.findViewById(R.id.kodeOtp2),
                view.findViewById(R.id.kodeOtp3),
                view.findViewById(R.id.kodeOtp4),
                view.findViewById(R.id.kodeOtp5),
                view.findViewById(R.id.kodeOtp6)
        };

        // Mengatur tindakan saat tombol "Kirim" ditekan
        sendButton.setOnClickListener(v -> {
            verifyOtpWithApi(); // Memanggil metode untuk verifikasi OTP
        });

        // Mengatur tindakan saat tombol "Kembali" ditekan
        textKembali.setOnClickListener(v -> {
            isNavigating = true;
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToForgotPassword();
            } else {
                Log.e("FragmentOTP", "MainActivity not found");
            }
        });

        // Mengatur tindakan saat tombol "Kirim Ulang" ditekan
        textKirimUlang.setOnClickListener(v -> {
            resendOtp(); // Memanggil metode untuk mengirim ulang OTP
        });

        // Menambahkan TextWatcher pada setiap field OTP
        setOTPInputListeners();

        return view;
    }

    private void setOTPInputListeners() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Tidak perlu aksi sebelum teks berubah
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        // Pindah ke kolom berikutnya
                        otpFields[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        // Pindah ke kolom sebelumnya jika kosong
                        otpFields[index - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Tidak perlu aksi setelah teks berubah
                }
            });
        }
    }

    // Mengambil OTP dari input pengguna
    private String getInputOTP() {
        StringBuilder otpBuilder = new StringBuilder();
        for (EditText otpField : otpFields) {
            otpBuilder.append(otpField.getText().toString().trim());
        }
        return otpBuilder.toString();
    }

    // Metode untuk memverifikasi OTP dengan API
    private void verifyOtpWithApi() {
        // Mengambil token final dari TokenManager
        TokenManager tokenManager = new TokenManager(requireContext());
        String finalToken = tokenManager.getFinalToken();

        // Mengambil input OTP dari field
        String inputOTP = getInputOTP();

        if (finalToken.isEmpty()) {
            Toast.makeText(getActivity(), "Token tidak ditemukan. Silakan coba lagi.", Toast.LENGTH_LONG).show();
            return;
        }

        if (inputOTP.isEmpty()) {
            Toast.makeText(getActivity(), "OTP tidak boleh kosong.", Toast.LENGTH_LONG).show();
            return;
        }

        // Melakukan verifikasi OTP dengan ApiClient
        apiClient.verifyOtp(finalToken, inputOTP, new ApiClient.OtpVerificationHelper() {
            @Override
            public void onSuccess(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Verifikasi berhasil", Toast.LENGTH_LONG).show();
                        // Navigasi ke halaman reset password setelah verifikasi berhasil
                        ((MainActivity) requireActivity()).navigateToResetPassword();
                    });
                }
            }

            @Override
            public void onFailed(IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Verifikasi gagal: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }
        });
    }

    // Metode untuk mengirim ulang OTP dengan API
    private void resendOtp() {
        TokenManager tokenManager = new TokenManager(requireContext());
        String finalToken = tokenManager.getFinalToken();

        if (finalToken.isEmpty()) {
            Toast.makeText(getActivity(), "Token tidak ditemukan. Silakan coba lagi.", Toast.LENGTH_LONG).show();
            return;
        }

        apiClient.resendOtp(finalToken, new ApiClient.ResendOtpHelper() {
            @Override
            public void onSuccess(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show()
                    );
                }
            }

            @Override
            public void onFailed(IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Gagal mengirim ulang OTP: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }
        });
    }
}
