package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    private static final String PREFS_NAME = "UserPrefs";
    static final String KEY_USER_NAME = "username";

    private OnDashboardVisibleListener listener; // Tambahkan listener

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Retrieve the user's name from shared preferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(KEY_USER_NAME, "User"); // Default to "User" if not found

        // Set the greeting text
        TextView salamText = view.findViewById(R.id.salamText);
        salamText.setText("Halo, " + userName);

        // Set up button to navigate to FragmentTentang
        TextView lebihBanyakButton = view.findViewById(R.id.btn_lebihBanyak);
        lebihBanyakButton.setOnClickListener(v -> {
            // Navigasi ke FragmentTentang
            ((MainActivity) requireActivity()).navigateToTentang();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Panggil listener saat fragment sudah muncul
        if (listener != null) {
            listener.onDashboardVisible();
        }
    }

    // Method untuk mengatur listener dari MainActivity
    public void setOnDashboardVisibleListener(OnDashboardVisibleListener listener) {
        this.listener = listener;
    }

    // Interface untuk listener
    public interface OnDashboardVisibleListener {
        void onDashboardVisible();
    }
}
