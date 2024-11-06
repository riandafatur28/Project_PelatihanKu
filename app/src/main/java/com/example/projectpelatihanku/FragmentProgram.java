package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.auth0.android.jwt.JWT;
import com.example.projectpelatihanku.api.ApiClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Response;

public class FragmentProgram extends Fragment {

    private static final String ARG_DEPARTMENT_ID = "DepartmentId";
    private String DepartmentId;
    private String endPoint;
    private RecyclerView recyclerView;
    private ProgramAdapter adapter;
    private List<Program> ProgramList = new ArrayList<>();

    // Metode newInstance untuk membuat instance fragment dengan DepartmentId
    public static FragmentProgram newInstance(String departmentId) {
        FragmentProgram fragment = new FragmentProgram();
        Bundle args = new Bundle();
        args.putString(ARG_DEPARTMENT_ID, departmentId);
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
        View view = inflater.inflate(R.layout.fragment_department, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewInstitusi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProgramAdapter(ProgramList, getContext());
        recyclerView.setAdapter(adapter);

        fetchData();
        return view;
    }

    public void fetchData() {
        // Ambil token dari shared preference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "Token Tidak ditemukan");

        // Decode token untuk mendapatkan departmentId jika tidak diset melalui newInstance
        if (DepartmentId == null || DepartmentId.isEmpty()) {
            JWT jwt = new JWT(token);
            DepartmentId = jwt.getClaim("departments").asObject(Map.class).get("id").toString();
            endPoint = "/departments/" + DepartmentId + "/programs";
        }

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

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Periksa apakah respons berhasil (kode status 200)
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("ApiResponse", "Response body: " + responseBody);

                    // Periksa apakah body respons kosong
                    if (responseBody.isEmpty()) {
                        Log.e("ApiResponse", "Empty response body");
                        return; // Jangan coba parsing JSON jika respons kosong
                    }

                    // Cek Content-Type untuk memastikan JSON
                    String contentType = response.header("Content-Type");
                    if (contentType != null && contentType.contains("application/json")) {
                        try {
                            // Mengonversi respons menjadi objek JSON
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            // Proses data JSON di sini
                            // Misalnya, mendapatkan array program
                            // JSONArray programs = jsonResponse.getJSONArray("programs");
                            Log.d("ApiResponse", "Parsed JSON: " + jsonResponse.toString());
                        } catch (JSONException e) {
                            Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
                        }
                    } else {
                        Log.d("ApiResponse", "Unexpected content type: " + contentType);
                    }
                } else {
                    // Jika status kode bukan 200 OK, log error
                    Log.e("ApiResponse", "Request failed with status code: " + response.code());
                }
            }
        });
    }

}
