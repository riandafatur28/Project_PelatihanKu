package com.example.projectpelatihanku.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.projectpelatihanku.R;

/**
 * Helper class untuk menggunakan Library Glide
 */
public class GlideHelper {

    /**
     * Helper Metode untuk menampilkan gambar dari URL ke ImageView
     * Ganti Placeholder dengan gambar yang diinginkan
     *
     * @param context   konteks aplikasi
     * @param imageView ImageView yang akan diisi gambarnya
     * @param url       URL gambar yang akan ditampilkan
     */
    public static void loadImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.balai_img)
                .into(imageView);
    }
}
