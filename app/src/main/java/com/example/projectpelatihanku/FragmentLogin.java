package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.api.ApiClient;


import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

public class FragmentLogin extends Fragment {
    private Button buttonLogin;
    private TextView textRegister, textForgotPassword;
    private boolean isPasswordVisible = false;
    private EditText emailEditText, passwordEditText;
    private ImageView iconPassword;

    static String Name;
    static String firstName;

    private String endPoint = "login";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        buttonLogin = view.findViewById(R.id.button_login);
        textRegister = view.findViewById(R.id.text_register);
        textForgotPassword = view.findViewById(R.id.text_forgot_password);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        iconPassword = view.findViewById(R.id.icon_password);

        buttonLogin.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            ApiClient apiClient = new ApiClient();
            try {
                apiClient.oauthLogin(endPoint, email, password, new ApiClient.LoginHelper() {
                    @Override
                    public void onSuccess(String token) {
                        getActivity().runOnUiThread(() -> {
                            SharedPreferences preferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", token);
                            editor.apply();
                            JWT jwt = new JWT(token);
                            String username = jwt.getClaim("users").asObject(Map.class).get("username").toString();
                            Name = username;
                            firstName = Name.split(" ")[0];
                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).navigateToDashboard();
                            } else {
                                Log.e("FragmentLogin", "MainActivity not found");
                            }
                        });

                    }

                    @Override
                    public void onFailed(IOException e) {
                        Log.d("Failed", "onFailed " + e.getMessage());
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        iconPassword.setOnClickListener(v -> {
            togglePasswordVisibility(passwordEditText);
            iconPassword.setImageResource(isPasswordVisible ? R.drawable.vector_eye_close : R.drawable.vector_eye_open);
        });

        textRegister.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToRegister();
            } else {
            }
        });

        textForgotPassword.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToForgotPassword();
            } else {
            }
        });

        return view;
    }

    private void togglePasswordVisibility(EditText passwordEditText) {
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setInputType(isPasswordVisible ?
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEditText.setSelection(passwordEditText.length());
    }
}
