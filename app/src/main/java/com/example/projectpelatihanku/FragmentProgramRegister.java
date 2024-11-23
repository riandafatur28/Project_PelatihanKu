package com.example.projectpelatihanku;

import static com.example.projectpelatihanku.MainActivity.hideBottomNavigationView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.cardview.widget.CardView;

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.FunctionHelper;
import com.example.projectpelatihanku.helper.JwtHelper;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FragmentProgramRegister extends Fragment {

    private EditText inputNamaDaftar, inputKejuruanDaftar, inputProgramDaftar;
    private TextView textUnduhBukti, textViewKTPFileName, textViewKKFileName, textViewIjazahFileName;
    private Uri uriKtp, uriKk, uriIjazah;
    private static final int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;
    private Button buttonDaftar;
    private ImageView backBtn;
    private CardView cardViewUploadKTP, cardViewUploadKK, cardViewUploadIjazah;
    private String currentFileType;
    private String nomorRegistrasi;
    private int userId;
    private String programId;
    private String programName;
    private String departmentName;
    private String token;
    private String username;
    private String userEmail;
    private String userPhone;
    private String userGender;
    private String userBirth;
    private String userAddress;
    private final String regex = "^[a-zA-Z .,']+$";

    /**
     * Default contructor, gunakan jika tidak perlu mengirim data ke fragment.
     */
    public FragmentProgramRegister() {

    }

    /**
     * Contructor untuk fragment program register, Wajib menggunakan contructor ini jika
     * fungsional terkait pendaftaran program.
     *
     * @param programId      id program yang dipilih.
     * @param programName    nama program yang dipilih.
     * @param departmentName nama kejuruan yang dipilih.
     */
    public FragmentProgramRegister(String programId, String programName, String departmentName) {
        this.programId = programId;
        this.programName = programName;
        this.departmentName = departmentName;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program_register, container, false);

        inputNamaDaftar = view.findViewById(R.id.inputNamaDaftar);
        inputKejuruanDaftar = view.findViewById(R.id.inputKejuruanDaftar);
        inputProgramDaftar = view.findViewById(R.id.inputProgramDaftar);
        buttonDaftar = view.findViewById(R.id.buttonDaftar);
        textUnduhBukti = view.findViewById(R.id.textUnduhBukti);
        textViewKTPFileName = view.findViewById(R.id.textViewKTPFileName);
        textViewKKFileName = view.findViewById(R.id.textViewKKFileName);
        textViewIjazahFileName = view.findViewById(R.id.textViewIjazahFileName);
        cardViewUploadKTP = view.findViewById(R.id.cardViewUploadKTP);
        cardViewUploadKK = view.findViewById(R.id.cardViewUploadKK);
        cardViewUploadIjazah = view.findViewById(R.id.cardViewUploadIjazah);
        backBtn = view.findViewById(R.id.imageArrow);

        token = SharedPreferencesHelper.getToken(getContext());
        JWT jwt = new JWT(token);
        Double userIdDouble = (Double) jwt.getClaim("users").asObject(Map.class).get("id");
        userId = userIdDouble.intValue();
        username = JwtHelper.getUserData(token, "users", "username");
        inputNamaDaftar.setText(username);
        inputProgramDaftar.setText(programName);
        inputKejuruanDaftar.setText(departmentName);

        buttonDaftar.setOnClickListener(v -> {
            registerProgram(new ApiClient());
        });
        FragmentHelper.backNavigation(getActivity(), backBtn, null);

        cardViewUploadKTP.setOnClickListener(v -> requestStoragePermission("KTP"));
        cardViewUploadKK.setOnClickListener(v -> requestStoragePermission("KK"));
        cardViewUploadIjazah.setOnClickListener(v -> requestStoragePermission("Ijazah"));
        hideBottomNavigationView();
        return view;
    }

    /**
     * Meminta izin akses ke penyimpanan untuk memilih file PDF.
     *
     * @param type Jenis file yang dipilih (KTP, KK, atau Ijazah).
     */
    private void requestStoragePermission(String type) {
        currentFileType = type;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            openFilePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToastWithDelay("Izin akses diberikan", 2000);
            } else {
                showToastWithDelay("Izin akses ditolak", 2000);
            }
        }
    }

    /**
     * Membuka dialog file picker untuk memilih file PDF.
     */
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Pilih file " + currentFileType), PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (uri != null) {
                if (currentFileType.equals("KTP")) {
                    uriKtp = uri;
                    textViewKTPFileName.setText(new File(uri.getPath()).getName());
                    textViewKTPFileName.setVisibility(View.VISIBLE);
                } else if (currentFileType.equals("KK")) {
                    uriKk = uri;
                    textViewKKFileName.setText(new File(uri.getPath()).getName());
                    textViewKKFileName.setVisibility(View.VISIBLE);
                } else if (currentFileType.equals("Ijazah")) {
                    uriIjazah = uri;
                    textViewIjazahFileName.setText(new File(uri.getPath()).getName());
                    textViewIjazahFileName.setVisibility(View.VISIBLE);
                }
            }
        } else {
            Toast.makeText(getActivity(), "Gagal memilih file!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Mengecek kevalidan data yang dimasukkan pengguna.
     *
     * @return True jika data valid, false jika tidak.
     * @see FunctionHelper#validateString
     * @see FunctionHelper#validateUri
     */
    private boolean isValidData() {
        boolean isValid = true;
        username = inputNamaDaftar.getText().toString();
        programName = inputProgramDaftar.getText().toString();
        departmentName = inputKejuruanDaftar.getText().toString();

        isValid = FunctionHelper.validateString(getContext(), username, "Nama", regex, 50) && isValid;
        isValid = FunctionHelper.validateString(getContext(), programName, "Program", regex, 50) && isValid;
        isValid = FunctionHelper.validateString(getContext(), departmentName, "Kejuruan", regex, 50) && isValid;
        isValid = FunctionHelper.validateUri(getContext(), uriKtp, "KTP") && isValid;
        isValid = FunctionHelper.validateUri(getContext(), uriKk, "KK") && isValid;
        isValid = FunctionHelper.validateUri(getContext(), uriIjazah, "Ijazah") && isValid;
        return isValid;
    }

    /**
     * Mengirim request registrasi program ke server.
     *
     * @param api Service Class untuk melakukan request ke server.
     */
    private void registerProgram(ApiClient api) {
        if (!isValidData()) {
            return;
        }
        String nama = inputNamaDaftar.getText().toString();
        String program = inputProgramDaftar.getText().toString();

        api.registerProgram(getContext(), token, "/user/registrations/training", userId, nama,
                programId, program,
                uriKtp, uriKk, uriIjazah, new ApiClient.RegistrationsProgramsHelper() {
                    @Override
                    public void onSuccess(String message, String noRegistrasi) {
                        requireActivity().runOnUiThread(() -> {
                            showToastWithDelay(message, 0);
                            nomorRegistrasi = noRegistrasi;
                            fetchUserDatas(new ApiClient());
                        });

                    }

                    @Override
                    public void onFailed(IOException e) {
                        requireActivity().runOnUiThread(() -> {
                            showToastWithDelay(e.getMessage(), 3000);
                        });

                    }
                });
    }

    /**
     * Mengambil data profil pengguna untuk ditampilkan di form bukti pendaftaran.
     *
     * @param api Service Class untuk melakukan request ke server.
     * @see ApiClient#FetchProfile(String, String, ApiClient.ProfileHelper)
     */
    private void fetchUserDatas(ApiClient api) {
        api.FetchProfile(token, "/users/" + userId, new ApiClient.ProfileHelper() {
            @Override
            public void onSuccess(String name, String jk, String ttl, String tlp, String email, String alamat, String imageUri) {
                requireActivity().runOnUiThread(() -> {
                    username = name;
                    userGender = jk;
                    userBirth = ttl;
                    userPhone = tlp;
                    userEmail = email;
                    userAddress = alamat;

                    textUnduhBukti.setVisibility(View.VISIBLE);
                    simpanBuktiPendaftaran();
                });
            }

            @Override
            public void onFailed(IOException e) {
                requireActivity().runOnUiThread(() -> {
                    showToastWithDelay(e.getMessage(), 3000);
                });
            }
        });
    }

    /**
     * Menampilkan Toast dengan custom delay
     *
     * @param message Pesan yang akan ditampilkan
     * @param delay   Delay dalam milidetik
     */
    private void showToastWithDelay(String message, long delay) {
        new Handler().postDelayed(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show(), delay);
    }

    /**
     * Handler metode untuk event ketika tombol "Unduh Bukti" ditekan.
     *
     * @see RegistrationDocument
     */
    private void simpanBuktiPendaftaran() {
        textUnduhBukti.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegistrationDocument.class);
            intent.putExtra("nomorRegistrasi", nomorRegistrasi);
            intent.putExtra("namaPendaftar", username);
            intent.putExtra("tanggalLahir", userBirth);
            intent.putExtra("jenisKelamin", userGender);
            intent.putExtra("nomorHandphone", userPhone);
            intent.putExtra("email", userEmail);
            intent.putExtra("alamat", userAddress);
            intent.putExtra("program", programName);
            intent.putExtra("kejuruan", departmentName);
            startActivity(intent);
        });
    }
}
