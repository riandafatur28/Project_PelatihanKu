package com.example.projectpelatihanku;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
public class ImageUtils {

        // Simpan gambar dalam bentuk Base64 ke SharedPreferences
        public static void saveImageToPreferences(Context context, String imageName) {
            byte[] imageBytes = convertImageToByteArray(context, imageName);
            if (imageBytes != null) {
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_profile_image", encodedImage);
                editor.apply();
            }
        }

        // Ubah gambar menjadi byte array
        private static byte[] convertImageToByteArray(Context context, String imageName) {
            int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            if (imageResId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResId);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
            return null;
        }

        // Ambil gambar dari SharedPreferences
        public static Bitmap getImageFromPreferences(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String encodedImage = sharedPreferences.getString("user_profile_image", null);

            if (encodedImage != null) {
                byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            }
            return null; // Jika tidak ada gambar, kembalikan null
        }
    

}
