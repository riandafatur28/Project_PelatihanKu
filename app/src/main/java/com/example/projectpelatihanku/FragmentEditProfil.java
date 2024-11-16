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
import android.text.TextUtils;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.projectpelatihanku.FragmentProfil.address;
import static com.example.projectpelatihanku.FragmentProfil.birth;
import static com.example.projectpelatihanku.FragmentProfil.gender;
import static com.example.projectpelatihanku.FragmentProfil.phone;
import static com.example.projectpelatihanku.FragmentProfil.userEmail;
import static com.example.projectpelatihanku.FragmentProfil.username;

public class FragmentEditProfil extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String IMAGE_URI_KEY = "image_uri";
    private static final String ACCOUNT_TOKEN_PREFS = "accountToken";
    private static final String GENDER_KEY = "gender";
    private static final String DEFAULT_IMAGE = "gambar_user";
    private static final String MALE_IMAGE = "img_men";
    private static final String FEMALE_IMAGE = "img_women";

    private EditText editNama, editEmail, editTTL, editNoTelp, editAlamat, editGender;
    private TextView namaUser;
    private ImageView imageSecond, iconCamera;
    private Uri imageUri;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profil, container, false);
        initializeUIComponents(view);
        setProfileData();
        checkPermissionsAndLoadImage();
        setButtonListeners(view);
        return view;
    }

    private void initializeUIComponents(View view) {
        namaUser = view.findViewById(R.id.namaUser);
        editNama = view.findViewById(R.id.editnamaProfil);
        editEmail = view.findViewById(R.id.editemailProfil);
        editTTL = view.findViewById(R.id.editTTLProfil);
        editNoTelp = view.findViewById(R.id.editNoTelpProfil);
        editAlamat = view.findViewById(R.id.editalamatProfil);
        editGender = view.findViewById(R.id.editJKProfil);
        imageSecond = view.findViewById(R.id.imageProfil);
        iconCamera = view.findViewById(R.id.iconCamera);

        // Disable editing for certain fields
        editTTL.setEnabled(false);
        editGender.setEnabled(false);
        editEmail.setEnabled(false);
    }

    private void setProfileData() {
        // Set data from FragmentProfil
        namaUser.setText(username);
        editNama.setText(username);
        editGender.setText(gender);
        editEmail.setText(userEmail);
        editNoTelp.setText(phone);
        editAlamat.setText(address);

        // Format birth date
        try {
            if (!TextUtils.isEmpty(birth)) {
                Date birthDate = inputFormat.parse(birth);
                if (birthDate != null) {
                    String formattedDate = outputFormat.format(birthDate);
                    editTTL.setText(formattedDate);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            showToast("Format tanggal lahir tidak valid", 3000);
        }
    }

    private void checkPermissionsAndLoadImage() {
        // Request permission for external storage if not granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        // Load profile image
        loadProfileImage();
    }

    private void loadProfileImage() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ACCOUNT_TOKEN_PREFS, Context.MODE_PRIVATE);
        String imageUriString = sharedPreferences.getString(IMAGE_URI_KEY, null);
        String gender = sharedPreferences.getString(GENDER_KEY, "Tidak diketahui");

        if (imageUriString != null) {
            try {
                imageSecond.setImageURI(Uri.parse(imageUriString));
                Log.d("FragmentEditProfil", "Loaded image from URI: " + imageUriString);
            } catch (Exception e) {
                setDefaultImage(gender);
                Log.e("FragmentEditProfil", "Failed to load image from URI", e);
            }
        } else {
            setDefaultImage(gender);
        }
    }

    private void setDefaultImage(String gender) {
        if ("Laki-laki".equalsIgnoreCase(gender)) {
            imageSecond.setImageResource(R.drawable.img_men);
        } else if ("Perempuan".equalsIgnoreCase(gender)) {
            imageSecond.setImageResource(R.drawable.img_women);
        } else {
            imageSecond.setImageResource(R.drawable.gambar_user);
        }
    }

    private void setButtonListeners(View view) {
        Button buttonUbah = view.findViewById(R.id.buttonubahProfil);
        buttonUbah.setOnClickListener(v -> simpanPerubahan());

        LinearLayout txtKembali = view.findViewById(R.id.txtKembali);
        txtKembali.setOnClickListener(v -> navigateBackToProfile());

        imageSecond.setOnClickListener(v -> pilihGambar());
        iconCamera.setOnClickListener(v -> pilihGambar());
    }

    private void pilihGambar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            handleImageSelection();
        }
    }

    private void handleImageSelection() {
        try {
            Cursor cursor = getActivity().getContentResolver().query(imageUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                long fileSizeInBytes = cursor.getLong(sizeIndex);
                cursor.close();

                if (fileSizeInBytes <= 1 * 1024 * 1024) { // Maks 1 MB
                    imageSecond.setImageURI(imageUri); // Set image to ImageView
                    saveImageUriToPreferences(imageUri); // Save image URI
                } else {
                    showToast("Ukuran gambar melebihi 1 MB!", 3000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Gagal memuat gambar!", 3000);
        }
    }

    private void saveImageUriToPreferences(Uri imageUri) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ACCOUNT_TOKEN_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IMAGE_URI_KEY, imageUri.toString());
        editor.apply(); // Asynchronously save the changes
        Log.d("FragmentEditProfil", "Image URI saved: " + imageUri.toString());
    }

    private void simpanPerubahan() {
        String nama = editNama.getText().toString().trim();
        String noTelp = editNoTelp.getText().toString().trim();
        String alamat = editAlamat.getText().toString().trim();

        // Validasi input
        if (!validasiNama(nama)) {
            showToast("Nama harus hanya menggunakan huruf!", 3000);
            return;
        }

        // Perbarui nama pada UI `namaUser` di FragmentEditProfil
        namaUser.setText(nama);
        showToast("Perubahan berhasil disimpan!", 3000);
    }

    private void navigateBackToProfile() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToProfil();
            ((MainActivity) getActivity()).showBottomNavigation();
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
}
