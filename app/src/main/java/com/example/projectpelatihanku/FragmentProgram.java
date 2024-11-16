package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    private String endPoint;
    private RecyclerView recyclerView;
    private ProgramAdapter adapter;
    private List<Program> programList = new ArrayList<>();

    public FragmentProgram(String departmentId) {
        this.endPoint = "/departments/" + departmentId + "/programs";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String namaInstitusi = bundle.getString("namaInstitusi");
            TextView textNamaInstitusi = view.findViewById(R.id.texttentang);
            String displayText = "Program di Kejuruan " + namaInstitusi;
            textNamaInstitusi.setText(displayText);
        }

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomview);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        recyclerView = view.findViewById(R.id.recyclerViewProgramInstitusi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProgramAdapter(programList, getContext());
        recyclerView.setAdapter(adapter);
        ImageView imageArrow2 = view.findViewById(R.id.imageArrow2);
        imageArrow2.setOnClickListener(v -> navigateToDepartment());
        fetchData();

        return view;
    }

    private void navigateToDepartment() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomview);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }

            FragmentDepartment fragmentDepartment = new FragmentDepartment();
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.layoutprogram, fragmentDepartment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void fetchData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "Token Tidak ditemukan");

        ApiClient apiClient = new ApiClient();
        apiClient.fetchProgram(token, endPoint, new ApiClient.ProgramHelper() {
            @Override
            public void onSuccess(ArrayList<Program> data) {
                requireActivity().runOnUiThread(() -> {
                    if (data != null && !data.isEmpty()) {
                        programList.clear();
                        programList.addAll(data);
                        adapter.notifyDataSetChanged();
                    } else {
                    }
                });
            }

            @Override
            public void onFailed(IOException e) {
            }
        });
    }
}
