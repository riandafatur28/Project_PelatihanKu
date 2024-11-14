package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_TEMP_TOKEN = "temp_token";
    private static final String KEY_FINAL_TOKEN = "token";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Simpan token sementara
    public void saveTempToken(String tempToken) {
        editor.putString(KEY_TEMP_TOKEN, tempToken);
        editor.apply();
        Log.d("TokenManager", "Token sementara disimpan: " + tempToken);
    }

    // Dapatkan token sementara
    public String getTempToken() {
        String tempToken = sharedPreferences.getString(KEY_TEMP_TOKEN, "");
        Log.d("TokenManager", "Token sementara diambil: " + tempToken);
        return tempToken;
    }

    // Simpan token final setelah verifikasi OTP
    public void saveFinalToken(String finalToken) {
        editor.putString(KEY_FINAL_TOKEN, finalToken);
        editor.apply();
        Log.d("TokenManager", "Token final disimpan: " + finalToken);
    }

    // Dapatkan token final
    public String getFinalToken() {
        String finalToken = sharedPreferences.getString(KEY_FINAL_TOKEN, "");
        Log.d("TokenManager", "Token final diambil: " + finalToken);
        return finalToken;
    }

    // Hapus semua token (untuk logout atau reset)
    public void clearTokens() {
        editor.remove(KEY_TEMP_TOKEN);
        editor.remove(KEY_FINAL_TOKEN);
        editor.apply();
        Log.d("TokenManager", "Semua token dihapus");
    }
}
