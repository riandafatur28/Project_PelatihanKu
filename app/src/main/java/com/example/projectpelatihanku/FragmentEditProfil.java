package com.example.projectpelatihanku;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class FragmentEditProfil extends Fragment {

    private EditText editNama, editEmail, editTTL, editNoTelp, editAlamat, editGender;
    private TextView namaUser;
    private ImageView imageSecond, iconCamera;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREFS_NAME = "UserPrefs";
    private Uri imageUri;  // Untuk menyimpan URI gambar yang dipilih

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profil, container, false);

        // Inisialisasi komponen UI
        namaUser = view.findViewById(R.id.namaUser);
        editNama = view.findViewById(R.id.editnamaProfil);
        editEmail = view.findViewById(R.id.editemailProfil);
        editTTL = view.findViewById(R.id.editTTLProfil);
        editNoTelp = view.findViewById(R.id.editNoTelpProfil);
        editAlamat = view.findViewById(R.id.editalamatProfil);
        editGender = view.findViewById(R.id.editJKProfil);
        imageSecond = view.findViewById(R.id.imageProfil); // Profil image
        iconCamera = view.findViewById(R.id.iconCamera);   // Ikon kamera

        // Buat Tanggal Lahir dan Jenis Kelamin tidak bisa diubah
        editTTL.setEnabled(false);
        editGender.setEnabled(false);
        editEmail.setEnabled(false);  // Email tidak bisa diubah

        // Ambil data dari SharedPreferences untuk ditampilkan di form
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, 0);
        String nama = sharedPreferences.getString("nama", "");
        String email = sharedPreferences.getString("email", "");
        String tanggalLahir = sharedPreferences.getString("tanggalLahir", "");
        String nomorTelepon = sharedPreferences.getString("nomorTelepon", "");
        String alamat = sharedPreferences.getString("alamat", "");
        String gender = sharedPreferences.getString("gender", "");
        String imageUriString = sharedPreferences.getString("image_uri", null);

        // Set data ke EditText
        namaUser.setText(nama);
        editNama.setText(nama);
        editEmail.setText(email);
        editTTL.setText(tanggalLahir);
        editNoTelp.setText(nomorTelepon);
        editAlamat.setText(alamat);
        editGender.setText(gender);

        // Set gambar profil jika ada di SharedPreferences
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            imageSecond.setImageURI(imageUri);
        }

        // Tombol untuk menyimpan perubahan
        Button buttonUbah = view.findViewById(R.id.buttonubahProfil);
        buttonUbah.setOnClickListener(v -> simpanPerubahan());

        // Tombol untuk kembali
        LinearLayout txtKembali = view.findViewById(R.id.txtKembali);
        txtKembali.setOnClickListener(v -> navigateBackToProfile());

        // Izin akses galeri
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        // Listener untuk memilih gambar
        imageSecond.setOnClickListener(v -> pilihGambar());
        iconCamera.setOnClickListener(v -> pilihGambar()); // Ikon kamera juga bisa memilih gambar

        return view;
    }

    // Fungsi untuk memilih gambar dari galeri
    private void pilihGambar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Menangani hasil pemilihan gambar
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Mengecek ukuran file gambar
                Cursor cursor = getActivity().getContentResolver().query(imageUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    long fileSizeInBytes = cursor.getLong(sizeIndex);
                    cursor.close();

                    if (fileSizeInBytes <= 1 * 1024 * 1024) { // Maks 1 MB
                        imageSecond.setImageURI(imageUri);

                        // Simpan URI gambar ke SharedPreferences
                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("image_uri", imageUri.toString());
                        editor.apply();  // Simpan perubahan

                    } else {
                        showToast("Ukuran gambar melebihi 1 MB!", 3000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Gagal memuat gambar!", 3000);
            }
        }
    }

    // Fungsi untuk menyimpan perubahan profil
    private void simpanPerubahan() {
        String nama = editNama.getText().toString().trim();
        String noTelp = editNoTelp.getText().toString().trim();
        String alamat = editAlamat.getText().toString().trim();

        // Validasi input
        if (!validasiNama(nama)) {
            showToast("Nama harus hanya menggunakan huruf!", 3000);
            return;
        }

        if (!validasiNoTelp(noTelp)) {
            showToast("No. Telepon harus hanya angka dan maksimal 15 digit!", 3000);
            return;
        }

        // Simpan perubahan ke SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nama", nama);
        editor.putString("nomorTelepon", noTelp);
        editor.putString("alamat", alamat);
        editor.apply();  // Simpan perubahan

        showToast("Perubahan disimpan!", 3000);

        // Navigasi kembali ke profil
        navigateBackToProfile();
    }

    private void navigateBackToProfile() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToProfil();
            ((MainActivity) getActivity()).showBottomNavigation(); // Menampilkan kembali BottomNavigation
        }
    }

    private void showToast(String message, int duration) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(toast::cancel, duration);
    }

    private boolean validasiNama(String nama) {
        return nama.matches("[a-zA-Z ]+");
    }

    private boolean validasiNoTelp(String noTelp) {
        return noTelp.matches("\\d{1,15}");
    }
}
