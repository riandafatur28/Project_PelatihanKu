package com.example.projectpelatihanku;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegistrationDocument extends AppCompatActivity {

    private TextView tvNama, tvKejuruan, tvProgram;
    private LinearLayout buttonDownload;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_document); // Ganti dengan nama layout Anda

        // Inisialisasi View
        tvNama = findViewById(R.id.tvNamapendaftar);
        tvKejuruan = findViewById(R.id.tvkejuruan);
        tvProgram = findViewById(R.id.tvprogram);
        buttonDownload = findViewById(R.id.buttonCetak);

        // Ambil data dari intent
        String nama = getIntent().getStringExtra("nama");
        String kejuruan = getIntent().getStringExtra("kejuruan");
        String program = getIntent().getStringExtra("program");

        // Tampilkan data di TextView
        tvNama.setText("Nama: " + nama);
        tvKejuruan.setText("Kejuruan: " + kejuruan);
        tvProgram.setText("Program: " + program);

        // Set OnClickListener untuk tombol download
        buttonDownload.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                downloadBuktiPendaftaran();
            } else {
                requestStoragePermission();
            }
        });
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin akses diberikan", Toast.LENGTH_SHORT).show();
                downloadBuktiPendaftaran();
            } else {
                Toast.makeText(this, "Izin akses ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downloadBuktiPendaftaran() {
        // Membuat objek PdfDocument
        PdfDocument pdfDocument = new PdfDocument();
        // Mengatur ukuran halaman
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Menggambar data ke halaman PDF
        Bitmap bitmap = Bitmap.createBitmap(595, 842, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        View contentView = getLayoutInflater().inflate(R.layout.registration_document, null);
        contentView.measure(View.MeasureSpec.makeMeasureSpec(595, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(842, View.MeasureSpec.EXACTLY));
        contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
        contentView.draw(canvas);

        page.getCanvas().drawBitmap(bitmap, 0, 0, null);
        pdfDocument.finishPage(page);

        // Menyimpan PDF ke penyimpanan
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/BuktiPendaftaran"; // Ganti path sesuai kebutuhan
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String pdfFilePath = directoryPath + "/bukti_pendaftaran.pdf";
        try {
            FileOutputStream outputStream = new FileOutputStream(pdfFilePath);
            pdfDocument.writeTo(outputStream);
            outputStream.close();
            pdfDocument.close();
            Toast.makeText(this, "Bukti pendaftaran berhasil diunduh!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan bukti pendaftaran", Toast.LENGTH_SHORT).show();
        }
    }
}