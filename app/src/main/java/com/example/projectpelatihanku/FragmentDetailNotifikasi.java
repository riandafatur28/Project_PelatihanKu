package com.example.projectpelatihanku;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentDetailNotifikasi extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_DETAIL = "detail";

    public static FragmentDetailNotifikasi newInstance(String title, String detail) {
        FragmentDetailNotifikasi fragment = new FragmentDetailNotifikasi();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DETAIL, detail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_notifikasi, container, false);

        ImageView imageArrow = view.findViewById(R.id.imageArrow); // Ambil referensi ke ImageView
        TextView textTitle = view.findViewById(R.id.textTitle);
        TextView textContent = view.findViewById(R.id.textContent);

        if (getArguments() != null) {
            String title = getArguments().getString(ARG_TITLE);
            String detail = getArguments().getString(ARG_DETAIL);
            textTitle.setText(title);
            textContent.setText(detail);
        }

        // Menambahkan OnClickListener untuk ImageView
        imageArrow.setOnClickListener(v -> {
            // Menggunakan FragmentManager untuk kembali ke Fragment sebelumnya
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
}
}