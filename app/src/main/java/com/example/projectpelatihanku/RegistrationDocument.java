package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RegistrationDocument extends AppCompatActivity {

    private TextView tvNama, tvKejuruan, tvProgram, tvTanggalLahir, tvJenisKelamin,
            tvNomorHandphone, tvEmail, tvAlamat, tvNoRegistrasi;
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

        btnSimpan.setOnClickListener( v-> {
            saveDocumentAsPdf();
        });

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

        tvNoRegistrasi.setText(nomorRegistrasi);
        tvNama.setText(namaPendaftar);
        tvTanggalLahir.setText(tanggalLahir);
        tvJenisKelamin.setText(jenisKelamin);
        tvNomorHandphone.setText(nomorHandphone);
        tvEmail.setText(email);
        tvAlamat.setText(alamat);
        tvKejuruan.setText(namaKejuruan);
        tvProgram.setText(namaProgram);
    }

    /**
     * Menyimpan dokumen sebagai PDF
     */
    private void saveDocumentAsPdf() {
        int pageWidth = 595;
        int pageHeight = 1042;

        Bitmap bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layoutDocument.measure(
                View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.UNSPECIFIED)
        );
        layoutDocument.layout(0, 0, layoutDocument.getMeasuredWidth(), layoutDocument.getMeasuredHeight());
        layoutDocument.draw(canvas);

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        page.getCanvas().drawBitmap(bitmap, 0, 0, null);
        pdfDocument.finishPage(page);

        OutputStream outputStream = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "bukti_pendaftaran.pdf");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/BuktiPendaftaran");

                ContentResolver contentResolver = getContentResolver();
                Uri uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
                if (uri != null) {
                    outputStream = contentResolver.openOutputStream(uri);
                }
            } else {
                String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/BuktiPendaftaran";
                File dir = new File(directoryPath);
                if (!dir.exists() && !dir.mkdirs()) {
                    Toast.makeText(this, "Gagal membuat direktori.", Toast.LENGTH_SHORT).show();
                    return;
                }
                File pdfFile = new File(dir, "bukti_pendaftaran.pdf");
                outputStream = new FileOutputStream(pdfFile);
            }

            if (outputStream != null) {
                pdfDocument.writeTo(outputStream);
                pdfDocument.close();
                outputStream.close();
                Toast.makeText(this, "Bukti pendaftaran berhasil disimpan di Documents.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Gagal mendapatkan akses ke penyimpanan.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan bukti pendaftaran", Toast.LENGTH_SHORT).show();
        }
    }


}
