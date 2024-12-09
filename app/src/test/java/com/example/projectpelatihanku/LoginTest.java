package com.example.projectpelatihanku;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    @Before
    public void setup() {
        System.out.println("Tes dimulai...");
    }

    @After
    public void teardown() {
        System.out.println("Tes selesai...");
    }

    // 1. Memeriksa sistem ketika email tidak diisi atau kosong
    @Test
    public void validateIsNotEmptyEmail() {
        String email = ""; // Email kosong
        boolean isValid = isEmailValid(email);
        System.out.println("Hasil validasi email kosong: " + isValid);
        assertFalse("Email tidak boleh kosong", isValid);
    }

    // 2. Memeriksa sistem ketika email yang diinputkan menggunakan domain selain @gmail.com
    @Test
    public void validateEmailWithNonGmailDomain() {
        String email = "fajarfadhilah@yahoo.com"; // Domain selain gmail.com
        boolean isValid = isEmailValid(email);
        System.out.println("Hasil validasi email dengan domain selain @gmail.com: " + isValid);
        assertFalse("Email harus menggunakan domain @gmail.com", isValid);
    }

    // 3. Memeriksa sistem ketika password tidak diisi atau kosong
    @Test
    public void validateIsNotEmptyPassword() {
        String password = ""; // Password kosong
        boolean isValid = isPasswordValid(password);
        System.out.println("Hasil validasi password kosong: " + isValid);
        assertFalse("Password tidak boleh kosong", isValid);
    }

    // Fungsi validasi email (menambahkan logika validasi untuk email)
    private boolean isEmailValid(String email) {
        if (email.isEmpty()) {
            return false; // Email kosong, tidak valid
        }
        return email.endsWith("@gmail.com");
    }

    // Fungsi validasi password (menambahkan logika validasi untuk password)
    private boolean isPasswordValid(String password) {
        return !password.isEmpty();
    }
}
