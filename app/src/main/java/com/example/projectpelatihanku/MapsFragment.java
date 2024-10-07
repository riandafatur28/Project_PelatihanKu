package com.example.projectpelatihanku;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);

        // Inisialisasi SupportMapFragment dan mendapatkan notifikasi ketika peta siap digunakan
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Mengizinkan zoom controls di peta
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Lokasi Balai Latihan Kerja Nganjuk
        LatLng targetLocation = new LatLng(-7.605396, 111.568613);

        // Menambahkan marker di Balai Latihan Kerja Nganjuk
        mMap.addMarker(new MarkerOptions().position(targetLocation).title("Gedung Balai Latihan Kerja Nganjuk"));

        // Memindahkan kamera ke lokasi Balai Latihan Kerja dengan zoom level 15
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 15)); // Zoom level diset ke 15

        // Cek apakah kamera berhasil dipindahkan
        System.out.println("Camera moved to target location.");
    }

}
