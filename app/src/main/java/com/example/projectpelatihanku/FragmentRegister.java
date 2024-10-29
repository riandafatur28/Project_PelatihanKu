package com.example.projectpelatihanku;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class FragmentRegister extends Fragment {

    private Button buttonRegister;
    private TextView textLogin;
    private Spinner spinner;
    private String selectedGender = null;
    private EditText inputTanggal;
    private ImageView imageProfil, iconPassword, iconConfirmPassword;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USER_NAME = "username";

    private EditText inputPassword, konfirmasiPassword;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Inisialisasi komponen UI
        buttonRegister = view.findViewById(R.id.buttonregister);
        textLogin = view.findViewById(R.id.apakahpunyaakun);
        EditText inputNama = view.findViewById(R.id.inputNama);
        EditText inputEmail = view.findViewById(R.id.inputEmail);
        inputPassword = view.findViewById(R.id.inputpassword);
        konfirmasiPassword = view.findViewById(R.id.KonfirmasiPassword);
        EditText inputNoTelp = view.findViewById(R.id.inputNoTelp);
        EditText inputAlamat = view.findViewById(R.id.inputAlamat);
        imageProfil = view.findViewById(R.id.imageProfil);

        // Inisialisasi ikon mata
        iconPassword = view.findViewById(R.id.iconinputpassword);
        iconConfirmPassword = view.findViewById(R.id.iconkonfirmpassword);

        // Toggle visibilitas password dan konfirmasi password
        iconPassword.setOnClickListener(v -> togglePasswordVisibility(inputPassword, iconPassword));
        iconConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(konfirmasiPassword, iconConfirmPassword));

        spinner = view.findViewById(R.id.jenisKelamin);
        setupGenderSpinner();
        inputTanggal = view.findViewById(R.id.inputTanggal);
        view.findViewById(R.id.iconKalender).setOnClickListener(v -> showDatePicker());

        imageProfil.setOnClickListener(v -> openGallery());

        // Listener untuk tombol Register
        buttonRegister.setOnClickListener(v -> {
            String nama = inputNama.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String konfirmasi = konfirmasiPassword.getText().toString().trim();
            String nomorTelepon = inputNoTelp.getText().toString().trim();
            String alamat = inputAlamat.getText().toString().trim();
            String tanggalLahir = inputTanggal.getText().toString().trim();

            // Validasi input sebelum menyimpan data
            if (!isInputValid(nama, email, password, konfirmasi)) {
                return;
            }

            if (selectedGender == null) {
                Toast.makeText(getContext(), "Harap pilih jenis kelamin", Toast.LENGTH_LONG).show();
                return;
            }

            // Simpan data ke SharedPreferences
            saveUserData(nama, selectedGender, email, tanggalLahir, nomorTelepon, alamat);

            Toast.makeText(getContext(), "Akun berhasil dibuat", Toast.LENGTH_LONG).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });

        textLogin.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });

        return view;
    }

    private void setupGenderSpinner() {
        if (spinner != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.jenis_kelamin_array,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedGender = position == 0 ? null : parent.getItemAtPosition(position).toString();
                    if (selectedGender != null) {
                        imageProfil.setImageResource(selectedGender.equals("Laki-laki") ? R.drawable.vector_men : R.drawable.vector_women);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
    }

    private void togglePasswordVisibility(EditText passwordEditText, ImageView toggleIcon) {
        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.vector_eye_close); // Ganti dengan ikon mata terbuka
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.vector_eye_open); // Ganti dengan ikon mata tertutup
        }
        passwordEditText.setSelection(passwordEditText.getText().length()); // Pindah kursor ke akhir teks
    }

    private void saveUserData(String nama, String gender, String email, String tanggalLahir, String nomorTelepon, String alamat) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, nama);
        editor.putString("gender", gender);
        editor.putString("email", email);
        editor.putString("tanggalLahir", tanggalLahir);
        editor.putString("nomorTelepon", nomorTelepon);
        editor.putString("alamat", alamat);
        if (imageUri != null) {
            editor.putString("image_uri", imageUri.toString());
        }
        editor.apply();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, day) -> {
            inputTanggal.setText(day + "/" + (month + 1) + "/" + year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openGallery() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageProfil.setImageURI(imageUri);
        }
    }

    private boolean isInputValid(String nama, String email, String password, String konfirmasi) {
        if (nama.isEmpty()) {
            Toast.makeText(getContext(), "Nama harus diisi", Toast.LENGTH_LONG).show();
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Email tidak valid", Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.isEmpty() || !password.equals(konfirmasi)) {
            Toast.makeText(getContext(), "Password dan konfirmasi tidak cocok", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
