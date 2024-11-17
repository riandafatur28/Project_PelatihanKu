package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectpelatihanku.Models.Dashboard;
import com.example.projectpelatihanku.api.ApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

    static final String PREFS_NAME = "UserPrefs";
    public static final String KEY_USER_NAME = "user_name";
    private static final String endPoint = "/dashboard/summary";

    private ShapeableImageView imageUser;
    private GoogleMap mMap;
    private ImageView imagePage;
    private TextView totalDepartments, totalPrograms, totalBuildings, totalInstructors, totalTools, salamText;
    private LatLng initialLocation = new LatLng(-7.600671315258253, 111.88837430296729);
    private int[] images = {R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3};
    private int currentIndex = 0;
    private Handler handler;
    private final int CAROUSEL_DELAY = 5000;
    private OnDashboardVisibleListener listener;

    public DashboardFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardVisibleListener) {
            listener = (OnDashboardVisibleListener) context;
        } else {
            throw new ClassCastException(context.toString() + " harus mengimplementasikan OnDashboardVisibleListener");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initializeUI(view);
        loadUserData();
        initializeMap();
        startAutoSlide();

        view.findViewById(R.id.btn_lebihBanyak).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToInstitute();
            }
        });

        fetchData();

        return view;
    }

    private void fetchData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null || token.equals("Token Tidak ditemukan")) {
            Log.e("DashboardFragment", "Token tidak ditemukan di SharedPreferences");
            showErrorUI();
            return;
        }

        ApiClient apiClient = new ApiClient();

        apiClient.fetchDashboard(token, endPoint, new ApiClient.DashboardDataHelper() {
            @Override
            public void onSuccess(ArrayList<Dashboard> data) {
                requireActivity().runOnUiThread(() -> updateUIWithDashboardData(data));
            }

            @Override
            public void onFailed(IOException e) {
                Log.e("DashboardFragment", "Gagal mengambil data: " + e.getMessage());
                requireActivity().runOnUiThread(() -> showErrorUI());
            }
        });
    }

    private void updateUIWithDashboardData(ArrayList<Dashboard> data) {
        for (Dashboard item : data) {
            switch (item.getTableName()) {
                case "departments":
                    totalDepartments.setText("Memiliki Total " + item.getTotal());
                    break;
                case "programs":
                    totalPrograms.setText("Memiliki Total " + item.getTotal());
                    break;
                case "buildings":
                    totalBuildings.setText("Memiliki Total " + item.getTotal());
                    break;
                case "instructors":
                    totalInstructors.setText("Memiliki Total " + item.getTotal());
                    break;
                case "tools":
                    totalTools.setText("Memiliki Total " + item.getTotal());
                    break;
                default:
                    Log.w("DashboardFragment", "Nama tabel tidak dikenal: " + item.getTableName());
                    break;
            }
        }
    }

    private void showErrorUI() {
        totalDepartments.setText("Error");
        totalPrograms.setText("Error");
        totalBuildings.setText("Error");
        totalInstructors.setText("Error");
        totalTools.setText("Error");
    }

    private void initializeUI(View view) {
        imageUser = view.findViewById(R.id.imageUser);
        salamText = view.findViewById(R.id.salamText);
        imagePage = view.findViewById(R.id.imagepage);

        totalDepartments = view.findViewById(R.id.totalDepartment);
        totalPrograms = view.findViewById(R.id.totalProgram);
        totalBuildings = view.findViewById(R.id.totalBuilding);
        totalInstructors = view.findViewById(R.id.totalInstructor);
        totalTools = view.findViewById(R.id.totalTools);
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        salamText.setText("Halo, " + FragmentLogin.firstName);
        String imageUriString = sharedPreferences.getString("image_uri", null);
        if (imageUriString != null) {
            try {
                imageUser.setImageURI(Uri.parse(imageUriString));
            } catch (Exception e) {
                imageUser.setImageResource(R.drawable.gambar_user);
                Log.e("DashboardFragment", "Gagal memuat gambar pengguna", e);
            }
        } else {
            String gender = sharedPreferences.getString("gender", "Tidak diketahui");

            if ("Laki-laki".equalsIgnoreCase(gender)) {
                imageUser.setImageResource(R.drawable.img_men);
            } else if ("Perempuan".equalsIgnoreCase(gender)) {
                imageUser.setImageResource(R.drawable.img_women);
            }
        }
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void startAutoSlide() {
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentIndex++;
                if (currentIndex >= images.length) {
                    currentIndex = 0;
                }
                imagePage.setImageResource(images[currentIndex]);
                handler.postDelayed(this, CAROUSEL_DELAY);
            }
        }, CAROUSEL_DELAY);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
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

    public interface OnDashboardVisibleListener {
        void onDashboardVisible();
    }
}
