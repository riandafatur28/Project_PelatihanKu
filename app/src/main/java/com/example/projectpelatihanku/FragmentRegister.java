package com.example.projectpelatihanku;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private Spinner spinner;
    private String selectedGender = null; // Variabel untuk menyimpan gender yang dipilih

    // Variabel untuk input tanggal
    private EditText inputTanggal;
    private ImageView iconKalender;

    // Variabel untuk gambar profil
    private ImageView imageProfil;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    // Variabel untuk SharedPreferences
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_NAME = "username"; // Kunci nama pengguna

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Inisialisasi komponen UI
        buttonRegister = view.findViewById(R.id.buttonregister);
        textLogin = view.findViewById(R.id.apakahpunyaakun);
        EditText inputNama = view.findViewById(R.id.inputNama);
        EditText inputEmail = view.findViewById(R.id.inputEmail);
        EditText inputPassword = view.findViewById(R.id.inputpassword);
        EditText konfirmasiPassword = view.findViewById(R.id.KonfirmasiPassword);
        EditText inputNoTelp = view.findViewById(R.id.inputNoTelp);
        EditText inputAlamat = view.findViewById(R.id.inputAlamat);
        ImageView iconPassword = view.findViewById(R.id.iconinputpassword);
        ImageView iconConfirmPassword = view.findViewById(R.id.iconkonfirmpassword);
        imageProfil = view.findViewById(R.id.imageProfil);

        // Inisialisasi Spinner untuk jenis kelamin
        spinner = view.findViewById(R.id.jenisKelamin);
        setupGenderSpinner();

        // Inisialisasi input tanggal lahir
        inputTanggal = view.findViewById(R.id.inputTanggal);
        iconKalender = view.findViewById(R.id.iconKalender);

        iconKalender.setOnClickListener(v -> showDatePicker());

        inputTanggal.setOnClickListener(v -> showDatePicker());

        imageProfil.setOnClickListener(v -> openGallery());

        iconPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                togglePasswordVisibility(inputPassword);
                return true;
            }
            return false;
        });

        iconConfirmPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                togglePasswordVisibility(konfirmasiPassword);
                return true;
            }
            return false;
        });

        // Listener untuk tombol Register
        buttonRegister.setOnClickListener(v -> {
            String nama = inputNama.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String konfirmasi = konfirmasiPassword.getText().toString().trim();
            String nomorTelepon = inputNoTelp.getText().toString().trim();
            String alamat = inputAlamat.getText().toString().trim();
            String tanggalLahir = inputTanggal.getText().toString().trim();

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

    private void togglePasswordVisibility(EditText inputPassword) {
    }

    // Setup Spinner untuk memilih gender
    private void setupGenderSpinner() {
        if (spinner != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    getContext(),
                    R.array.jenis_kelamin_array,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        selectedGender = null;
                    } else {
                        selectedGender = parent.getItemAtPosition(position).toString();
                        if (selectedGender.equals("Laki-laki")) {
                            imageProfil.setImageResource(R.drawable.men);
                        } else if (selectedGender.equals("Perempuan")) {
                            imageProfil.setImageResource(R.drawable.women);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        } else {
            Log.e("FragmentRegister", "Spinner untuk jenis kelamin tidak ditemukan di layout.");
        }
    }

    // Fungsi untuk menyimpan data pengguna ke SharedPreferences
    private void saveUserData(String nama, String gender, String email, String tanggalLahir, String nomorTelepon, String alamat) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, nama);  // Simpan nama pengguna
        editor.putString("gender", gender);
        editor.putString("email", email);
        editor.putString("tanggalLahir", tanggalLahir);
        editor.putString("nomorTelepon", nomorTelepon);
        editor.putString("alamat", alamat);
        editor.apply();
    }

    // Fungsi validasi input
    private boolean isInputValid(String nama, String email, String password, String konfirmasi) {
        if (nama.isEmpty()) {
            Toast.makeText(getContext(), "Nama harus diisi", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isValidName(nama)) {
            Toast.makeText(getContext(), "Nama hanya boleh diisi dengan huruf dan spasi", Toast.LENGTH_LONG).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Email harus diisi", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isEmailValid(email)) {
            Toast.makeText(getContext(), "Email harus valid dan tidak boleh hanya berisi @gmail.com", Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Password harus diisi", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(getContext(), "Password harus terdiri dari 8 karakter, kombinasi huruf dan angka, " +
                    "minimal satu huruf kapital, tidak boleh ada spasi, dan tidak ada karakter spesial", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!password.equals(konfirmasi)) {
            Toast.makeText(getContext(), "Konfirmasi password tidak sama dengan password", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean isValidName(String nama) {
        return nama.matches("[a-zA-Z\\s]+");
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".") && !email.equals("@gmail.com") && !email.startsWith("@");
    }

    private boolean isValidPassword(String password) {
        return password.length() == 8 && password.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-zA-Z]).*$") &&
                !password.contains(" ") && !password.matches(".*[^a-zA-Z0-9].*");
    }

    // Menampilkan DatePickerDialog untuk memilih tanggal
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    inputTanggal.setText(selectedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    // Membuka galeri untuk memilih gambar
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageProfil.setImageURI(imageUri);
        }
    }
}
