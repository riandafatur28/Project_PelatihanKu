package com.example.projectpelatihanku.api;

public interface CallBackHelper {
    void onLoginSuccess(String name);
    void onLoginFailed();
    void onProfileFetchSuccess(String name, String email, String ttl, String tlp, String jenisKelamin, String alamat);
    void onProfileFetchFailure(String errorMessage); // Tambahkan metode ini
}
