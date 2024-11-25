package com.example.projectpelatihanku.helper;

import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectpelatihanku.MainActivity;

/**
 * Kelas helper untuk navigasi fragment
 */
public class FragmentHelper {

    /**
     * Navigasi ke fragment baru
     *
     * @param activity       FragmentActivity yang memuat fragment.
     * @param containerId    ID layout container (XML) yang akan diganti.
     * @param newFragment    Instance fragment baru yang akan ditampilkan.
     * @param addToBackStack Opsi untuk menambahkan fragment ke back stack,
     *                       set True jika ingin menambahkan ke back stack.
     *                       default option : false
     * @param name           nama backstack, tambahkan jika perlu navigasi secara spesifik,
     *                       default : null.
     */
    public static void navigateToFragment(FragmentActivity activity, int containerId,
                                          Fragment newFragment, boolean addToBackStack,
                                          String name) {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
            transaction.replace(containerId, newFragment);

            if (addToBackStack) {
                if (name != null) {
                    transaction.addToBackStack(name);
                } else {
                    transaction.addToBackStack(null);
                }
            }
            transaction.commit();
        }
    }

    /**
     * Navigasi ke backstack sebelumnya
     *
     * @param activity:            Activity yang sedang aktif.
     *                             Gunakan .this jika berada di activity.
     *                             Gunakan getActivity() jika berada di fragment
     * @param imageView            Image view yang digunakan sebagai button back.
     *                             imageView : null jika tidak digunakan.
     * @param button               Button yang digunakan sebagai button back.
     *                             button : null jika tidak digunakan.
     * @param name                 nama backstack, tambahkan jika perlu navigasi secara spesifik, set null jika
     *                             tidak.
     * @param flag                 Flag yang mengontrol cara fragment di pop dari backstack.
     * @param showBottomNavigation opsi untuk menyembunyikan bottom navigation view saat fragment
     *                             baru ditampilkan, set true jika ingin menyembunyikan.
     */
    public static void backNavigation(FragmentActivity activity, ImageView imageView,
                                      Button button, @Nullable String name, int flag,
                                      @Nullable boolean showBottomNavigation) {
        if (activity == null) return;

        if (imageView != null) {
            imageView.setOnClickListener(v -> backHandler(activity, name, flag, showBottomNavigation));
        } else if (button != null) {
            button.setOnClickListener(v -> backHandler(activity, name, flag, showBottomNavigation));
        } else {
            backHandler(activity, name, flag, showBottomNavigation);
        }
    }

    /**
     * Main method untuk navigasi ke backstack sebelumnya
     *
     * @param activity             FragmentActivity yang sedang aktif.
     * @param name                 nama backstack, tambahkan jika perlu navigasi secara spesifik, set null
     *                             jika tidak.
     * @param flag                 Flag yang mengontrol cara fragment di pop dari backstack.
     * @param showBottomNavigation opsi untuk menyembunyikan bottom navigation view saat fragment
     *                             ditampilkan, set true jika ingin menampilkan.
     */
    private static void backHandler(FragmentActivity activity, @Nullable String name, int flag, @Nullable boolean showBottomNavigation) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
            transaction.commit();

            if (showBottomNavigation) {
                MainActivity.showBottomNavigationView();
            } else {
                MainActivity.hideBottomNavigationView();
            }

            if (name != null) {
                fragmentManager.popBackStack(name, flag);
            } else {
                fragmentManager.popBackStack();
            }
        } else {
            activity.onBackPressed();
        }
    }

    /**
     * Metode Helper untuk handle navigasi kembali tombol back bawaan android
     *
     * @param fragment                 Fragment yang sedang aktif
     * @param hideBottomNavigationView opsi untuk menyembunyikan bottom navigation view saat
     *                                 back, set true jika ingin menyembunyikan.
     */
    public static void backHandlerDefault(Fragment fragment, boolean hideBottomNavigationView) {
        if (fragment == null || fragment.getActivity() == null) return;

        fragment.requireActivity().getOnBackPressedDispatcher().addCallback(
                fragment.getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (hideBottomNavigationView) {
                            MainActivity.hideBottomNavigationView();
                        } else {
                            MainActivity.showBottomNavigationView();
                        }

                        if (fragment.getParentFragmentManager().getBackStackEntryCount() > 0) {
                            fragment.getParentFragmentManager().popBackStack();
                        } else {
                            fragment.requireActivity().onBackPressed();
                        }
                    }
                }
        );
    }
}
