package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectpelatihanku.Adapter.ProgramAdapter;
import com.example.projectpelatihanku.Models.Program;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.SharedPreferencesHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentProgram extends Fragment {

    private RecyclerView recyclerView;
    private ProgramAdapter adapter;
    private ImageView backButton;
    private List<Program> programList = new ArrayList<>();
    private String departmentId;
    private String departmentName;
    private String endPoint;

    /**
     * Constructor untuk kelas FragmentProgram (default constructor)
     */
    public FragmentProgram() {

    }

    /**
     * Constructor untuk kelas FragmentProgram
     * Gunakan contructor ini untuk mengambil data program dari department yang dipilih
     *
     * @param departmentId   id department dari program yang dipilih
     * @param departmentName nama department dari program yang dipilih
     */
    public FragmentProgram(String departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        TextView textNamaInstitusi = view.findViewById(R.id.texttentang);
        textNamaInstitusi.setText("Program di Kejuruan " + departmentName);

        recyclerView = view.findViewById(R.id.recyclerViewProgramInstitusi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProgramAdapter(programList, getContext());
        recyclerView.setAdapter(adapter);
        backButton = view.findViewById(R.id.btnBack);
        fetchData(new ApiClient());
        navigateToDepartment();
        return view;
    }

    /**
     * Navigasi ke FragmentDepartment
     *
     * @see FragmentHelper#backNavigation(FragmentActivity, ImageView, Button, String, int, boolean) 
     * @see MainActivity#showBottomNavigationView()
     */
    private void navigateToDepartment() {
        FragmentHelper.backNavigation(getActivity(), backButton, null, "department", 0, true);
        MainActivity.showBottomNavigationView();
    }

    /**
     * Mengambil data dari API
     * Kirim token dari SharedPreferences ke ApiClient untuk mengambil data dari API
     *
     * @param apiClient Service Class untuk mengambil data dari API
     * @see SharedPreferencesHelper#getToken(Context)
     * @see ApiClient#fetchProgram(String, String, ApiClient.ProgramHelper)
     */
    public void fetchData(ApiClient apiClient) {
        String token = SharedPreferencesHelper.getToken(getContext());
        endPoint = "/departments/" + departmentId + "/programs";
        apiClient.fetchProgram(token, endPoint, new ApiClient.ProgramHelper() {
            @Override
            public void onSuccess(ArrayList<Program> data) {
                requireActivity().runOnUiThread(() -> {
                    if (data != null && !data.isEmpty()) {
                        programList.clear();
                        programList.addAll(data);
                        adapter.notifyDataSetChanged();
                    }
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

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.hideBottomNavigationView();
    }
}
