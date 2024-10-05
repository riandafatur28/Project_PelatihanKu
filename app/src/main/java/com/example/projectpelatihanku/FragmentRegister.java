package com.example.projectpelatihanku;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.content.SharedPreferences;

public class FragmentRegister extends Fragment {

    private Button buttonRegister;
    private TextView textLogin;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "email"; // Kunci untuk email

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Inisialisasi komponen
        buttonRegister = view.findViewById(R.id.buttonregister);
        textLogin = view.findViewById(R.id.apakahpunyaakun);
        EditText inputNama = view.findViewById(R.id.inputNama);
        EditText inputEmail = view.findViewById(R.id.inputEmail);
        EditText inputPassword = view.findViewById(R.id.inputpassword);
        EditText konfirmasiPassword = view.findViewById(R.id.KonfirmasiPassword);
        ImageView iconPassword = view.findViewById(R.id.iconinputpassword);
        ImageView iconConfirmPassword = view.findViewById(R.id.iconkonfirmpassword);

        // Logika visibilitas password
        iconPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                togglePasswordVisibility(inputPassword);
                return true;
            }
            return false;
        });

        // Logika visibilitas konfirmasi password
        iconConfirmPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                togglePasswordVisibility(konfirmasiPassword);
                return true;
            }
            return false;
        });

        // Logika tombol daftar
        buttonRegister.setOnClickListener(v -> {
            String nama = inputNama.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String konfirmasi = konfirmasiPassword.getText().toString().trim();

            if (!isInputValid(nama, email, password, konfirmasi)) {
                return; // Jika input tidak valid, hentikan eksekusi
            }

            // Simpan email yang terdaftar
            saveEmail(email);

            Toast.makeText(getContext(), "Akun berhasil dibuat", Toast.LENGTH_SHORT).show();

            // Arahkan ke halaman login setelah berhasil membuat akun
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });

        // Arahkan ke halaman login ketika "Apakah Anda sudah punya akun?" ditekan
        textLogin.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });

        return view;
    }

    private void togglePasswordVisibility(EditText editText) {
        if (isPasswordVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible = false;
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPasswordVisible = true;
        }
        editText.setSelection(editText.getText().length());
    }

    private boolean isInputValid(String nama, String email, String password, String konfirmasi) {
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || konfirmasi.isEmpty()) {
            Toast.makeText(getContext(), "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidName(nama)) {
            Toast.makeText(getContext(), "Nama hanya boleh diisi dengan huruf dan spasi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isEmailNew(email)) {
            Toast.makeText(getContext(), "Email sudah digunakan", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(getContext(), "Email harus menggunakan domain @gmail.com", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(getContext(), "Password harus terdiri dari 8 karakter, kombinasi huruf dan angka, " +
                    "minimal satu huruf kapital, tidak boleh ada spasi, dan tidak ada karakter spesial", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(konfirmasi)) {
            Toast.makeText(getContext(), "Konfirmasi password tidak sama dengan password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Semua validasi berhasil
    }

    private boolean isValidName(String nama) {
        // Cek apakah nama hanya terdiri dari huruf dan spasi
        return nama.matches("[a-zA-Z\\s]+");
    }

    private boolean isEmailNew(String email) {
        // Ambil email yang terdaftar dari SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, 0);
        String registeredEmail = sharedPreferences.getString(KEY_EMAIL, null);
        return registeredEmail == null || !email.equals(registeredEmail); // Email baru jika tidak terdaftar
    }

    private boolean isValidPassword(String password) {
        // Cek apakah password valid
        return password.length() == 8 && password.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-zA-Z]).*$") &&
                !password.contains(" ") && !password.matches(".*[^a-zA-Z0-9].*");
    }

    private void saveEmail(String email) {
        // Simpan email yang terdaftar ke SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }
}
