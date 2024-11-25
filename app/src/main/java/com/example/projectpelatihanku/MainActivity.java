
package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.projectpelatihanku.helper.FragmentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

    public static void hideBottomNavigationView() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public static void showBottomNavigationView() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
