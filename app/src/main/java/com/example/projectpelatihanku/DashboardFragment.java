package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.imageview.ShapeableImageView;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

    private static final String PREFS_NAME = "UserPrefs";
    static final String KEY_USER_NAME = "user_name";
    private ShapeableImageView imageUser;
    private GoogleMap mMap;
    private LatLng initialLocation = new LatLng(-7.600671315258253, 111.88837430296729);
    private ImageView imagePage;
    private int[] images = {R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3};
    private int currentIndex = 0;
    private GestureDetector gestureDetector;
    private OnDashboardVisibleListener listener;
    private String name;
    private Handler handler;
    private final int CAROUSEL_DELAY = 2000; // 2 detik

    public void setName(String name) {
        this.name = name;
    }

    public DashboardFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardVisibleListener) {
            listener = (OnDashboardVisibleListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnDashboardVisibleListener");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Inisialisasi komponen UI
        imageUser = view.findViewById(R.id.imageUser);
        TextView salamText = view.findViewById(R.id.salamText);
        imagePage = view.findViewById(R.id.imagepage);

        // Mengambil data pengguna dari SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        salamText.setText("Halo, " + FragmentLogin.firstName);  // Menampilkan nama pengguna

        String imageUriString = sharedPreferences.getString("image_uri", null);

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            try {
                imageUser.setImageURI(imageUri);
            } catch (Exception e) {
                imageUser.setImageResource(R.drawable.gambar_user);  // Jika ada error, tampilkan default
                e.printStackTrace();
            }
        } else {
            String gender = sharedPreferences.getString("gender", "Tidak diketahui");
            if ("Laki-laki".equals(gender)) {
                imageUser.setImageResource(R.drawable.img_men);
            } else if ("Perempuan".equals(gender)) {
                imageUser.setImageResource(R.drawable.img_women);
            } else {
                imageUser.setImageResource(R.drawable.gambar_user);
            }
        }

        // Inisialisasi peta
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Mulai carousel otomatis
        handler = new Handler(Looper.getMainLooper());
        startAutoSlide();

        return view;
    }

    // Fungsi untuk menjalankan carousel otomatis
    private void startAutoSlide() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ganti gambar ke yang berikutnya
                currentIndex++;
                if (currentIndex >= images.length) {
                    currentIndex = 0; // Reset ke awal jika sudah mencapai akhir
                }
                imagePage.setImageResource(images[currentIndex]);

                // Jalankan lagi setiap 2 detik
                handler.postDelayed(this, CAROUSEL_DELAY);
            }
        }, CAROUSEL_DELAY);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null); // Hentikan handler saat fragment dihancurkan
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15));
        mMap.addMarker(new MarkerOptions()
                .position(initialLocation)
                .title("Balai Latihan Kerja Nganjuk"));
        mMap.setOnMapLongClickListener(latLng -> {
            String uri = String.format("geo:%f,%f?q=%f,%f(Balai+Latihan+Kerja+Nganjuk)",
                    initialLocation.latitude, initialLocation.longitude,
                    initialLocation.latitude, initialLocation.longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
    }

    public interface OnDashboardVisibleListener {
        void onDashboardVisible();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null) {
            listener.onDashboardVisible();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
