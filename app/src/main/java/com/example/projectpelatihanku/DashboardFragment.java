package com.example.projectpelatihanku;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

    private static final String PREFS_NAME = "UserPrefs";
    static final String KEY_USER_NAME = "username";

    private OnDashboardVisibleListener listener; // Tambahkan listener

    private GoogleMap mMap;
    private LatLng initialLocation = new LatLng(-7.60067, 111.88837); // Lokasi Balai Latihan Kerja Nganjuk
    private ImageView imageView;
    private int[] images = {R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3}; // Array gambar
    private int currentIndex = 0;

    private GestureDetector gestureDetector; // Tambahkan GestureDetector

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Retrieve the user's name from shared preferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(KEY_USER_NAME, "User"); // Default to "User" if not found

        // Set the greeting text
        TextView salamText = view.findViewById(R.id.salamText);
        salamText.setText("Halo, " + userName);

        // Initialize ImageView
        imageView = view.findViewById(R.id.imagepage);
        imageView.setImageResource(images[currentIndex]); // Set gambar pertama

        // Inisialisasi SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set up button for left navigation
        ImageButton btnLeft = view.findViewById(R.id.btn_left);
        btnLeft.setOnClickListener(v -> {
            currentIndex--; // Kurangi index
            if (currentIndex < 0) {
                currentIndex = images.length - 1; // Kembali ke gambar terakhir jika sudah di gambar pertama
            }
            imageView.setImageResource(images[currentIndex]);
        });

        // Set up button for right navigation
        ImageButton btnRight = view.findViewById(R.id.btn_right);
        btnRight.setOnClickListener(v -> {
            currentIndex++; // Tambah index
            if (currentIndex >= images.length) {
                currentIndex = 0; // Kembali ke gambar pertama jika sudah di gambar terakhir
            }
            imageView.setImageResource(images[currentIndex]);
        });

        // Set up button to navigate to FragmentTentang
        TextView lebihBanyakButton = view.findViewById(R.id.btn_lebihBanyak);
        lebihBanyakButton.setOnClickListener(v -> {
            // Navigasi ke FragmentTentang
            ((MainActivity) requireActivity()).navigateToTentang();
        });

        // Initialize GestureDetector
        gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // Enable ScrollView hanya jika scrolling vertikal
                if (Math.abs(distanceY) > Math.abs(distanceX)) {
                    requireActivity().findViewById(R.id.scroll_view).setEnabled(true);
                } else {
                    requireActivity().findViewById(R.id.scroll_view).setEnabled(false);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        // Mengatur touch listener pada layout utama
        view.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return false; // Kembalikan false agar event dilanjutkan
        });

        // Mengatur touch listener pada peta
        mapFragment.getView().setOnTouchListener((v, event) -> {
            // Menonaktifkan ScrollView saat peta disentuh
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                requireActivity().findViewById(R.id.scroll_view).setEnabled(false);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                requireActivity().findViewById(R.id.scroll_view).setEnabled(true);
            }
            return false; // Kembalikan false agar event dilanjutkan
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Atur posisi kamera ke lokasi awal
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15)); // Zoom level 15

        // Tambahkan penanda lokasi
        mMap.addMarker(new MarkerOptions()
                .position(initialLocation)
                .title("Balai Latihan Kerja Nganjuk")); // Nama lokasi

        // Set listener untuk marker
        mMap.setOnMarkerClickListener(marker -> {
            // Tindakan saat marker ditekan dan ditahan
            return false; // Kembalikan false agar event dilanjutkan
        });

        // Menyimpan posisi kamera
        mMap.setOnCameraChangeListener(cameraPosition -> {
            // Simpan posisi kamera setelah perubahan
            LatLng target = cameraPosition.target;
            float zoom = cameraPosition.zoom;

            // Jika zoom level lebih kecil dari level minimum, kembalikan ke posisi awal
            if (zoom < 10) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15));
            }
        });

        // Menangani event tekan dan tahan pada peta
        mMap.setOnMapLongClickListener(latLng -> {
            // Membuka Google Maps pada lokasi yang diinginkan
            String uri = String.format("geo:%f,%f?q=%f,%f(Balai+Latihan+Kerja+Nganjuk)", latLng.latitude, latLng.longitude, latLng.latitude, latLng.longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Panggil listener saat fragment sudah muncul
        if (listener != null) {
            listener.onDashboardVisible();
        }
    }

    // Method untuk mengatur listener dari MainActivity
    public void setOnDashboardVisibleListener(OnDashboardVisibleListener listener) {
        this.listener = listener;
    }

    // Interface untuk listener
    public interface OnDashboardVisibleListener {
        void onDashboardVisible();
    }
}
