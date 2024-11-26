
package com.example.projectpelatihanku;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.api.WebSocketService;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.JwtHelper;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, DashboardFragment.OnDashboardVisibleListener {

    public static BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bottomNavigationView = findViewById(R.id.bottomview);
        bottomNavigationView.setOnItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.navActivity);
            if (currentFragment != null) {
                updateBottomNavigationSelection(currentFragment);
            }
        });

        if (savedInstanceState == null) {
            FragmentHelper.navigateToFragment(this, R.id.navActivity, new FragmentSplash(), true, "splash");
            hideBottomNavigationView();
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.navActivity);

            if (isMainFragment(currentFragment)) {
                showBottomNavigationView();
            } else {
                hideBottomNavigationView();
            }
        }
    }

    /**
     * Memulai layanan WebSocketService untuk menjalankan koneksi WebSocket dengan server.
     * Layanan ini digunakan untuk menerima notifikasi real-time dari server.
     * <p>
     * Metode ini menyiapkan intent untuk memulai WebSocketService dan mengirimkan token autentikasi
     * serta ID pengguna yang diperlukan untuk membuka koneksi WebSocket.
     * <p>
     * Jika perangkat menjalankan versi Android 8.0 (API level 26) atau lebih tinggi, layanan akan dijalankan
     * sebagai layanan foreground menggunakan `startForegroundService()`. Untuk versi Android yang lebih rendah,
     * digunakan `startService()` untuk memulai layanan.
     *
     * @see WebSocketService
     * @see SharedPreferencesHelper#getToken(Context)
     */
    protected void startWebSocketService() {
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        serviceIntent.putExtra("token", SharedPreferencesHelper.getToken(this));
        JWT jwt = new JWT(SharedPreferencesHelper.getToken(this));
        Double userIdDouble = 0.0;
            try {
                userIdDouble = Double.parseDouble((String) jwt.getClaim("users").asObject(Map.class).get("id"));
            } catch (Exception e) {
                String id = JwtHelper.getUserData(SharedPreferencesHelper.getToken(this), "users", "id");
                userIdDouble = Double.parseDouble(id);
            }
        serviceIntent.putExtra("userId", userIdDouble.intValue());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    /**
     * Memperbarui items bottom navigation berdasarkan fragement yang dibuka saat ini.
     *
     * @param currentFragment fragment yang dibuka saat ini
     */
    private void updateBottomNavigationSelection(Fragment currentFragment) {
        int selectedItemId = -1;

        if (currentFragment instanceof DashboardFragment) {
            selectedItemId = R.id.home;
        } else if (currentFragment instanceof FragmentDepartment) {
            selectedItemId = R.id.department;
        } else if (currentFragment instanceof FragmentNotifikasi) {
            selectedItemId = R.id.notifikasi;
        } else if (currentFragment instanceof FragmentProfil) {
            selectedItemId = R.id.user;
        }

        if (selectedItemId != -1 && bottomNavigationView.getSelectedItemId() != selectedItemId) {
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }
    }

    /**
     * Handler untuk item bottom navigation.
     * Digunakan untuk melakukan navigasi ke fragment yang sesuai saat item bottom navigation diklik.
     *
     * @param item item yang diklik
     * @return true jika item diklik berhasil
     * @see FragmentHelper#navigateToFragment(FragmentActivity, int, Fragment, boolean, String)
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        String backStackName = null;

        if (item.getItemId() == R.id.home) {
            selectedFragment = new DashboardFragment();
            backStackName = "dashboard";
        } else if (item.getItemId() == R.id.department) {
            selectedFragment = new FragmentDepartment();
            backStackName = "department";
        } else if (item.getItemId() == R.id.notifikasi) {
            selectedFragment = new FragmentNotifikasi();
            backStackName = "notification";
        } else if (item.getItemId() == R.id.user) {
            selectedFragment = new FragmentProfil();
            backStackName = "profile";
        }

        if (selectedFragment != null) {
            FragmentHelper.navigateToFragment(this, R.id.navActivity, selectedFragment, true, backStackName);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.navActivity);
        if (isMainFragment(currentFragment)) {
            showBottomNavigationView();
        } else {
            hideBottomNavigationView();
        }

        super.onBackPressed();
    }

    /**
     * Mengecek apakah fragment adalah fragment utama (Dashboard, Department, Notifikasi, atau Profil).
     *
     * @param fragment fragment yang akan diperiksa
     * @return true jika fragment adalah fragment utama, false jika tidak
     */
    public boolean isMainFragment(Fragment fragment) {
        return fragment instanceof DashboardFragment ||
                fragment instanceof FragmentDepartment ||
                fragment instanceof FragmentNotifikasi ||
                fragment instanceof FragmentProfil;
    }

    @Override
    public void onDashboardVisible() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    /**
     * Menyembunyikan bottom navigation.
     */
    public static void hideBottomNavigationView() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    /**
     * Menampilkan bottom navigation.
     */
    public static void showBottomNavigationView() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
