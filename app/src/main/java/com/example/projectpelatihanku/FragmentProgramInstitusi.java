package com.example.projectpelatihanku;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentProgramInstitusi extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout untuk fragment
        View view = inflater.inflate(R.layout.fragment_program_institusi, container, false); // Ganti dengan layout Anda

        // Inisialisasi ImageView
        ImageView imageArrow = view.findViewById(R.id.imageArrow2);
        imageArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menggunakan metode dari MainActivity untuk berpindah ke FragmentInstitusi
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.navigateToInstitusi(); // Memanggil metode navigasi ke FragmentInstitusi
                }
            }
        });

        return view;
    }
}
