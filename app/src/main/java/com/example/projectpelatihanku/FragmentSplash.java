package com.example.projectpelatihanku;

import static android.content.Context.MODE_PRIVATE;
import static com.example.projectpelatihanku.DashboardFragment.KEY_USER_NAME;
import static com.example.projectpelatihanku.FragmentLogin.PREFS_NAME;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentSplash extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout untuk splash screen
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        // Menggunakan Handler untuk menunda perpindahan selama 3 detik
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Selalu arahkan ke Login
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin(); // Panggil metode untuk navigasi ke login

                // Jika ingin mengarahkan ke Dashboard, gunakan kode berikut:
                // String userName = sharedPreferences.getString(KEY_USER_NAME, null);
                // if (userName != null) {
                //     ((MainActivity) getActivity()).navigateToDashboard(); // Ganti dengan metode navigasi yang tepat
                // }
            }
        }, 3000); // 3000 milidetik = 3 detik

        return view;
    }
}

