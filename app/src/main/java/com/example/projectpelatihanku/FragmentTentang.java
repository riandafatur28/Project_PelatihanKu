package com.example.projectpelatihanku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.projectpelatihanku.api.ApiClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FragmentTentang extends Fragment {
    private String endPoint = "institute/getInstitute";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tentang, container, false);

        ImageView backArrow = view.findViewById(R.id.imageArrow);
        backArrow.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToDashboard();
        });

        // Instansiasi Kelas ApiClient
        ApiClient apiClient = new ApiClient();
        // Panggil metode fetchInstitusi untuk melakukan request get data institusi
        apiClient.fetchInstitusi(endPoint, new ApiClient.GetResponese() {
            @Override
            public void onSuccesArray(String[] data) {
                // Jalankan ke main tread
                getActivity().runOnUiThread(() -> {
                    if (getView() != null) {
                        TextView namaInstitusi = getView().findViewById(R.id.nama_institusi);
                        TextView deskripsiInstitusi = getView().findViewById(R.id.deskripsiInstitusi);
                        TextView notelpLembaga = getView().findViewById(R.id.notelpLembaga);
                        TextView emailLembaga = getView().findViewById(R.id.emailLembaga);
                        TextView nomorFax = getView().findViewById(R.id.nomorFax);
                        TextView websiteLembaga = getView().findViewById(R.id.website);
                        TextView status = getView().findViewById(R.id.status_beroperasi);
                        TextView jenisLembaga = getView().findViewById(R.id.tipe);
                        TextView NoVin = getView().findViewById(R.id.no_vin);
                        TextView thnBerdiri = getView().findViewById(R.id.tahun_berdiri);
                        TextView namaPimpinan = getView().findViewById(R.id.nama_pimpinan);
                        TextView kepemilikan = getView().findViewById(R.id.kepemilikan);
                        TextView noSotk = getView().findViewById(R.id.no_sotk);

                        // Set teks dari data JSON
                        namaInstitusi.setText(data[1]);
                        deskripsiInstitusi.setText(data[12]);
                        notelpLembaga.setText(data[6]);
                        emailLembaga.setText(data[5]);
                        nomorFax.setText(data[10]);
                        websiteLembaga.setText(data[11]);
                        jenisLembaga.setText(data[9]);
                        NoVin.setText(data[7]);
                        thnBerdiri.setText(data[13]);
                        namaPimpinan.setText(data[2]);
                        kepemilikan.setText(data[3]);
                        noSotk.setText(data[8]);
                        status.setText(data[4]);
                    }
                });
            }

            @Override
            public void onSuccessArrayList(ArrayList<String> data) {

            }

            @Override
            public void onSuccessFetchNotif(ArrayList<MyNotification> data) {

            }

            @Override
            public void onSuccessFetchDepartment(ArrayList<Institusi> data) {

            }

            @Override
            public void onFailure(IOException e) {
                e.getMessage();
            }
        });
        return view;
    }
}
