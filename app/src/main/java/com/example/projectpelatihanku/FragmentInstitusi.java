package com.example.projectpelatihanku;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projectpelatihanku.MainActivity;
import com.example.projectpelatihanku.R;

public class FragmentInstitusi extends Fragment {

    // Parameter arguments
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentInstitusi() {
        // Required empty public constructor
    }

    // Factory method to create a new instance of this fragment
    public static FragmentInstitusi newInstance(String param1, String param2) {
        FragmentInstitusi fragment = new FragmentInstitusi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_institusi, container, false);

        // Menangani klik pada TextView "Lihat Program"
        TextView lihatProgramTextView = view.findViewById(R.id.btn_lihat_program);
        lihatProgramTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menggunakan metode dari MainActivity untuk berpindah ke FragmentProgramInstitusi
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.navigateToProgramInstitusi(); // Panggil metode navigasi
                }
            }
        });

        return view;
    }
}
