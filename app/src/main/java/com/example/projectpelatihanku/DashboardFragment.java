
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.Models.Dashboard;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.GlideHelper;
import com.example.projectpelatihanku.helper.JwtHelper;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

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
    private String token;

    public DashboardFragment() {
    }

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
        initializeMap();
        startAutoSlide();

        salamText.setText("Halo, " + FragmentLogin.firstName);
        getAvatarImage(new ApiClient());

        view.findViewById(R.id.btn_lebihBanyak).setOnClickListener(v -> {
            FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity, new FragmentInstitute(), true, "institute");
        });

        fetchData(new ApiClient());
        MainActivity.showBottomNavigationView();
        return view;
    }

    /**
     * Mengambil data dashboard summary dari server
     *
     * @param apiClient Service Class untuk melakukan request ke server
     * @see SharedPreferencesHelper#getToken(Context)
     * @see ApiClient#fetchDashboard(String, String, ApiClient.DashboardDataHelper)
     */
    private void fetchData(ApiClient apiClient) {
        token = SharedPreferencesHelper.getToken(getContext());
        if (token == null || token.equals("Token Tidak ditemukan")) {
            showErrorUI();
            return;
        }

        apiClient.fetchDashboard(token, endPoint, new ApiClient.DashboardDataHelper() {
            @Override
            public void onSuccess(ArrayList<Dashboard> data) {
                requireActivity().runOnUiThread(() -> updateUIWithDashboardData(data));
            }

            @Override
            public void onFailed(IOException e) {
                requireActivity().runOnUiThread(() -> showErrorUI());
            }
        });
    }

    /**
     * Mengambil gambar profil dari server
     *
     * @param api Service Class untuk melakukan request ke server
     * @see SharedPreferencesHelper#getToken(Context)
     * @see ApiClient#fetchImageProfile(String, String, ApiClient.imageProfile)
     */
    private void getAvatarImage(ApiClient api) {
        token = SharedPreferencesHelper.getToken(getContext());
        JWT jwt = new JWT(token);
        Double userIdDouble = 0.0;
        try {
            userIdDouble = Double.parseDouble((String) jwt.getClaim("users").asObject(Map.class).get("id"));
        } catch (Exception e) {
            String id = JwtHelper.getUserData(SharedPreferencesHelper.getToken(getContext()), "users", "id");
            userIdDouble = Double.parseDouble(id);
        }
        int userId = userIdDouble.intValue();
        api.fetchImageProfile(token, "/users/image/" + userId, new ApiClient.imageProfile() {
            @Override
            public void onSuccess(String uri) {
                requireActivity().runOnUiThread(() -> {
                    GlideHelper.loadImage(getContext(), imageUser, uri);
                });
            }

            @Override
            public void onFailed(IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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
