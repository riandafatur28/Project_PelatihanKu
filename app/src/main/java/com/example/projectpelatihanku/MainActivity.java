package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private FragmentInstitusi fragmentInstitusi = new FragmentInstitusi();
    private FragmentNotifikasi fragmentNotifikasi = new FragmentNotifikasi();
    private FragmentProfil fragmentProfil = new FragmentProfil();
    private FragmentLogin fragmentLogin = new FragmentLogin();
    private FragmentProgramInstitusi fragmentProgramInstitusi = new FragmentProgramInstitusi();
    private FragmentSplash splashFragment = new FragmentSplash();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomview);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Tampilkan SplashFragment sebagai fragment awal
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navActivity, splashFragment)
                    .commit();
            bottomNavigationView.setVisibility(View.GONE); // Sembunyikan BottomNavigationView saat splash aktif
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        // Menentukan fragment yang akan ditampilkan berdasarkan item yang dipilih
        if (item.getItemId() == R.id.home) {
            selectedFragment = dashboardFragment;
        } else if (item.getItemId() == R.id.institusi) {
            selectedFragment = fragmentInstitusi;
        } else if (item.getItemId() == R.id.notifikasi) {
            selectedFragment = fragmentNotifikasi;
        } else if (item.getItemId() == R.id.user) {
            selectedFragment = fragmentProfil;
        }

        // Mengganti fragment jika tidak null
        if (selectedFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navActivity, selectedFragment)
                    .commit();
            return true;
        }
        return false;
    }

    // Metode untuk navigasi ke FragmentProgramInstitusi
    public void navigateToProgramInstitusi() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navActivity, fragmentProgramInstitusi)
                .addToBackStack(null)
                .commit();
    }

    // Metode untuk navigasi dari FragmentLogin ke Dashboard
    public void navigateToDashboard() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navActivity, dashboardFragment)
                .commit();
        bottomNavigationView.setVisibility(View.VISIBLE); // Tampilkan BottomNavigationView setelah navigasi
    }

    // Metode untuk navigasi ke FragmentInstitusi
    public void navigateToInstitusi() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navActivity, fragmentInstitusi)
                .addToBackStack(null)
                .commit();
    }

    // Metode untuk navigasi ke FragmentLogin
    public void navigateToLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navActivity, fragmentLogin)
                .commit();
    }
}
