package com.example.projectpelatihanku.helper;

import android.widget.Button;
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
     * @param activity: Activity yang sedang aktif.
     *                  Gunakan .this jika berada di activity.
     *                  Gunakan getActivity() jika berada di fragment
     * @param imageView Image view yang digunakan sebagai button back.
     *                  imageView : null jika tidak digunakan.
     * @param button    Button yang digunakan sebagai button back.
     *                  button : null jika tidak digunakan.
     */
    public static void backNavigation(FragmentActivity activity, ImageView imageView,
                                      Button button) {
        if (activity == null) return;

        if (imageView != null) {
            imageView.setOnClickListener(v -> backHandler(activity));
        } else if (button != null) {
            button.setOnClickListener(view -> backHandler(activity));
        }
    }

    /**
     * Main method untuk navigasi ke backstack sebelumnya
     *
     * @param activity FragmentActivity yang sedang aktif.
     */
    private static void backHandler(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
            );
            transaction.commit();
            fragmentManager.popBackStack();
        } else {
            activity.onBackPressed();
        }
    }
}
