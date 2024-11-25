package com.example.projectpelatihanku;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectpelatihanku.helper.FragmentHelper;

public class FragmentDetailNotifikasi extends Fragment {
    private String message;

    /**
     * Construktor default, digunakan jika tidak ada parameter yang diberikan
     */
    public FragmentDetailNotifikasi() {

    }

    /**
     * Construktor untuk menerima parameter message
     *
     * @param message Pesan yang akan ditampilkan di fragment
     * @see MainActivity#hideBottomNavigationView()
     */
    public FragmentDetailNotifikasi(String message) {
        this.message = message;
        MainActivity.hideBottomNavigationView();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_notifikasi, container, false);

        TextView txtMessage = view.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

        backHandler(view);

        return view;
    }

    /**
     * Motode handler untuk melakukan navigasi kembali
     *
     * @param view View yang berisi layout fragment
     * @see FragmentHelper#navigateToFragment(FragmentActivity, int, Fragment, boolean, String)
     */
    private void backHandler(View view) {
        ImageView btnBack = view.findViewById(R.id.btnBack);
        FragmentHelper.backNavigation(getActivity(), btnBack, null, null, 0, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (getActivity() != null) {
            View recyclerView = getActivity().findViewById(R.id.recyclerViewNotifikasi);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        MainActivity.showBottomNavigationView();
    }
}
