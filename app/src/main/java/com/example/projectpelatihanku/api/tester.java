package com.example.projectpelatihanku.api;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectpelatihanku.R;

import org.json.JSONException;

public class tester extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tester);
        met();
    }

    public void met(){
        ApiClient apiClient = new ApiClient();
        apiClient.fetchDepartment("department/getDepartment");
    }
}