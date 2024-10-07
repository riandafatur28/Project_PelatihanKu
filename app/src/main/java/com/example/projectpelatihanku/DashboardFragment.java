package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
    private ImageView imageView;
    private int[] images = {R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3};
    private int currentIndex = 0;
    private GestureDetector gestureDetector;
    private OnDashboardVisibleListener listener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardVisibleListener) {
            listener = (OnDashboardVisibleListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnDashboardVisibleListener");
        }
    }

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Inisialisasi komponen UI
        imageUser = view.findViewById(R.id.imageUser);
        TextView salamText = view.findViewById(R.id.salamText);
        imageView = view.findViewById(R.id.imagepage);

        // Mengambil data pengguna dari SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(KEY_USER_NAME, "User");  // "User" adalah default jika nama tidak ditemukan
        salamText.setText("Halo, " + userName);  // Menampilkan nama pengguna


        // Mengambil URI gambar profil dari SharedPreferences
        String imageUriString = sharedPreferences.getString("image_uri", null);
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            try {
                imageUser.setImageURI(imageUri);  // Set gambar profil menggunakan URI
            } catch (Exception e) {
                imageUser.setImageResource(R.drawable.gambar_user);  // Set gambar default jika terjadi kesalahan
                e.printStackTrace();
            }
        } else {
            // Jika tidak ada gambar yang disimpan, gunakan gambar default
            imageUser.setImageResource(R.drawable.gambar_user);
        }

        // Inisialisasi peta
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Tombol untuk berpindah gambar carousel
        ImageButton btnLeft = view.findViewById(R.id.btn_left);
        btnLeft.setOnClickListener(v -> {
            currentIndex--;
            if (currentIndex < 0) {
                currentIndex = images.length - 1;
            }
            imageView.setImageResource(images[currentIndex]);
        });

        ImageButton btnRight = view.findViewById(R.id.btn_right);
        btnRight.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex >= images.length) {
                currentIndex = 0;
            }
            imageView.setImageResource(images[currentIndex]);
        });

        // Gesture untuk mengaktifkan scroll pada peta
        gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                requireActivity().findViewById(R.id.scroll_view).setEnabled(Math.abs(distanceY) > Math.abs(distanceX));
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        view.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return false;
        });

        // Tombol "Lebih Banyak"
        TextView btn_LebihBanyak = view.findViewById(R.id.btn_lebihBanyak);
        btn_LebihBanyak.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTentang();
            }
        });

        return view;
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
