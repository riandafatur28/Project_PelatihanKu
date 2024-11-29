package com.example.projectpelatihanku;

import static android.app.Activity.RESULT_OK;
import static com.example.projectpelatihanku.FragmentProfil.address;
import static com.example.projectpelatihanku.FragmentProfil.birth;
import static com.example.projectpelatihanku.FragmentProfil.gender;
import static com.example.projectpelatihanku.FragmentProfil.imagePath;
import static com.example.projectpelatihanku.FragmentProfil.phone;
import static com.example.projectpelatihanku.FragmentProfil.userEmail;
import static com.example.projectpelatihanku.FragmentProfil.username;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.FunctionHelper;
import com.example.projectpelatihanku.helper.GlideHelper;
import com.example.projectpelatihanku.helper.JwtHelper;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FragmentEditProfil extends Fragment {

    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final int PICK_IMAGE_REQUEST = 101;

    private EditText editNama, editEmail, editTTL, editNoTelp, editAlamat, editGender;
    private TextView namaUser;
    private Button btnBack, buttonUbah;
    private ImageView imageProfile, iconCamera;
    private Uri imageUri;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profil, container, false);
        initializeUIComponents(view);
        setProfileData();
        setButtonListeners();
        FragmentHelper.backHandlerDefault(this, false);
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
        imageProfile = view.findViewById(R.id.imageProfil);
        iconCamera = view.findViewById(R.id.iconCamera);
        btnBack = view.findViewById(R.id.btnBack);
        buttonUbah = view.findViewById(R.id.buttonubahProfil);
        editTTL.setEnabled(false);
        editGender.setEnabled(false);
        editEmail.setEnabled(false);
    }

    private void setProfileData() {
        namaUser.setText(username);
        editNama.setText(username);
        editGender.setText(gender);
        editEmail.setText(userEmail);
        editNoTelp.setText(phone);
        editAlamat.setText(address);
        GlideHelper.loadImage(getContext(), imageProfile, imagePath);
        try {
            if (!TextUtils.isEmpty(birth)) {
                Date birthDate = inputFormat.parse(birth);
                if (birthDate != null) {
                    String formattedDate = outputFormat.format(birthDate);
                    editTTL.setText(formattedDate);
                }
            }
        } catch (ParseException e) {
            showToast("Gagal saat parsing tanggal lahir", 3000);
        }
    }

    private void setButtonListeners() {
        buttonUbah.setOnClickListener(v -> simpanPerubahan(new ApiClient()));
        imageProfile.setOnClickListener(v -> checkPermissionsAndLoadImage());
        iconCamera.setOnClickListener(v -> checkPermissionsAndLoadImage());
        navigateBackToProfile();
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

                if (fileSizeInBytes <= 1 * 1024 * 1024) {
                    imageProfile.setImageURI(imageUri);
                } else {
                    showToast("Ukuran gambar melebihi 1 MB!", 3000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Gagal memuat gambar!", 3000);
        }
    }

    private void simpanPerubahan(ApiClient api) {
        String token = SharedPreferencesHelper.getToken(getContext());
        String data[] = new String[3];
        data[0] = editNama.getText().toString().trim();
        data[1] = editNoTelp.getText().toString().trim();
        data[2] = editAlamat.getText().toString().trim();

        JWT jwt = new JWT(token);
        Double userIdDouble = 0.0;
        try {
            userIdDouble = Double.parseDouble((String) jwt.getClaim("users").asObject(Map.class).get("id"));
        } catch (Exception e) {
            String id = JwtHelper.getUserData(SharedPreferencesHelper.getToken(getContext()), "users", "id");
            userIdDouble = Double.parseDouble(id);
        }
        int userId = userIdDouble.intValue();

        // Siapkan file jika ada gambar yang dipilih
        File file = null;
        if (imageUri != null) {
            file = FunctionHelper.getFileFromUriImage(imageUri, getContext());
        }

        // Kirim request ke API dengan userId yang valid
        api.updateProfile(token, "/users/auth/" + userId, data, file, new ApiClient.updateProfileHelper() {
            @Override
            public void onSuccess(String message) {
                // Menangani respons sukses
                requireActivity().runOnUiThread(() -> {
                    showToast(message, 3000);
                    // Arahkan ke fragment profil setelah berhasil memperbarui profil
                    FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity,
                            new FragmentProfil(), true, null);
                    MainActivity.showBottomNavigationView();
                });
            }

            @Override
            public void onFailed(IOException e) {
                // Menangani kegagalan request
                requireActivity().runOnUiThread(() -> {
                    showToast(e.getMessage(), 3000);
                });
            }
        });
    }

    private void navigateBackToProfile() {
        FragmentHelper.backNavigation(getActivity(), null, btnBack, null, 0, true);
    }

    private void showToast(String message, int duration) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(toast::cancel, duration);
    }

    private void checkPermissionsAndLoadImage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        } else {
            pilihGambar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pilihGambar();
            } else {
                showToast("Akses penyimpanan diperlukan untuk memilih gambar!", 3000);
            }
        }
    }
}
