package com.example.projectpelatihanku.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class untuk mengelola interaksi dengan shared preferences
 */
public class SharedPreferencesHelper {
    private static final String PREF_NAME = "accountToken";

    /**
     * Menyimpan token JWT ke shared preferences
     *
     * @param context konteks aplikasi
     * @param token   token JWT
     */
    public static void saveToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    /**
     * Mengambil token JWT dari shared preferences
     *
     * @param context konteks aplikasi
     * @return token JWT
     */
    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return preferences.getString("token", "Token Tidak ditemukan");
    }

    /**
     * Menghapus token JWT dari shared preferences
     *
     * @param context konteks aplikasi
     */
    public static void clearToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.apply();
    }

    /**
     * Menyimpan string ke shared preferences
     *
     * @param context    konteks aplikasi
     * @param preference nama preference
     * @param keyName    nama key dari preference
     * @param keyValue   nilai dari key
     */
    public static void saveString(Context context, String preference, String keyName,
                                  String keyValue) {
        SharedPreferences preferences = context.getSharedPreferences(preference,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(keyName, keyValue);
        editor.apply();
    }

    /**
     * Mengambil data string dari shared preferences
     *
     * @param context    konteks aplikasi
     * @param preference nama preference
     * @param keyName    nama key dari preference
     * @return nilai dari key
     */
    public static String getString(Context context, String preference, String keyName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyName, "Data tidak ditemukan");
    }

}
