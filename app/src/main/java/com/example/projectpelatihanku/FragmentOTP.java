package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FragmentOTP extends Fragment {

    private TextView textKembali, timerText, textKirimUlang;
    private Button sendButton;
    private EditText[] otpFields;
    private final String generatedOTP = "123456";
    private CountDownTimer countDownTimer;
    private boolean isNavigating = false;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        sendButton = view.findViewById(R.id.button_kirim);
        textKembali = view.findViewById(R.id.text_Kembali);
        timerText = view.findViewById(R.id.timer_text);
        textKirimUlang = view.findViewById(R.id.kirimUlang);

        otpFields = new EditText[]{
                view.findViewById(R.id.kodeOtp1),
                view.findViewById(R.id.kodeOtp2),
                view.findViewById(R.id.kodeOtp3),
                view.findViewById(R.id.kodeOtp4),
                view.findViewById(R.id.kodeOtp5),
                view.findViewById(R.id.kodeOtp6)
        };

        for (EditText otpField : otpFields) {
            otpField.setText("");
            otpField.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        }

        sendButton.setOnClickListener(v -> {
            String inputOTP = getInputOTP();

            // Validasi input OTP
            if (inputOTP.isEmpty()) {
                showToastWithDelay("OTP tidak boleh kosong.", 1000);
            } else if (inputOTP.length() < 6) {
                showToastWithDelay("OTP harus terdiri dari 6 digit.", 1000);
            } else if (!validateOTP(inputOTP)) {
                showToastWithDelay("OTP tidak valid. Silakan coba lagi.", 1000);
            } else {

                isNavigating = true;
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) requireActivity()).navigateToResetPassword();
                } else {
                    Log.e("FragmentOTP", "MainActivity not found");
                }
            }
        });

        textKembali.setOnClickListener(v -> {
            isNavigating = true;
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToForgotPassword();
            } else {
                Log.e("FragmentOTP", "MainActivity not found");
            }
        });

        startTimer();

        setOTPInputListeners();

        textKirimUlang.setOnClickListener(v -> {
            showToastWithDelay("Kode OTP telah dikirim ulang ke email.", 1000); // Tampilkan toast dengan delay
            startTimer();
        });

        return view;
    }

    private String getInputOTP() {
        StringBuilder otpBuilder = new StringBuilder();
        for (EditText otpField : otpFields) {
            otpBuilder.append(otpField.getText().toString().trim());
        }
        return otpBuilder.toString();
    }

    private boolean validateOTP(String inputOTP) {
        return inputOTP.equals(generatedOTP);
    }

    private void setOTPInputListeners() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        otpFields[index - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                }
            });
        }
    }

    private void showToastWithDelay(String message, long delayMillis) {
        if (!isNavigating) {
            new Handler().postDelayed(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show(), delayMillis);
        }
    }

    private void startTimer() {
        timerText.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("Sisa waktu: " + millisUntilFinished / 1000 + " detik");
            }

            @Override
            public void onFinish() {
                timerText.setText("Waktu habis!");
                showToastWithDelay("Waktu OTP habis. Kode OTP baru telah dikirim.", 1000);
                startTimer(); // Reset timer
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
