package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectpelatihanku.api.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentProgram extends Fragment {

    private static final String ARG_DEPARTMENT_ID = "DepartmentId";
    private String DepartmentId;
    private String endPoint;
    private RecyclerView recyclerView;
    private ProgramAdapter adapter;
    private List<Program> ProgramList = new ArrayList<>();
    private Object context;

    // Metode newInstance untuk membuat instance fragment dengan DepartmentId
    public static FragmentProgram newInstance(int departmentId) {
        FragmentProgram fragment = new FragmentProgram();
        Bundle args = new Bundle();
        args.putInt(ARG_DEPARTMENT_ID, departmentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mendapatkan DepartmentId dari arguments jika ada
        if (getArguments() != null) {
            DepartmentId = getArguments().getString(ARG_DEPARTMENT_ID);
            // Membuat endpoint berdasarkan DepartmentId
            if (DepartmentId != null && !DepartmentId.isEmpty()) {
                endPoint = "/departments/" + DepartmentId + "/programs";
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating layout untuk fragment
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProgramInstitusi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProgramAdapter(ProgramList, getContext());
        recyclerView.setAdapter(adapter);

        // Set click listener for the arrow image
        ImageView imageArrow2 = view.findViewById(R.id.imageArrow2);
        imageArrow2.setOnClickListener(v -> navigateToDepartmentFragment());

        fetchData();

        return view;
    }


    private void navigateToDepartmentFragment() {
        if (getActivity() != null) {
            FragmentDepartment fragmentDepartment = new FragmentDepartment(); // Membuat fragment baru

            // Ganti fragment yang ada dengan FragmentDepartment tanpa menambahkannya ke back stack
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layoutprogram, fragmentDepartment) // Ganti fragment yang ada
                    .commit();  // Tidak menggunakan addToBackStack, fragment lama akan diganti
        } else {
            Log.e("FragmentProgram", "Activity is null");
        }
    }


    public void fetchData() {
        // Ambil token dari shared preference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "Token Tidak ditemukan");

        ApiClient apiClient = new ApiClient();
        // Mendapatkan data program dari API
        apiClient.fetchProgram(token, endPoint, new ApiClient.ProgramHelper() {
            @Override
            public void onSuccess(ArrayList<Program> data) {
                requireActivity().runOnUiThread(() -> {
                    if (data != null && !data.isEmpty()) {
                        ProgramList.clear();
                        ProgramList.addAll(data);
                        Log.d("Department", "onSuccessFetchDepartment: " + data);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("Department", "No data found or empty list.");
                    }
                });
            }

            @Override
            public void onFailed(IOException e) {
                Log.d("Failed", "onFailed: " + e.getMessage());
            }
        });
    }
}
