package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.InstitusiViewHolder> {
    private List<Department> institusiList;
    private Context context;
    private String departmentId;

    public DepartmentAdapter(List<Department> departmentList, Context context) {
        this.institusiList = departmentList;
        this.context = context;
    }

    @NonNull
    @Override
    public InstitusiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_department, parent, false);
        return new InstitusiViewHolder(view);
    }

    private void navigateToProgram(String departmentId, String namaInstitusi) {
        // Simpan departmentId ke SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("programPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("departmentId", departmentId);
        editor.apply(); // Simpan perubahan

        // Log untuk memastikan nilai departmentId tersimpan
        Log.d("DepartmentAdapter", "Saved departmentId: " + departmentId);

        // Navigasi ke FragmentProgram
        FragmentActivity activity = (FragmentActivity) context;
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomview);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        FragmentProgram fragmentProgram = new FragmentProgram(departmentId);

        Bundle bundle = new Bundle();
        bundle.putString("namaInstitusi", namaInstitusi);

        fragmentProgram.setArguments(bundle);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutDepartment, fragmentProgram);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBindViewHolder(@NonNull InstitusiViewHolder holder, int position) {
        Department department = institusiList.get(position);
        holder.textNamaInstitusi.setText(department.getNama());
        holder.textDeskripsi.setText(department.getDeskripsi());

        // Menampilkan gambar menggunakan Glide
        Glide.with(context).load(department.getImageUrl()).into(holder.imageInstitusi);

        // Tombol "Lebih Banyak"
        holder.btnLihatProgram.setOnClickListener(v -> {
            // Ambil departmentId langsung dari objek department
            String departmentId = department.getId(); // Ambil ID dari objek department
            String namaInstitusi = department.getNama();

            // Panggil navigateToProgram dengan parameter yang tepat
            navigateToProgram(departmentId, namaInstitusi);
        });
    }


    @Override
    public int getItemCount() {
        return institusiList.size();
    }

    static class InstitusiViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageInstitusi;
        TextView textNamaInstitusi, textDeskripsi, btnLihatProgram;

        InstitusiViewHolder(@NonNull View itemView) {
            super(itemView);
            imageInstitusi = itemView.findViewById(R.id.imageDepartment);
            textNamaInstitusi = itemView.findViewById(R.id.text_nama_department);
            textDeskripsi = itemView.findViewById(R.id.text_deskripsi);
            btnLihatProgram = itemView.findViewById(R.id.btn_lihat_program);
        }
    }
}
