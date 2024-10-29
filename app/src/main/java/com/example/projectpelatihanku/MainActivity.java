package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, DashboardFragment.OnDashboardVisibleListener {

    BottomNavigationView bottomNavigationView;
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private FragmentInstitusi fragmentInstitusi = new FragmentInstitusi();
    private FragmentNotifikasi fragmentNotifikasi = new FragmentNotifikasi();
    private FragmentProfil fragmentProfil = new FragmentProfil();
    private FragmentLogin fragmentLogin = new FragmentLogin();
    private FragmentTentang fragmentTentang = new FragmentTentang();
//    private FragmentProgramInstitusi fragmentProgramInstitusi = new FragmentProgramInstitusi();
    private FragmentSplash splashFragment = new FragmentSplash();
    private FragmentRegister fragmentRegister = new FragmentRegister();
    private FragmentLupaSandi fragmentLupaSandi = new FragmentLupaSandi();
//    private FragmentDetailProgram fragmentDetailProgram = new FragmentDetailProgram();
    private FragmentPendaftaran fragmentPendaftaran = new FragmentPendaftaran();
    private FragmentOTP fragmentOTP = new FragmentOTP();
    private FragmentUbahSandi fragmentUbahSandi = new FragmentUbahSandi();
    private FragmentConfirmPassword fragmentConfirmPassword = new FragmentConfirmPassword();
    private FragmentProfil getFragmentProfil = new FragmentProfil();
    private FragmentEditProfil fragmentEditProfil = new FragmentEditProfil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bottomNavigationView = findViewById(R.id.bottomview);
        bottomNavigationView.setOnItemSelectedListener(this);

        if (savedInstanceState == null) {
            navigateToFragment(splashFragment, false);
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.navActivity);

            if (isMainFragment(currentFragment)) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.home) {
            selectedFragment = dashboardFragment;
        } else if (item.getItemId() == R.id.institusi) {
            selectedFragment = fragmentInstitusi;
        } else if (item.getItemId() == R.id.notifikasi) {
            selectedFragment = fragmentNotifikasi;
        } else if (item.getItemId() == R.id.user) {
            selectedFragment = fragmentProfil;
        }

        if (selectedFragment != null) {
            navigateToFragment(selectedFragment, true);
            return true;
        }
        return false;
    }

    private void navigateToFragment(Fragment fragment, boolean showBottomNav) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.navActivity, fragment)
                .addToBackStack(null)
                .commit();

        if (showBottomNav) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.navActivity);

        if (isMainFragment(currentFragment)) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    private boolean isMainFragment(Fragment fragment) {
        return fragment instanceof DashboardFragment ||
                fragment instanceof FragmentInstitusi ||
                fragment instanceof FragmentNotifikasi ||
                fragment instanceof FragmentProfil;
    }

    public void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void navigateToDashboard() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navActivity, dashboardFragment)
                .addToBackStack(null)
                .commit();

        bottomNavigationView.setSelectedItemId(R.id.home); // Ubah ID sesuai dengan item Dashboard
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void navigateToLogin() {
        hideBottomNavigation();
        navigateToFragment(fragmentLogin, false);
    }

    public void navigateToInstitusi() {
        navigateToFragment(fragmentInstitusi, true);
    }

    public void navigateToRegister() {
        navigateToFragment(fragmentRegister, false);
    }

    public void navigateToLupaSandi() {
        navigateToFragment(fragmentLupaSandi, false);
    }

//    public void navigateToDetailProgram() {
//        navigateToFragment(fragmentDetailProgram, false);
//    }

    public void navigateToTentang() {
        navigateToFragment(fragmentTentang, false);
    }

//    public void navigateToProgramInstitusi() {
//        FragmentProgramInstitusi fragment = new FragmentProgramInstitusi();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .addToBackStack(null)
//                .commit();
//    }


    public void navigateToPendaftaran() {
        navigateToFragment(fragmentPendaftaran, false);
    }

    @Override
    public void onDashboardVisible() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void navigateToOTP() {
        navigateToFragment(fragmentOTP, false);
    }

    public void navigateToUbahSandi() {
        navigateToFragment(fragmentUbahSandi, false);
    }

    public void navigateToConfirmPassword() {
        navigateToFragment(fragmentConfirmPassword, false);
    }

    public void navigateToProfil() {
        navigateToFragment(getFragmentProfil, false);
    }

    public void navigateToEditProfil() {
        navigateToFragment(fragmentEditProfil, false);
    }

    public void showBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomview); // Sesuaikan ID BottomNavigationView
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void onLoginSuccess() {
        navigateToDashboard();
        showBottomNavigation();
    }


    public void hideBottomNavigationView() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomNavigationView() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}