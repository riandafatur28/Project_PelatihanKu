package com.example.projectpelatihanku;

import static com.example.projectpelatihanku.DashboardFragment.KEY_USER_NAME;

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

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        sharedPreferences = getActivity().getSharedPreferences("YourSharedPreferencesName", getContext().MODE_PRIVATE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (getActivity() instanceof MainActivity) {
                String userName = sharedPreferences.getString(KEY_USER_NAME, null);
                if (userName != null) {
                    ((MainActivity) getActivity()).navigateToDashboard();
                } else {
                    ((MainActivity) getActivity()).navigateToLogin();
                }
            }
        }, 3000);

        return view;
    }
}
