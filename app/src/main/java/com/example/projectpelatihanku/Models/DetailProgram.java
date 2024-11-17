package com.example.projectpelatihanku.Models;

import java.io.Serializable;

public class DetailProgram implements Serializable {
    private String id;
    private String nama;
    private String deskripsi;
    private String imageUrl;
    private String statusPendaftaran;
    private String tanggalMulai;
    private String tanggalAkhir;
    private String standar;
    private String peserta;
    private String idInstructor;
    private String namaInstructor;
    private String alamatInstructor;
    private String kontakInstructor;
    private String idBuilding;
    private String idDepartment;

    // Constructor
    public DetailProgram(String id, String nama, String deskripsi, String imageUrl, String statusPendaftaran,
                         String tanggalMulai, String tanggalAkhir, String standar, String peserta,
                         String idInstructor, String idBuilding, String idDepartment) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imageUrl = imageUrl;
        this.statusPendaftaran = statusPendaftaran;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
        this.standar = standar;
        this.peserta = peserta;
        this.idInstructor = idInstructor;
        this.namaInstructor = namaInstructor;
        this.alamatInstructor = alamatInstructor;
        this.kontakInstructor = kontakInstructor;
        this.idBuilding = idBuilding;
        this.idDepartment = idDepartment;
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

    public String getStatusPendaftaran() {
        return statusPendaftaran;
    }

    public String getTanggalMulai() {
        return tanggalMulai;
    }

    public String getTanggalAkhir() {
        return tanggalAkhir;
    }

    public String getStandar() {
        return standar;
    }

    public String getPeserta() {
        return peserta;
    }

    public String getIdInstructor() {
        return idInstructor;
    }

    public String getNamaInstructor() {
        return namaInstructor;
    }

    public String getAlamatInstructor() {
        return alamatInstructor;
    }

    public String getKontakInstructor() {
        return kontakInstructor;
    }

    public String getIdBuilding() {
        return idBuilding;
    }

    public String getIdDepartment() {
        return idDepartment;
    }
}
