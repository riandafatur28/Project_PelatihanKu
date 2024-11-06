package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class FragmentResetPassword extends Fragment {


        private LinearLayout backButton;
        private Button sendButton;

        @SuppressLint({"MissingInflatedId", "WrongViewCast"})
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_ubah_sandi, container, false);

            sendButton = view.findViewById(R.id.button_kirim);
            backButton = view.findViewById(R.id.txtKembali);

            sendButton.setOnClickListener(v -> {
                ((MainActivity) requireActivity()).navigateToConfirmPassword();
            });

            backButton.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToOTP();
                }
            });

            return view;
        }
    }
