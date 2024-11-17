package com.example.projectpelatihanku.helper;

import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
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
     * @param activity:     Activity yang sedang aktif
     *                      Activity class : this,
     *                      Fragment class : getActivity()
     * @param imageView Image view digunakan sebagai button back
     */
    public static void handleBackButton(FragmentActivity activity, ImageView imageView) {
        if (activity == null) return;
        imageView.setOnClickListener(v -> {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                activity.onBackPressed();
            }
        });
    }
}
