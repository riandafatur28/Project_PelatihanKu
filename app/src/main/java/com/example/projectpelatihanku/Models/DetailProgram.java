package com.example.projectpelatihanku.Models;

import java.io.Serializable;

public class DetailProgram implements Serializable {
    private String id;
    private String nama;
    private String statusPendaftaran;
    private String tanggalMulai;
    private String tanggalAkhir;
    private String standar;
    private String peserta;
    private String deskripsi;
    private String programImageUri;
    private String instructorId;
    private String buildingId;
    private String departmentId;
    private String instructorName;
    private String instructorAddress;
    private String instructorContact;
    private String instructorImageUri;
    private String buildingName;
    private String departmentName;

    /**
     * Constructor DetailProgram
     * @param id Id Program
     * @param nama Nama Program
     * @param statusPendaftaran Status Pendaftaran Program
     * @param tanggalMulai Tanggal Mulai Pendaftaran Program
     * @param tanggalAkhir Tanggal Akhir Pendaftaran Program
     * @param standar Standar Program
     * @param peserta Jumlah Peserta Program
     * @param deskripsi Deskripsi Program
     * @param programImageUri Gambar Program
     * @param instructorId Id Instructor Program
     * @param buildingId Id Gedung Program
     * @param departmentId Id Kejuruan Program
     * @param instructorName Nama Instructor Program
     * @param instructorAddress Alamat Instructor Program
     * @param instructorContact Kontak Instructor Program
     * @param instructorImageUri Gambar Instructor Program
     * @param buildingName Nama Gedung Program
     * @param departmentName Nama Kejuruan Program
     */
    public DetailProgram(String id, String nama, String statusPendaftaran, String tanggalMulai, String tanggalAkhir, String standar, String peserta, String deskripsi, String programImageUri, String instructorId, String buildingId, String departmentId, String instructorName, String instructorAddress, String instructorContact, String instructorImageUri, String buildingName, String departmentName) {
        this.id = id;
        this.nama = nama;
        this.statusPendaftaran = statusPendaftaran;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
        this.standar = standar;
        this.peserta = peserta;
        this.deskripsi = deskripsi;
        this.programImageUri = programImageUri;
        this.instructorId = instructorId;
        this.buildingId = buildingId;
        this.departmentId = departmentId;
        this.instructorName = instructorName;
        this.instructorAddress = instructorAddress;
        this.instructorContact = instructorContact;
        this.instructorImageUri = instructorImageUri;
        this.buildingName = buildingName;
        this.departmentName = departmentName;
    }

    /**
     * Getter Id Program
     * @return Id Program
     */
    public String getId() {
        return id;
    }

    /**
     * Getter Nama Program
     * @return Nama Program
     */
    public String getNama() {
        return nama;
    }

    /**
     * Getter Status Pendaftaran Program
     * @return Status Pendaftaran Program (Dibuka/Ditutup)
     */
    public String getStatusPendaftaran() {
        return statusPendaftaran;
    }

    /**
     * Getter Tanggal Mulai Pendaftaran Program
     * @return Tanggal Mulai Pendaftaran Program
     */
    public String getTanggalMulai() {
        return tanggalMulai;
    }

    /**
     * Getter Tanggal Akhir Pendaftaran Program
     * @return Tanggal Akhir Pendaftaran Program
     */
    public String getTanggalAkhir() {
        return tanggalAkhir;
    }

    /**
     * Getter Standar Program
     * @return Standar Program
     */
    public String getStandar() {
        return standar;
    }

    /**
     * Getter Jumlah Peserta Program
     * @return Jumlah Peserta Program
     */
    public String getPeserta() {
        return peserta;
    }

    /**
     * Getter Deskripsi Program
     * @return Deskripsi Program
     */
    public String getDeskripsi() {
        return deskripsi;
    }

    /**
     * Getter Gambar Program
     * @return Gambar Program
     */
    public String getProgramImageUri() {
        return programImageUri;
    }

    /**
     * Getter Id Instructor Program
     * @return Id Instructor Program
     */
    public String getInstructorId() {
        return instructorId;
    }

    /**
     * Getter Id Gedung Program
     * @return Id Gedung Program
     */
    public String getBuildingId() {
        return buildingId;
    }

    /**
     * Getter Id Department Program
     * @return Id Department Program
     */
    public String getDepartmentId() {
        return departmentId;
    }

    /**
     * Getter Nama Instructor Program
     * @return Nama Instructor Program
     */
    public String getInstructorName() {
        return instructorName;
    }

    /**
     * Getter Alamat Instructor Program
     * @return Alamat Instructor Program
     */
    public String getInstructorAddress() {
        return instructorAddress;
    }

    /**
     * Getter Kontak Instructor Program
     * @return Kontak Instructor Program
     */
    public String getInstructorContact() {
        return instructorContact;
    }

    /**
     * Getter Gambar/Foto Profile Instructor Program
     * @return Gambar/Foto Profile Instructor Program
     */
    public String getInstructorImageUri() {
        return instructorImageUri;
    }

    /**
     * Getter Nama Gedung Program
     * @return Nama Gedung Program
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * Getter Nama Department Program
     * @return Nama Department Program
     */
    public String getDepartmentName() {
        return departmentName;
    }
}
