package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.projectpelatihanku.helper.FragmentHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationDocument extends AppCompatActivity {

    private TextView tvNama, tvKejuruan, tvProgram, tvTanggalLahir, tvJenisKelamin,
            tvNomorHandphone, tvEmail, tvAlamat, tvNoRegistrasi, tvWaktuPendaftaran;
    private Button btnSimpan;
    private View layoutDocument;
    private String nomorRegistrasi, namaPendaftar, tanggalLahir, jenisKelamin, nomorHandphone,
            email, alamat, namaProgram, namaKejuruan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_document);

        tvNoRegistrasi = findViewById(R.id.tvidpeserta);
        tvNama = findViewById(R.id.tvNamapendaftar);
        tvTanggalLahir = findViewById(R.id.tvTanggalLahir);
        tvJenisKelamin = findViewById(R.id.tvjenisKelamin);
        tvNomorHandphone = findViewById(R.id.tvnotelp);
        tvEmail = findViewById(R.id.tvemail);
        tvAlamat = findViewById(R.id.tvidalamat);
        tvKejuruan = findViewById(R.id.tvkejuruan);
        tvProgram = findViewById(R.id.tvprogram);
        layoutDocument = findViewById(R.id.containerBukti);
        btnSimpan = findViewById(R.id.btnSimpan);
        tvWaktuPendaftaran = findViewById(R.id.tvwaktupendaftaran);

        Intent intent = getIntent();
        nomorRegistrasi = intent.getStringExtra("nomorRegistrasi");
        namaPendaftar = intent.getStringExtra("namaPendaftar");
        tanggalLahir = intent.getStringExtra("tanggalLahir");
        jenisKelamin = intent.getStringExtra("jenisKelamin");
        nomorHandphone = intent.getStringExtra("nomorHandphone");
        email = intent.getStringExtra("email");
        alamat = intent.getStringExtra("alamat");
        namaProgram = intent.getStringExtra("program");
        namaKejuruan = intent.getStringExtra("kejuruan");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String tglPendaftaran = format.format(new Date());

        tvNoRegistrasi.setText(nomorRegistrasi);
        tvNama.setText(namaPendaftar);
        tvTanggalLahir.setText(tanggalLahir);
        tvJenisKelamin.setText(jenisKelamin);
        tvNomorHandphone.setText(nomorHandphone);
        tvEmail.setText(email);
        tvAlamat.setText(alamat);
        tvKejuruan.setText(namaKejuruan);
        tvProgram.setText(namaProgram);
        tvWaktuPendaftaran.setText(tglPendaftaran);

        btnSimpan.setOnClickListener(v -> {
            saveDocumentAsPdf();
        });
    }

    /**
     * Menyimpan dokumen sebagai PDF
     *
     * @see FragmentHelper#backNavigation(FragmentActivity, ImageView, Button, String, int, boolean)
     */
    private void saveDocumentAsPdf() {
        int pageWidth = 595;
        int pageHeight = 842;

        layoutDocument.measure(View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layoutDocument.layout(0, 0, layoutDocument.getMeasuredWidth(), layoutDocument.getMeasuredHeight());

        int totalHeight = layoutDocument.getMeasuredHeight();

        PdfDocument pdfDocument = new PdfDocument();

        int currentPageTop = 0;
        int pageNumber = 1;
        while (currentPageTop < totalHeight) {
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas pageCanvas = page.getCanvas();
            pageCanvas.translate(0, -currentPageTop);
            layoutDocument.draw(pageCanvas);

            pdfDocument.finishPage(page);

            currentPageTop += pageHeight;
            pageNumber++;
        }

        try {
            String filePath = getFilePath("bukti_pendaftaran.pdf");
            if (filePath != null && filePath.startsWith("content://")) {
                Uri uri = Uri.parse(filePath);
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                    if (outputStream != null) {
                        pdfDocument.writeTo(outputStream);
                        pdfDocument.close();
                        Toast.makeText(this, "PDF berhasil disimpan!", Toast.LENGTH_SHORT).show();
                        FragmentHelper.backNavigation(this, null, null, "programDetail", 0, false);
                    }
                }
            } else {
                File file = new File(filePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    pdfDocument.writeTo(fos);
                    pdfDocument.close();
                    Toast.makeText(this, "PDF berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    FragmentHelper.backNavigation(this, null, null, "programDetail", 0, false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan PDF", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Mendapatkan path uri file PDF
     *
     * @param fileName Nama file PDF
     * @return Path uri file PDF
     */
    private String getFilePath(String fileName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/BuktiPendaftaran");

            Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

            if (uri != null) {
                return uri.toString();
            } else {
                return null;
            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory(), "Documents/BuktiPendaftaran");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            return new File(directory, fileName).getAbsolutePath();
        }
    }
}
