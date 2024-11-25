package com.example.projectpelatihanku.Models;

import java.io.Serializable;

public class Department implements Serializable {
    private String id;
    private String nama;
    private String deskripsi;
    private String imageUrl;

    /**
     * Constructor untuk kelas Department
     * @param id id department
     * @param nama nama department
     * @param deskripsi deskripsi department
     * @param imageUrl foto department
     */
    public Department(String id, String nama, String deskripsi, String imageUrl) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imageUrl = imageUrl;
    }

    /**
     * Getter untuk id department
     * @return id department
     */
    public String getId() {
        return id;
    }

    /**
     * Getter untuk nama department
     * @return nama department
     */
    public String getNama() {
        return nama;
    }

    /**
     * Getter untuk deskripsi department
     * @return deskripsi department
     */
    public String getDeskripsi() {
        return deskripsi;
    }

    /**
     * Getter untuk foto department
     * @return foto department
     */
    public String getImageUrl() {
        return imageUrl;
    }
}
