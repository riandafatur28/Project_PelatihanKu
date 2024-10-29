package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FragmentProgramInstitusi extends Fragment {

    private static final String ARG_NAMA_INSTITUSI = "nama_institusi";
    private String endPoint = "http://192.168.1.5:80/program/getProgram";
    private RecyclerView recyclerView;
    private ProgramAdapter adapter;
    private List<Program> programList = new ArrayList<>();
    private String namaInstitusi;

    public static FragmentProgramInstitusi newInstance(String nama) {
        FragmentProgramInstitusi fragment = new FragmentProgramInstitusi();
        Bundle args = new Bundle();
        args.putString(ARG_NAMA_INSTITUSI, nama);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            namaInstitusi = getArguments().getString(ARG_NAMA_INSTITUSI);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program_institusi, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProgramInstitusi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProgramAdapter(programList, getContext());
        recyclerView.setAdapter(adapter);

        fetchProgramData(); // Memanggil fungsi untuk mengambil data

        return view;
    }

    private void fetchProgramData() {
        // Tambahkan logika untuk mengambil data dari `endPoint`
        // Gunakan library seperti Retrofit atau Volley untuk mengakses data API
        // Setelah data diterima, tambahkan ke programList lalu panggil adapter.notifyDataSetChanged()
    }
}
