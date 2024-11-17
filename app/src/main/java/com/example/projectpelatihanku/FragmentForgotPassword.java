package com.example.projectpelatihanku;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.FragmentHelper;

import java.io.IOException;

public class FragmentForgotPassword extends Fragment {

    private Button buttonKirim;
    private Button btnBack;
    private EditText editTextEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        buttonKirim = view.findViewById(R.id.button_kirim);
        btnBack = view.findViewById(R.id.btnBack);
        editTextEmail = view.findViewById(R.id.inputEmail);

        buttonKirim.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (validateEmail(email)) {
                sendRequestOTP(new ApiClient(), email);
            }
        });

        backButtonHandler(view);

        return view;
    }

    /**
     * Mengirimkan request OTP ke server
     *
     * @param apiClient Service Class untuk mengirim request ke server
     * @param email     alamat email untuk kirim kode OTP
     * @see ApiClient#sendRequestOtp(String, ApiClient.RequestResetPassword)
     * @see FragmentHelper#navigateToFragment(FragmentActivity, int, Fragment, boolean, String)
     */
    private void sendRequestOTP(ApiClient apiClient, String email) {
        apiClient.sendRequestOtp(email, new ApiClient.RequestResetPassword() {
            @Override
            public void onSuccess(String tempToken) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Kode OTP berhasil dikirim, Cek email anda.",
                            Toast.LENGTH_LONG).show();
                    FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity,
                            new FragmentOTP(), true, null);
                });
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
     * @see FragmentHelper#backNavigation(FragmentActivity, ImageView, Button)
     */
    private void backButtonHandler(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        FragmentHelper.backNavigation(getActivity(), null, btnBack);
    }

    /**
     * Validasi email sebelum dikirim ke server
     * domain email yang diterima adalah @gmail.com
     *
     * @param email alamat email yang akan diperiksa
     */
    private Boolean validateEmail(String email) {
        if (email.isEmpty()) {
            Toast.makeText(getActivity(), "Email tidak boleh kosong", Toast.LENGTH_LONG).show();
            return false;
        } else if (!email.endsWith("@gmail.com")) {
            Toast.makeText(getActivity(), "Masukkan email yang valid dengan domain @gmail.com", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
