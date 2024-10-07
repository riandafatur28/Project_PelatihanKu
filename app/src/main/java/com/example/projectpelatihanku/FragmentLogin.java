package com.example.projectpelatihanku;

import static com.example.projectpelatihanku.DashboardFragment.KEY_USER_NAME;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

public class FragmentLogin extends Fragment {
    private Button buttonLogin;
    private TextView textRegister, textForgotPassword;
    private boolean isPasswordVisible = false;

    // Save key for SharedPreferences
    static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize UI components
        buttonLogin = view.findViewById(R.id.button_login);
        textRegister = view.findViewById(R.id.text_register);
        textForgotPassword = view.findViewById(R.id.text_forgot_password);

        // Initialize EditText for email and password
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        ImageView iconPassword = view.findViewById(R.id.icon_password);

        // Navigate to Dashboard when login button is pressed
        buttonLogin.setOnClickListener(v -> {
            // Generate random email and password
            String email = generateRandomEmail();
            String password = generateRandomPassword();

            Log.d("FragmentLogin", "Generated Email: " + email);
            Log.d("FragmentLogin", "Generated Password: " + password);

            // Check if the generated email and password are valid
            if (isValidEmail(email) && isValidPassword(password)) {
                // Simulate successful login with random credentials
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, 0);

                // Save the random email and password in SharedPreferences for simulation
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_EMAIL, email);
                editor.putString(KEY_PASSWORD, password);
                editor.apply();

                // Assuming you have a way to get the user's name
                String name = "User"; // Ganti dengan logika pengambilan nama yang sebenarnya

                // Save user name in shared preferences
                editor.putString(KEY_USER_NAME, name);
                editor.apply();

                // Continue navigation if email and password are valid
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToDashboard();
                } else {
                    Log.e("FragmentLogin", "MainActivity not found");
                }
            } else {
                Toast.makeText(getContext(), "Generated credentials are not valid", Toast.LENGTH_SHORT).show();
            }
        });

        // Toggle password visibility when eye icon is clicked
        iconPassword.setOnClickListener(v -> togglePasswordVisibility(passwordEditText));

        passwordEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - iconPassword.getWidth())) {
                    togglePasswordVisibility(passwordEditText);
                    return true;
                }
            }
            return false;
        });

        // Navigate to FragmentRegister when "DAFTAR" text is clicked
        textRegister.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToRegister();
            } else {
                Log.e("FragmentLogin", "MainActivity not found");
            }
        });

        // Navigate to FragmentLupaSandi when "Lupa Kata Sandi?" text is clicked
        textForgotPassword.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLupaSandi();
            } else {
                Log.e("FragmentLogin", "MainActivity not found");
            }
        });

        return view;
    }

    private void togglePasswordVisibility(EditText passwordEditText) {
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setInputType(isPasswordVisible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEditText.setSelection(passwordEditText.length());
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith("@gmail.com");
    }

    private boolean isValidPassword(String password) {
        // Password criteria: 8 characters, contains uppercase letter, digit, no spaces or special characters
        return password.length() == 8 &&
                password.matches(".*[A-Z].*") && // Contains uppercase letter
                password.matches(".*[0-9].*") && // Contains digit
                !password.contains(" ") && // No spaces
                password.matches("[a-zA-Z0-9]*"); // Only letters and digits
    }

    private String generateRandomEmail() {
        // Generate a random email
        return "user" + System.currentTimeMillis() + "@gmail.com";
    }

    private String generateRandomPassword() {
        // Generate a random password that meets the criteria
        return "Abc12345"; // Contoh password yang sesuai kriteria
    }
}
