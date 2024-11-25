package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.FragmentHelper;

import java.io.IOException;

public class FragmentOTP extends Fragment {

    private TextView textKirimUlang;
    private Button sendButton, btnBack;
    private EditText[] otpFields;
    private String token;

    /**
     * Contructor default untuk fragment OTP
     * Gunakan contructor ini jika tidak ingin mengirimkan parameter
     */
    public FragmentOTP() {

    }

    /**
     * Contructor untuk fragment OTP
     * Wajib digunakan jika untuk fungsional reset password
     *
     * @param token token yang digunakan untuk request verify kode OTP
     */
    public FragmentOTP(String token) {
        this.token = token;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        sendButton = view.findViewById(R.id.button_kirim);
        textKirimUlang = view.findViewById(R.id.kirimUlang);

        otpFields = new EditText[]{
                view.findViewById(R.id.kodeOtp1),
                view.findViewById(R.id.kodeOtp2),
                view.findViewById(R.id.kodeOtp3),
                view.findViewById(R.id.kodeOtp4),
                view.findViewById(R.id.kodeOtp5),
                view.findViewById(R.id.kodeOtp6)
        };

        backHandler(view);

        sendButton.setOnClickListener(v -> {
            verifyOtp(new ApiClient());
        });


        textKirimUlang.setOnClickListener(v -> {
            resendOtp(new ApiClient());
        });

        setOTPInputListeners();

        return view;
    }

    /**
     * Mengatur listener untuk setiap field OTP untuk berpindah ke field berikutnya atau
     * sebelumnya jika field kosong
     */
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

    /**
     * Mengambil input OTP dari setiap field dan mengembalikannya sebagai (string) kode OTP
     *
     * @return kode OTP yang diambil dari setiap field
     */
    private String getInputOTP() {
        StringBuilder otpBuilder = new StringBuilder();
        for (EditText otpField : otpFields) {
            otpBuilder.append(otpField.getText().toString().trim());
        }
        return otpBuilder.toString();
    }

    /**
     * Mengirimkan request ke API untuk melakukan verifikasi OTP
     *
     * @param apiClient Service Class untuk mengirim request ke server
     * @see  ApiClient#verifyOtp(String, String, ApiClient.OtpVerificationHelper)
     * @see FragmentResetPassword#FragmentResetPassword(String)
     */
    private void verifyOtp(ApiClient apiClient) {
        String otp = getInputOTP();

        if (otp.isEmpty()) {
            Toast.makeText(getActivity(), "Kode OTP tidak boleh kosong.", Toast.LENGTH_LONG).show();
            return;
        }

        apiClient.verifyOtp(otp, token, new ApiClient.OtpVerificationHelper() {
            @Override
            public void onSuccess(String token) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Verifikasi berhasil", Toast.LENGTH_LONG).show();
                        FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity,
                                new FragmentResetPassword(token), true, null);
                    });
                }
            }

            @Override
            public void onFailed(IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }
        });
    }

    /**
     * Mengirimkan request ke API untuk melakukan resend kode OTP
     *
     * @param apiClient Service Class untuk mengirim request ke server
     * @see ApiClient#resendOtp(String, ApiClient.ResendOtpHelper)
     */
    private void resendOtp(ApiClient apiClient) {

        apiClient.resendOtp(token, new ApiClient.ResendOtpHelper() {
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
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }
        });
    }

    /**
     * Handler untuk tombol kembali
     *
     * @param view View tempat tombol kembali berada
     * @see FragmentHelper#backNavigation(FragmentActivity, ImageView, Button, String, int, boolean)
     */
    private void backHandler(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        FragmentHelper.backNavigation(getActivity(), null, btnBack, null, 0, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.hideBottomNavigationView();
    }
}
