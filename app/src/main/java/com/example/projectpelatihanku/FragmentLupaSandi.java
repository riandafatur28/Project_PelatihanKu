package com.example.projectpelatihanku;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.Arrays;
import java.util.List;

public class FragmentLupaSandi extends Fragment {

    private Button buttonKirim;
    private LinearLayout txtKembali;
    private EditText editTextEmail;

    private List<String> registeredEmails = Arrays.asList(
            "user1@gmail.com",
            "user2@gmail.com",
            "user3@gmail.com"
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lupa_sandi, container, false);

        buttonKirim = view.findViewById(R.id.button_kirim);
        txtKembali = view.findViewById(R.id.txtKembali);
        editTextEmail = view.findViewById(R.id.inputEmail);

        buttonKirim.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();

            // Validasi email
            if (email.isEmpty()) {
                Toast.makeText(getActivity(), "Email tidak boleh kosong.", Toast.LENGTH_LONG).show();
            } else if (!email.endsWith("@gmail.com")) {
                Toast.makeText(getActivity(), "Masukkan email yang valid dengan domain @gmail.com", Toast.LENGTH_LONG).show();
            } else if (!checkEmailInRegisteredAccounts(email)) {
                Toast.makeText(getActivity(), "Email tidak terdaftar. Silakan gunakan email yang sudah terdaftar.", Toast.LENGTH_LONG).show();
            } else {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToOTP();
                } else {
                    Log.e("FragmentLupaSandi", "MainActivity not found");
                }
            }
        });

        txtKembali.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });

        return view;
    }

    private boolean checkEmailInRegisteredAccounts(String email) {
        return registeredEmails.contains(email);
    }
}
