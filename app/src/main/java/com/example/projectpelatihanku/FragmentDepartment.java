package com.example.projectpelatihanku;

import static com.example.projectpelatihanku.MainActivity.showBottomNavigationView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectpelatihanku.Models.Department;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment untuk menampilkan daftar department
 * @see SharedPreferencesHelper
 * @see ApiClient
 * @see Department
 * @see DepartmentAdapter
 */
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

        fetchData(new ApiClient());
        showBottomNavigationView();
        return view;
    }

    /**
     * Mengambil data dari API
     *
     * @param apiClient Service Class untuk mengambil data dari API
     * @see ApiClient
     * @see Department
     * @see SharedPreferencesHelper#getToken(Context)
     *
     */
    public void fetchData(ApiClient apiClient) {
        String token = SharedPreferencesHelper.getToken(getContext());

        apiClient.fetchDepartment(token, endPoint, new ApiClient.DepartmentHelper() {
            @Override
            public void onSuccess(ArrayList<Department> data) {
                requireActivity().runOnUiThread(() -> {
                    if (data != null && !data.isEmpty()) {
                        departmentList.clear();
                        departmentList.addAll(data);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailed(IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(),  e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}