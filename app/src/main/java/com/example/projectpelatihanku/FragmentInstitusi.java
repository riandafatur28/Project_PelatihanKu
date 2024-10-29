package com.example.projectpelatihanku;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentInstitusi extends Fragment {
    private String endPoint = "http://192.168.1.6/department/getDepartment";
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

        fetchInstitusiData(); // Memanggil fungsi untuk mengambil data

        return view;
    }

    private void fetchInstitusiData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endPoint)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Network Error", "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        institusiList.clear(); // Pastikan untuk menghapus data sebelumnya
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String nama = jsonObject.getString("nama");
                            String deskripsi = jsonObject.getString("deskripsi");

                            // Pengecekan untuk "image_url"
                            String imageUrl = jsonObject.has("image_url") ? jsonObject.getString("image_url") : null;
                            if (imageUrl == null) {
                                Log.w("Data Warning", "image_url tidak ditemukan untuk institusi dengan ID: " + id);
                            }

                            // Buat objek Institusi dan tambahkan ke list
                            Institusi institusi = new Institusi(id, nama, deskripsi, imageUrl);
                            institusiList.add(institusi);
                        }

                        // Update UI di thread utama
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        Log.e("JSON Error", "JSON Parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e("Network Error", "Response unsuccessful or empty body");
                }
            }
        });
    }
}
