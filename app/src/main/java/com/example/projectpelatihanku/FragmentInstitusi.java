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

import com.example.projectpelatihanku.api.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentInstitusi extends Fragment {
    private String endPoint = "/department/getDepartment";
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

        ApiClient api = new ApiClient();
        api.fetchDepartment(endPoint, new ApiClient.GetResponese() {
            @Override
            public void onSuccesArray(String[] data) {

            }

            @Override
            public void onSuccessArrayList(ArrayList<String> data) {

            }

            @Override
            public void onSuccessFetchNotif(ArrayList<MyNotification> data) {

            }

            @Override
            public void onSuccessFetchDepartment(ArrayList<Institusi> data) {
                requireActivity().runOnUiThread(() -> {
                    institusiList.clear();
                    institusiList.addAll(data);
                    Log.d("Department", "onSuccessFetchDepartment: " +data);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(IOException e) {
                Log.d("ERROR: ", "onFailure: " +e.getMessage());
            }
        });

        return view;
    }


}
