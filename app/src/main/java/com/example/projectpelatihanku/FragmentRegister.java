package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.FunctionHelper;

import java.io.IOException;


import okhttp3.MultipartBody;

public class FragmentRegister extends Fragment {

    private Button buttonRegister;
    private TextView textLogin;
    private Spinner spinner;
    private String selectedGender = null;
    private EditText inputNama, inputEmail, inputPassword, konfirmasiPassword, inputNoTelp, inputAlamat, inputTanggal;
    private ImageView iconInputPassword, iconConfirmPassword;
    private static int imageId = 0;
    private final String endPoint = "/user/registrations";
    private final String regexNama = "^[a-zA-Z.,' ]+$";
    private final String regexEmail = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
    private final String regexPassword = "^[A-Za-z\\d!@#$%^&*]{1,15}$";
    private final String regexNoHp = "^08[1-9][0-9]{7,10}$";
    private final String regexAlamat = "^[A-Za-z0-9\\s.,]{1,100}$";

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        buttonRegister = view.findViewById(R.id.buttonregister);
        textLogin = view.findViewById(R.id.txtMasuk);
        inputNama = view.findViewById(R.id.inputNama);
        inputEmail = view.findViewById(R.id.inputEmail);
        inputPassword = view.findViewById(R.id.inputpassword);
        konfirmasiPassword = view.findViewById(R.id.KonfirmasiPassword);
        inputNoTelp = view.findViewById(R.id.inputNoTelp);
        inputAlamat = view.findViewById(R.id.inputAlamat);
        iconInputPassword = view.findViewById(R.id.iconInputPassword);
        iconConfirmPassword = view.findViewById(R.id.iconConfirmPassword);

        spinner = view.findViewById(R.id.jenisKelamin);
        setupGenderSpinner();

        inputTanggal = view.findViewById(R.id.inputTanggal);
        view.findViewById(R.id.iconKalender).setOnClickListener(v -> showDatePicker());

        iconInputPassword.setOnClickListener(v -> togglePasswordVisibility(inputPassword, iconInputPassword));
        iconConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(konfirmasiPassword, iconConfirmPassword));

        buttonRegister.setOnClickListener(v -> {
            requestRegister(new ApiClient());
        });

        textLogin.setOnClickListener(v -> {
            BackHandler();
        });

        return view;
    }

    /**
     * Mengirim permintaan registrasi ke server.
     *
     * @param api Class Service untuk melakukan request ke server.
     * @see ApiClient#Register(String, MultipartBody.Part, String[], ApiClient.RegisterAccountHelper)
     * @see FunctionHelper#validateString(Context, String, String, String, int)
     * @see FunctionHelper#convertImageToMultipart(Context, int)
     */
    private void requestRegister(ApiClient api) {
        String nama = inputNama.getText().toString().trim();
        String tanggalLahir = inputTanggal.getText().toString().trim();
        String nomorTelepon = inputNoTelp.getText().toString().trim();
        String alamat = inputAlamat.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String konfirmPass = konfirmasiPassword.getText().toString().trim();

        boolean isValid = true;
        isValid = FunctionHelper.validateString(getContext(), nama, "Nama", regexNama, 50) && isValid;
        isValid = FunctionHelper.validateString(getContext(), nomorTelepon, "Nomor Telepon",
                regexNoHp, 13) && isValid;
        isValid = FunctionHelper.validateString(getContext(), email, "Email",
                regexEmail, 70) && isValid;
        isValid = FunctionHelper.validateString(getContext(), password, "Password",
                regexPassword, 15) && isValid;
        isValid = FunctionHelper.validateString(getContext(), alamat, "Alamat",
                regexAlamat, 100) && isValid;
        isValid = genderValidate(selectedGender) && isValid;
        isValid = dateValidate(tanggalLahir) && isValid;
        isValid = passwordValidate(password, konfirmPass) && isValid;

        if (isValid) {

            String data[] = new String[7];
            data[0] = nama;
            data[1] = tanggalLahir;
            data[2] = nomorTelepon;
            data[3] = alamat;
            data[4] = email;
            data[5] = password;

            if (selectedGender.equalsIgnoreCase("Laki-laki")) {
                imageId = R.drawable.img_men;
                data[6] = "Laki-laki";
            } else if (selectedGender.equalsIgnoreCase("Perempuan")) {
                imageId = R.drawable.img_women;
                data[6] = "Perempuan";
            }

            MultipartBody.Part imagePart = FunctionHelper.convertImageToMultipart(getContext(),
                    imageId);
            api.Register(endPoint, imagePart, data, new ApiClient.RegisterAccountHelper() {
                @Override
                public void onSuccess(String message) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        FragmentLogin login = new FragmentLogin();
                        FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity, login,
                                true, null);
                    });
                }

                @Override
                public void onFailed(IOException e) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }

    /**
     * Memvalidasi password dan konfirmasi password.
     *
     * @param password        password.
     * @param confirmPassword konfirmasi password.
     */
    private boolean passwordValidate(String password, String confirmPassword) {
        if (!password.equalsIgnoreCase(confirmPassword)) {
            Toast.makeText(getContext(), "Password harus bernilai sama", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Memvalidasi input jenis kelamin.
     *
     * @param value jenis kelamin.
     * @return true jika tidak kosong dan false jika kosong.
     */
    private boolean genderValidate(String value) {
        if (value == null || value.isEmpty() || value.trim().equalsIgnoreCase("Jenis Kelamin")) {
            Toast.makeText(getContext(), "Pilih Opsi Jenis Kelamin yang tersedia!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Memvalidasi input tanggal lahir.
     *
     * @param value tanggal lahir.
     * @return true jika tidak kosong dan false jika kosong.
     */
    private boolean dateValidate(String value) {
        if (value == null || value.isEmpty() || value.trim().equalsIgnoreCase("Tanggal Lahir")) {
            Toast.makeText(getContext(), "Tanggal Lahir wajib diisi",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Mengatur spinner untuk memilih jenis kelamin.
     */
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
                    selectedGender = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }


    /**
     * Menampilkan dialog DatePicker untuk memilih tanggal lahir.
     */
    private void showDatePicker() {
        FunctionHelper.datePickerHelper(getContext(), inputTanggal);
    }

    /**
     * Mengatur visibilitas password ketika icon visibilitas ditekan.
     *
     * @param passwordField text input password yang akan diubah visibilitasnya.
     * @param toggleIcon    ikon visibilitas, (icon_eye_open atau icon_eye_close).
     */
    private void togglePasswordVisibility(EditText passwordField, ImageView toggleIcon) {
        if (passwordField.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.vector_eye_close);
        } else {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.vector_eye_open);
        }
        passwordField.setSelection(passwordField.getText().length());
    }

    /**
     * Metode untuk handle aksi tombol kembali.
     */
    private void BackHandler() {
        FragmentLogin login = new FragmentLogin();
        FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity, login, true, null);
    }
}
