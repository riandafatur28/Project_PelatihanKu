package com.example.projectpelatihanku.api;

import java.io.IOException;

public interface CallBackHelper {
    // Login
    void onLoginSuccess(String token);
    void onLoginFailed(IOException e);



    void onProfileFetchSuccess(String name, String email, String ttl, String tlp, String jenisKelamin, String alamat);
    void onProfileFetchFailure(String errorMessage);

}
