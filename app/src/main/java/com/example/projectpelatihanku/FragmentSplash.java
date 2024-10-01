package com.example.projectpelatihanku;

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
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Memeriksa apakah MainActivity aktif dan melakukan navigasi ke FragmentLogin
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToLogin(); // Memanggil metode yang benar
                }
            }
        }, 3000); // 3000 milidetik = 3 detik

        return view;
    }
}
