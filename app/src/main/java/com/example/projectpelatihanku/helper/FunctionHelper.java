package com.example.projectpelatihanku.helper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Class Helper untuk metode yang digunakan dalam aplikasi
 */
public class FunctionHelper {

    /**
     * Metode Helper untuk memotong String dengan panjang yang ditentukan
     *
     * @param text      String yang akan dipotong
     * @param batasKata Jumlah maksimum kata yang akan ditampilkan
     * @return String yang telah dipotong
     */
    public static String potongString(String text, int batasKata) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        String[] kata = text.split("\\s+");
        if (kata.length <= batasKata) {
            return text;
        }
        return String.join(" ", java.util.Arrays.copyOfRange(kata, 0, batasKata)) + "...";
    }

    /**
     * Metode Helper untuk menampilkan DatePickerDialog
     *
     * @param context      Konteks aplikasi
     * @param inputTanggal EditText untuk menampilkan tanggal
     */
    public static void datePickerHelper(Context context, EditText inputTanggal) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, month, day) -> {
                    inputTanggal.setText(day + "/" + (month + 1) + "/" + year);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    /**
     * Metode Helper untuk validasi String.
     *
     * @param context   Context aplikasi.
     * @param value     String yang akan divalidasi.
     * @param feedback  String feedback yang akan ditampilkan jika validasi gagal.
     * @param regex     Regular Expression yang digunakan untuk validasi.
     * @param maxLength Panjang maksimum yang diperbolehkan.
     * @return `true` jika semua valid, `false` jika terdapat ketidakcocokan.
     */
    public static boolean validateString(Context context, String value, String feedback,
                                         String regex,
                                         int maxLength) {
        if (value == null || value.isEmpty()) {
            Toast.makeText(context, feedback + " Wajib diisi", Toast.LENGTH_LONG).show();
            return false;
        }
        if (value.length() > maxLength) {
            Toast.makeText(context, feedback + " Tidak boleh lebih dari " + maxLength,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!value.matches(regex)) {
            Toast.makeText(context, feedback + " Tidak valid", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Metode Helper untuk validasi Uri.
     *
     * @param context  Context aplikasi
     * @param value    Uri yang akan divalidasi
     * @param feedback String feedback yang akan ditampilkan jika validasi gagal
     * @return `true` jika uri tidak kosong/null dan `false` jika sebaliknya.
     */
    public static boolean validateUri(Context context, Uri value, String feedback) {
        if (value == null) {
            Toast.makeText(context, feedback + " Wajib diupload", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Metode helper untuk mengonversi gambar dari drawable menjadi file dan mengirimkannya sebagai
     * Multipart
     *
     * @param context    Context aplikasi
     * @param resourceId ID gambar di drawable
     * @return MultipartBody.Part untuk gambar yang akan dikirim
     */
    public static MultipartBody.Part convertImageToMultipart(Context context, int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);

        File tempFile = new File(context.getCacheDir(), "temp_image.jpg");
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), tempFile);
        return MultipartBody.Part.createFormData("pas_foto", tempFile.getName(), requestBody);
    }

    /**
     * Mengembalikan file dari URI yang dikirim.
     *
     * @param uri     URI yang akan dikonversi.
     * @param context Context aplikasi.
     * @return File yang dihasilkan dari URI.
     */
    public static File getFileFromUriImage(Uri uri, Context context) {
        File file = null;
        if (uri != null) {

            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String filePath = cursor.getString(columnIndex);
                file = new File(filePath);
                cursor.close();
            }
        }
        return file;
    }

    /**
     * Mengembalikan path dari URI sebagai file sementara di cache aplikasi
     *
     * @param context Context aplikasi
     * @param uri     URI file yang dipilih
     * @return Path file sementara, null jika gagal
     */
    public static File getFileFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                return null;
            }

            File tempFile = new File(context.getCacheDir(), "temp_" + System.currentTimeMillis() + ".pdf");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            Toast.makeText(context, "Gagal mengambil file dari URI", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


}
