package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
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

import com.example.projectpelatihanku.api.ApiClient;

import java.io.IOException;

public class FragmentResetPassword extends Fragment {

    private LinearLayout backButton;
    private Button sendButton;
    private EditText newPasswordInput;
    private EditText confirmPasswordInput;
    private ImageView iconInputPassword;
    private ImageView iconConfirmPassword;
    private ApiClient apiClient;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        apiClient = new ApiClient();

        sendButton = view.findViewById(R.id.button_kirim);
        backButton = view.findViewById(R.id.txtKembali);
        newPasswordInput = view.findViewById(R.id.inputpassword);
        confirmPasswordInput = view.findViewById(R.id.konfirmSandiBaru);
        iconInputPassword = view.findViewById(R.id.iconinputpassword);
        iconConfirmPassword = view.findViewById(R.id.iconkonfirmpassword);

        sendButton.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getActivity(), "Password tidak boleh kosong.", Toast.LENGTH_LONG).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Password tidak cocok.", Toast.LENGTH_LONG).show();
            } else {
                resetPassword(newPassword); // Memanggil resetPassword tanpa mengirim token secara langsung
            }
        });

        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToOTP();
            }
        });

        // Mengatur aksi klik pada ikon visibilitas untuk password baru
        iconInputPassword.setOnClickListener(v -> togglePasswordVisibility(newPasswordInput, iconInputPassword));

        // Mengatur aksi klik pada ikon visibilitas untuk konfirmasi password
        iconConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordInput, iconConfirmPassword));

        return view;
    }

    private void resetPassword(String newPassword) {

//        if (finalToken == null || finalToken.isEmpty()) {
//            Toast.makeText(getActivity(), "Token tidak ditemukan. Silakan coba lagi.", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        apiClient.resetPassword(finalToken, newPassword, new ApiClient.PasswordResetHelper() {
//            @Override
//            public void onSuccess(String message) {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(() -> {
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//                        if (getActivity() instanceof MainActivity) {
//                            ((MainActivity) getActivity()).navigateToConfirmPassword();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailed(IOException e) {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(() ->
//                            Toast.makeText(getActivity(), "Reset password gagal: " + e.getMessage(), Toast.LENGTH_LONG).show()
//                    );
//                }
//            }
//        });
    }

    // Metode untuk mengatur visibilitas password
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
}
