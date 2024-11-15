package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String SHARED_PREFS_NAME = "token_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        String token = prefs.getString(KEY_TOKEN, null);
        if (isTokenExpired(token)) {
            token = generateNewToken(); // generate token baru saat token kadaluarsa
            saveToken(token);
        }
        return token;
    }

    private boolean isTokenExpired(String token) {
        // Implementasikan logika untuk mengecek apakah token sudah kadaluarsa
        return false; // ganti sesuai kebutuhan
    }

    private String generateNewToken() {
        // Logika untuk menghasilkan token baru
        return "newGeneratedToken"; // ganti sesuai kebutuhan Anda
    }
}
