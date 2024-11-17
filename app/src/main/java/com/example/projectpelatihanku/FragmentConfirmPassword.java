package com.example.projectpelatihanku;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.projectpelatihanku.helper.FragmentHelper;

public class FragmentConfirmPassword extends Fragment {

    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_password, container, false);

        backButton = view.findViewById(R.id.buttonconfirmpass);
        backButton.setOnClickListener(v -> backHandler());

        return view;
    }

    /**
     * Handler button kembali untuk navigasi ke fragment login.
     * @see FragmentHelper#navigateToFragment(FragmentActivity, int, Fragment, boolean, String)
     * @see FragmentLogin
     */
    private void backHandler(){
        FragmentHelper.navigateToFragment(getActivity(), R.id.navActivity, new FragmentLogin(), true, null);
    }
}
