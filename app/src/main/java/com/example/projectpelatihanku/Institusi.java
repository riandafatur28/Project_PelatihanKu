package com.example.projectpelatihanku;

import java.io.Serializable;

public class Institusi implements Serializable {
    private String id; // Menambahkan ID untuk institusi
    private String nama;
    private String deskripsi;
    private String imageUrl;

    public Institusi(String id, String nama, String deskripsi, String imageUrl) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
