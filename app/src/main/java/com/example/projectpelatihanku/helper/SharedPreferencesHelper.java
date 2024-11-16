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
     * @param context konteks aplikasi
     * @param token token JWT
     */
    public static void saveToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

}
