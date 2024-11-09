package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectpelatihanku.api.ApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentDepartment extends Fragment {
    private String endPoint = "/departments";
    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private List<Department> departmentList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewInstitusi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DepartmentAdapter(departmentList, getContext());
        recyclerView.setAdapter(adapter);

        fetchData();
        return view;
    }

    public void fetchData() {
        // Get token dari shared preference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "Token Tidak ditemukan");
        ApiClient apiClient = new ApiClient();

        // get data
        apiClient.fetchDepartment(token, endPoint, new ApiClient.DepartmentHelper() {
            @Override
            public void onSuccess(ArrayList<Department> data) {
                requireActivity().runOnUiThread(() -> {
                    Log.d("Department", "Jumlah data diterima: " + data.size()); // Periksa jumlah data
                    if (data != null && !data.isEmpty()) {
                        departmentList.clear();
                        departmentList.addAll(data);
                        Log.d("Department", "Data setelah ditambahkan: " + departmentList.size());
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("Department", "Data kosong.");
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