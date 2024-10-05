package com.example.projectpelatihanku;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.core.content.ContextCompat;

public class FragmentNotifikasi extends Fragment {

    private Button buttonSemuaNotifikasi, buttonNotifikasiBelumDibaca;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);

        buttonSemuaNotifikasi = view.findViewById(R.id.buttonSemuaNotifikasi);
        buttonNotifikasiBelumDibaca = view.findViewById(R.id.buttonNotifikasiBelumDibaca);

        // Load default view (Semua Notifikasi)
        loadFragment(new FragmentSemuaNotifikasi());
        setActiveButton(buttonSemuaNotifikasi, buttonNotifikasiBelumDibaca); // Set default active button

        // Set click listeners for both buttons
        buttonSemuaNotifikasi.setOnClickListener(v -> {
            loadFragment(new FragmentSemuaNotifikasi());
            setActiveButton(buttonSemuaNotifikasi, buttonNotifikasiBelumDibaca);
        });

        buttonNotifikasiBelumDibaca.setOnClickListener(v -> {
            loadFragment(new FragmentNotifikasiBelumDibaca());
            setActiveButton(buttonNotifikasiBelumDibaca, buttonSemuaNotifikasi);
        });

        return view;
    }

    // Helper method to load the selected fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layerNotifikasi, fragment);
        transaction.commit();
    }

    // Helper method to change the active button's color
    private void setActiveButton(Button activeButton, Button inactiveButton) {
        // Set the active button to orange with white text
        activeButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.orange));
        activeButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

        // Set the inactive button to transparent with orange text
        inactiveButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), android.R.color.transparent));
        inactiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange));
    }
}
