package com.example.projectpelatihanku;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.api.CallBackHelper;

import org.json.JSONException;

public class FragmentLogin extends Fragment {
    private Button buttonLogin;
    private TextView textRegister, textForgotPassword;
    private boolean isPasswordVisible = false;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private EditText emailEditText, passwordEditText;
    private ImageView iconPassword;
    public static String sayName;
    public static String sayTtl;
    public static String sayEmail;
    public static String sayNoTelp;
    public static String sayJenisKelamin;
    public static String sayAlamat;

    // End Point
    private String endPoint = "/login";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inisialisasi elemen UI
        buttonLogin = view.findViewById(R.id.button_login);
        textRegister = view.findViewById(R.id.text_register);
        textForgotPassword = view.findViewById(R.id.text_forgot_password);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        iconPassword = view.findViewById(R.id.icon_password);

        // Fungsi untuk login
        buttonLogin.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            ApiClient apiClient = new ApiClient();
            try {
                apiClient.oauthLogin(endPoint, email, password, new CallBackHelper() {
                    @Override
                    public void onLoginSuccess(String name) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                if (getActivity() instanceof MainActivity) {
                                    sayName = name;
                                    sayEmail = email;
                                    ((MainActivity) getActivity()).navigateToDashboard();
                                } else {
                                    Log.e("FragmentLogin", "MainActivity not found");
                                }
                            });
                        }
                    }

                    @Override
                    public void onLoginFailed() {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Username atau password salah", Toast.LENGTH_LONG).show()
                            );
                        }
                    }

                    @Override
                    public void onProfileFetchSuccess(String name, String email, String tlp, String jenisKelamin, String ttl, String alamat) {
                        // Tidak digunakan di FragmentLogin, jadi biarkan kosong
                        if (getActivity() instanceof MainActivity) {
                            sayName = name;
                            sayEmail = email;
                            sayNoTelp = tlp;
                            sayJenisKelamin = jenisKelamin;
                            sayTtl = ttl;
                            sayAlamat = alamat;
                            ((MainActivity) getActivity()).navigateToProfil();
                        } else {
                            Log.e("FragmentProfil", "MainActivity not found");
                        }
                    }

                    @Override
                    public void onProfileFetchFailure(String errorMessage) {

                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Toggle visibilitas password dengan listener
        iconPassword.setOnClickListener(v -> {
            togglePasswordVisibility(passwordEditText);
            iconPassword.setImageResource(isPasswordVisible ? R.drawable.vector_eye_close : R.drawable.vector_eye_open);
        });

        // Listener untuk pindah ke halaman register
        textRegister.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToRegister();
            } else {
                Log.e("FragmentLogin", "MainActivity not found");
            }
        });

        // Listener untuk pindah ke halaman lupa sandi
        textForgotPassword.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLupaSandi();
            } else {
                Log.e("FragmentLogin", "MainActivity not found");
            }
        });

        return view;
    }

    // Fungsi toggle visibilitas password
    private void togglePasswordVisibility(EditText passwordEditText) {
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setInputType(isPasswordVisible ?
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEditText.setSelection(passwordEditText.length());
    }

    // Validasi email agar berformat email dan diakhiri dengan "@gmail.com"
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith("@gmail.com");
    }

    // Validasi password panjangnya 8 karakter, ada huruf kapital, angka, dan tidak ada spasi
    private boolean isValidPassword(String password) {
        return password.length() == 8 && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*") && !password.contains(" ");
    }
}
