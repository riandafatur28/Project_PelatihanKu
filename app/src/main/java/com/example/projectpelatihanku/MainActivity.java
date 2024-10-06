package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, DashboardFragment.OnDashboardVisibleListener {

    private BottomNavigationView bottomNavigationView;
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private FragmentInstitusi fragmentInstitusi = new FragmentInstitusi();
    private FragmentNotifikasi fragmentNotifikasi = new FragmentNotifikasi();
    private FragmentProfil fragmentProfil = new FragmentProfil();
    private FragmentLogin fragmentLogin = new FragmentLogin();
    private FragmentTentang fragmentTentang = new FragmentTentang();
    private FragmentProgramInstitusi fragmentProgramInstitusi = new FragmentProgramInstitusi();
    private FragmentSplash splashFragment = new FragmentSplash();
    private FragmentRegister fragmentRegister = new FragmentRegister();
    private FragmentLupaSandi fragmentLupaSandi = new FragmentLupaSandi();
    private FragmentDetailProgram fragmentDetailProgram = new FragmentDetailProgram();
    private FragmentPendaftaran fragmentPendaftaran = new FragmentPendaftaran(); // Perbaikan nama variabel



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mengatur agar aplikasi tidak menggunakan tema gelap
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Inisialisasi BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomview);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Tampilkan SplashFragment sebagai fragment awal
        if (savedInstanceState == null) {
            navigateToFragment(splashFragment, false); // Sembunyikan BottomNavigationView saat splash aktif
        } else {
            // Ambil fragment yang aktif dari back stack
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.navActivity);

            // Cek apakah fragment yang aktif adalah salah satu dari fragment utama
            if (isMainFragment(currentFragment)) {
                // Tampilkan BottomNavigationView
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                // Sembunyikan BottomNavigationView
                bottomNavigationView.setVisibility(View.GONE);
            }
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
            navigateToFragment(selectedFragment, true);
            return true;
        }
        return false;
    }

    // Method umum untuk navigasi antar fragment
    private void navigateToFragment(Fragment fragment, boolean showBottomNav) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navActivity, fragment)
                .addToBackStack(null) // Menyimpan transaksi dalam back stack
                .commit();

        // Tampilkan atau sembunyikan BottomNavigationView berdasarkan parameter
        if (showBottomNav) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Ambil fragment yang saat ini aktif
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.navActivity);

        // Cek apakah fragment yang sedang aktif adalah salah satu dari fragment yang butuh menampilkan BottomNavigationView
        if (isMainFragment(currentFragment)) {
            // Tampilkan BottomNavigationView
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            // Sembunyikan BottomNavigationView untuk fragment lainnya
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    // Method ini akan memeriksa apakah fragment saat ini adalah salah satu dari fragment utama
    private boolean isMainFragment(Fragment fragment) {
        return fragment instanceof DashboardFragment ||
                fragment instanceof FragmentInstitusi ||
                fragment instanceof FragmentNotifikasi ||
                fragment instanceof FragmentProfil;
    }

    public void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    // Method untuk navigasi dari FragmentLogin ke Dashboard
    public void navigateToDashboard() {
        // Sembunyikan BottomNavigationView sebelum proses login selesai
        hideBottomNavigation();

        // Pindah ke DashboardFragment setelah login selesai
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navActivity, dashboardFragment)
                .addToBackStack(null)
                .commit();

        // Tampilkan BottomNavigationView segera setelah dashboard tampil
        dashboardFragment.setOnDashboardVisibleListener(new DashboardFragment.OnDashboardVisibleListener() {
            @Override
            public void onDashboardVisible() {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

    // Method untuk navigasi dari SplashFragment ke FragmentLogin
    public void navigateToLogin() {
        navigateToFragment(fragmentLogin, false);
    }

    // Method lain untuk navigasi ke fragment yang berbeda
    public void navigateToInstitusi() {
        navigateToFragment(fragmentInstitusi, true);
    }

    public void navigateToRegister() {
        navigateToFragment(fragmentRegister, false);
    }

    public void navigateToLupaSandi() {
        navigateToFragment(fragmentLupaSandi, false);
    }

    public void navigateToDetailProgram() {
        navigateToFragment(fragmentDetailProgram, false); // Sembunyikan BottomNavigationView saat di FragmentDetailProgram
    }

    public void navigateToTentang() {
        navigateToFragment(fragmentTentang, false);
    }

    public void navigateToProgramInstitusi() {
        navigateToFragment(fragmentProgramInstitusi, false);
    }

    // Navigasi ke FragmentPendaftaran
    public void navigateToPendaftaran() {
        navigateToFragment(fragmentPendaftaran, false);
    }

    @Override
    public void onDashboardVisible() {
        // Tampilkan BottomNavigationView setelah DashboardFragment terlihat
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
