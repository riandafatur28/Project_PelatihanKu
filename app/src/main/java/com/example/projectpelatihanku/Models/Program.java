package com.example.projectpelatihanku.Models;

import java.io.Serializable;

public class Program implements  Serializable{
    private String id;
    private String nama;
    private String deskripsi;
    private String imageUrl;
    private String departmentId;

    /**
     * Constructor untuk kelas Program
     * @param id Id program
     * @param nama Nama program
     * @param deskripsi Deskripsi program
     * @param imageUrl URL gambar program
     */
    public Program(String id, String nama, String deskripsi, String imageUrl) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imageUrl = imageUrl;
        this.departmentId = departmentId;
    }

    /**
     * Getter untuk mengambil id program
     * @return id program
     */
    public String getId() {
        return id;
    }

    /**
     * Getter untuk mengambil nama program
     * @return nama program
     */
    public String getNama() {
        return nama;
    }

    /**
     * Getter untuk mengambil deskripsi program
     * @return deskripsi program
     */
    public String getDeskripsi() {
        return deskripsi;
    }

    /**
     * Getter untuk mengambil URL gambar program
     * @return URL gambar program
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Getter untuk mengambil id department
     * @return id department
     */
    public String getDepartmentId() {
        return departmentId;
    }
}
