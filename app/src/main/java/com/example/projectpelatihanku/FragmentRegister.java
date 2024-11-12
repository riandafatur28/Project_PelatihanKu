package com.example.projectpelatihanku;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.example.projectpelatihanku.api.ApiClient;

import org.json.JSONException;

import java.io.IOException;
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
    private EditText inputPassword, konfirmasiPassword;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private boolean isImageUploaded = false;
    private String endPoint = "user/create";

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
            String konfirmPass = konfirmasiPassword.getText().toString().trim();
            String nomorTelepon = inputNoTelp.getText().toString().trim();
            String alamat = inputAlamat.getText().toString().trim();
            String tanggalLahir = inputTanggal.getText().toString().trim();

            // Tentukan fotoProfil berdasarkan gender
            String fotoProfil;
            if ("Laki-laki".equals(selectedGender)) {
                fotoProfil = "img_men"; // Ganti dengan path yang sesuai untuk img_men
            } else if ("Perempuan".equals(selectedGender)) {
                fotoProfil = "img_women"; // Ganti dengan path yang sesuai untuk img_women
            } else {
                fotoProfil = null;
            }

            if (isInputValid(nama, email, password, konfirmPass, nomorTelepon, alamat, tanggalLahir)) {
                ApiClient apiClient = new ApiClient();
                try {
                    apiClient.Register(endPoint, nama, selectedGender, tanggalLahir, nomorTelepon, email, password, konfirmPass, fotoProfil, alamat, new ApiClient.RegisterHelper() {
                        @Override
                        public void onSuccess(String response) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                                if (getActivity() instanceof MainActivity) {
                                    ((MainActivity) getActivity()).navigateToLogin();
                                }
                            });
                        }

                        @Override
                        public void onFailed(IOException e) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Registrasi gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        textLogin.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });

        return view;
    }

    /// Inisialisasi Spinner Gender dan Gambar Profil
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

                    // Atur gambar profil sesuai gender jika pengguna belum mengunggah gambar
                    if (!isImageUploaded && selectedGender != null) {
                        if ("Laki-laki".equals(selectedGender)) {
                            imageProfil.setImageResource(R.drawable.img_men);
                        } else if ("Perempuan".equals(selectedGender)) {
                            imageProfil.setImageResource(R.drawable.img_women);
                        } else {
                            imageProfil.setImageResource(R.drawable.gambar_user); // Gambar default untuk opsi lainnya
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
    }

    // Fungsi untuk mengatur gambar profil dari foto yang diunggah
    private void setProfileImage(Uri imageUri) {
        imageProfil.setImageURI(imageUri);
        isImageUploaded = true;
    }

    private void togglePasswordVisibility(EditText passwordEditText, ImageView toggleIcon) {
        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.vector_eye_close); // Ganti dengan ikon mata terbuka
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.vector_eye_open); // Ganti dengan ikon mata tertutup
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
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
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            setProfileImage(imageUri);
        }
    }

    private boolean isInputValid(String nama, String email, String password, String konfirmasi, String nomorTelepon, String alamat, String tanggalLahir) {
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
