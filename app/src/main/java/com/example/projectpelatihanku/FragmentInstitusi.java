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

public class FragmentInstitusi extends Fragment {
    private String endPoint = "/departments";
    private RecyclerView recyclerView;
    private InstitusiAdapter adapter;
    private List<Institusi> institusiList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_institusi, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewInstitusi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new InstitusiAdapter(institusiList, getContext());
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
            public void onSuccess(ArrayList<Institusi> data) {
                requireActivity().runOnUiThread(() -> {
                    institusiList.clear();
                    institusiList.addAll(data);
                    Log.d("Department", "onSuccessFetchDepartment: " +data);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailed(IOException e) {
                Log.d("Failed", "onFailed: " + e.getMessage());
            }
        });
    }
}
