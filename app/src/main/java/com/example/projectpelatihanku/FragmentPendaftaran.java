package com.example.projectpelatihanku;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import java.io.File;

public class FragmentPendaftaran extends Fragment {

    private EditText inputNamaDaftar, inputKejuruanDaftar, inputProgramDaftar;
    private TextView textUnduhBukti, textViewKTPFileName, textViewKKFileName, textViewIjazahFileName;
    private Uri uriKtp, uriKk, uriIjazah;
    private static final int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendaftaran, container, false);

        // Initialize components
        inputNamaDaftar = view.findViewById(R.id.inputNamaDaftar);
        inputKejuruanDaftar = view.findViewById(R.id.inputKejuruanDaftar);
        inputProgramDaftar = view.findViewById(R.id.inputProgramDaftar);
        Button buttonDaftar = view.findViewById(R.id.buttonDaftar);
        textUnduhBukti = view.findViewById(R.id.textUnduhBukti);
        textViewKTPFileName = view.findViewById(R.id.textViewKTPFileName);
        textViewKKFileName = view.findViewById(R.id.textViewKKFileName);
        textViewIjazahFileName = view.findViewById(R.id.textViewIjazahFileName);
        CardView cardViewUploadKTP = view.findViewById(R.id.cardViewUploadKTP);
        CardView cardViewUploadKK = view.findViewById(R.id.cardViewUploadKK);
        CardView cardViewUploadIjazah = view.findViewById(R.id.cardViewUploadIjazah);

        // Back navigation
        ImageView imageArrow = view.findViewById(R.id.imageArrow);
        imageArrow.setOnClickListener(v -> navigateBackDetailProgram());

        // Registration logic
        buttonDaftar.setOnClickListener(v -> {
            String nama = inputNamaDaftar.getText().toString();
            String kejuruan = inputKejuruanDaftar.getText().toString();
            String program = inputProgramDaftar.getText().toString();

            if (!nama.isEmpty() && !kejuruan.isEmpty() && !program.isEmpty() && uriKtp != null && uriKk != null && uriIjazah != null) {
                textUnduhBukti.setVisibility(View.VISIBLE);
            }
        });

        // Upload buttons
        cardViewUploadKTP.setOnClickListener(v -> requestStoragePermission("KTP"));
        cardViewUploadKK.setOnClickListener(v -> requestStoragePermission("KK"));
        cardViewUploadIjazah.setOnClickListener(v -> requestStoragePermission("Ijazah"));

        return view;
    }

    private void navigateBackDetailProgram() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.navigateToDetailProgram();
        }
    }

    private void requestStoragePermission(String type) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            openFilePicker(type);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Izin akses diberikan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Izin akses ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openFilePicker(String type) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Pilih file " + type), PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String filePath = uri.getPath();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (file.length() <= 1 * 1024 * 1024) { // Max 1MB
                        handleFileUpload(uri, data.getDataString());
                    } else {
                        textUnduhBukti.setText("Ukuran file tidak boleh lebih dari 1 MB");
                        textUnduhBukti.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void handleFileUpload(Uri uri, String dataString) {
        String fileName = uri.getLastPathSegment(); // Extract file name

        if (dataString.contains("KTP")) {
            uriKtp = uri;
            textViewKTPFileName.setText(fileName); // Show file name for KTP
        } else if (dataString.contains("KK")) {
            uriKk = uri;
            textViewKKFileName.setText(fileName); // Show file name for KK
        } else if (dataString.contains("Ijazah")) {
            uriIjazah = uri;
            textViewIjazahFileName.setText(fileName); // Show file name for Ijazah
        }
    }
}
