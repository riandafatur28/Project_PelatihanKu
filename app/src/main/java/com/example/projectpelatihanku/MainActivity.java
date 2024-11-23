
package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, DashboardFragment.OnDashboardVisibleListener {

    public static BottomNavigationView bottomNavigationView;
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private FragmentDepartment fragmentDepartment = new FragmentDepartment();
    private FragmentNotifikasi fragmentNotifikasi = new FragmentNotifikasi();
    private FragmentProfil fragmentProfil = new FragmentProfil();
    private FragmentLogin fragmentLogin = new FragmentLogin();
    private FragmentInstitute fragmentInstitute = new FragmentInstitute();
    private FragmentProgram fragmentProgram = new FragmentProgram();
    private FragmentSplash splashFragment = new FragmentSplash();
    private FragmentRegister fragmentRegister = new FragmentRegister();
    private FragmentForgotPassword fragmentForgotPassword = new FragmentForgotPassword();
    private FragmentDetailProgram fragmentDetailProgram = new FragmentDetailProgram();
    private FragmentProgramRegister fragmentProgramRegister = new FragmentProgramRegister();
    private FragmentOTP fragmentOTP = new FragmentOTP();
    private FragmentResetPassword fragmentResetPassword = new FragmentResetPassword();
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
        } else if (item.getItemId() == R.id.department) {
            selectedFragment = fragmentDepartment;
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
                fragment instanceof FragmentDepartment ||
                fragment instanceof FragmentNotifikasi ||
                fragment instanceof FragmentProfil;
    }

    public static void hideBottomNavigation() {
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

    public void navigateToDepartment() {
        navigateToFragment(fragmentDepartment, true);
    }

    public void navigateToRegister() {
        navigateToFragment(fragmentRegister, false);
    }

    public void navigateToForgotPassword() {
        navigateToFragment(fragmentForgotPassword, false);
    }

    public void navigateToDetailProgram() {
        navigateToFragment(fragmentDetailProgram, false);
    }


    public void navigateToInstitute() {
        navigateToFragment(fragmentInstitute, false);
    }

    public void navigateToProgram() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Mengecek apakah FragmentProgram sudah ada dan sedang ditampilkan
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (!(currentFragment instanceof FragmentProgram)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new FragmentProgram())
                    .commit();

            // Menyembunyikan BottomNavigationView setelah mengganti fragment
            hideBottomNavigation();
        }
    }

    public void navigateToProgramRegister() {
        navigateToFragment(fragmentProgramRegister, false);
    }

    @Override
    public void onDashboardVisible() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    // Metode navigateToOTP yang memanggil navigateToFragment
    public void navigateToOTP() {
        Fragment fragmentOTP = new FragmentOTP(); // Pastikan Anda telah membuat FragmentOTP
        navigateToFragment(fragmentOTP, false);
    }
    public void navigateToResetPassword() {
        navigateToFragment(fragmentResetPassword, false);
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomview);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public static void hideBottomNavigationView() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public static void showBottomNavigationView() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
