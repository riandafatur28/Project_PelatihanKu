package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.FragmentHelper;

import java.io.IOException;

public class FragmentResetPassword extends Fragment {

    private Button backButton, sendButton;
    private EditText newPasswordInput, confirmPasswordInput;
    private ImageView iconInputPassword, iconConfirmPassword;
    private String newPassword;
    private String rePassword;
    private String token;

    /**
     * Contructor default untuk fragment reset password.
     * Gunakan contructor ini jika tidak ingin mengirimkan parameter
     */
    public FragmentResetPassword() {
    }

    /**
     * Contructor untuk fragment reset password.
     * Wajib digunakan jika berkaitan dengan fungsionalitas reset password.
     *
     * @param token token yang didapatkan setelah verifikasi kode OTP
     */
    public FragmentResetPassword(String token) {
        this.token = token;
    }

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        newPasswordInput = view.findViewById(R.id.inputpassword);
        confirmPasswordInput = view.findViewById(R.id.konfirmSandiBaru);
        iconInputPassword = view.findViewById(R.id.iconinputpassword);
        iconConfirmPassword = view.findViewById(R.id.iconkonfirmpassword);

        sendButtonHandler(view);
        backButtonHandler(view);

        // Mengatur aksi klik pada ikon visibilitas untuk password baru
        iconInputPassword.setOnClickListener(v -> togglePasswordVisibility(newPasswordInput, iconInputPassword));
        // Mengatur aksi klik pada ikon visibilitas untuk konfirmasi password
        iconConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordInput, iconConfirmPassword));

        return view;
    }

    /**
     * Mengirimkan request ke API untuk melakukan reset password.
     * 
     * @param apiClient Service Class untuk mengirim request ke server
     * @see ApiClient#resetPassword(String, String, ApiClient.RequestResetPassword)
     */
    private void requestResetPassword(ApiClient apiClient) {
        apiClient.resetPassword(token, newPassword, new ApiClient.RequestResetPassword() {
            @Override
            public void onSuccess(String message) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity,
                            new FragmentConfirmPassword(), true, null);
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
     * Mengatur visibilitas password berdasarkan icons yang diklik.
     *
     * @param passwordField EditText untuk password. Element yang akan diubah visibilitasnya.
     * @param toggleIcon    ImageView untuk ikon visibilitas.
     */
    private void togglePasswordVisibility(EditText passwordField, ImageView toggleIcon) {
        if (passwordField.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.vector_eye_close);
        } else {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.vector_eye_open);
        }
        // Memindahkan kursor ke akhir teks
        passwordField.setSelection(passwordField.getText().length());
    }

    /**
     * Validasi password baru dan konfirmasi password.
     *
     * @param newPassword password baru
     * @param rePassword  konfirmasi password
     * @return true jika password valid, dan new password dengan konfirmasi password cocok.
     * false jika syarat tidak terpenuhi.
     */
    private boolean validatePassword(String newPassword, String rePassword) {
        if (newPassword.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(getActivity(), "Password tidak boleh kosong.", Toast.LENGTH_LONG).show();
            return false;
        } else if (!newPassword.equals(rePassword)) {
            Toast.makeText(getActivity(), "Password tidak cocok.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Handler untuk tombol kirim.
     * @see FragmentResetPassword#requestResetPassword(ApiClient)
     */
    private void sendButtonHandler(View view) {
        sendButton = view.findViewById(R.id.button_kirim);
        sendButton.setOnClickListener(v -> {
            newPassword = newPasswordInput.getText().toString();
            rePassword = confirmPasswordInput.getText().toString();
            if (validatePassword(newPassword, rePassword)) {
                requestResetPassword(new ApiClient());
            }
        });
    }

    /**
     * Handler untuk tombol kembali.
     *
     * @param view View tempat tombol kembali berada.
     * @see FragmentHelper#backNavigation(FragmentActivity, ImageView, Button, String, int, boolean)
     */
    private void backButtonHandler(View view) {
        backButton = view.findViewById(R.id.btnBack);
        FragmentHelper.backNavigation(getActivity(), null, backButton, null, 0, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.hideBottomNavigationView();
    }
}
