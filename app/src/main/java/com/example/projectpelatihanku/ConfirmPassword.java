package com.example.projectpelatihanku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projectpelatihanku.ui.main.ConfirmPasswordFragment;

public class ConfirmPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ConfirmPasswordFragment.newInstance())
                    .commitNow();
        }
    }
}