package com.example.projectpelatihanku;

import java.io.Serializable;

public class Program implements  Serializable{
    private String id;
    private String nama;
    private String deskripsi;
    private String imageUrl;
    private String departmentId;

    // Constructor
    public Program(String id, String nama, String deskripsi, String imageUrl) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imageUrl = imageUrl;
        this.departmentId = departmentId;
    }

    // Getters
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

    public String getDepartmentId() {
        return departmentId;
    }
}
