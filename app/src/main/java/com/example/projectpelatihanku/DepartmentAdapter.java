package com.example.projectpelatihanku;

import android.content.Context;
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

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.InstitusiViewHolder> {
    private List<Department> institusiList;
    private Context context;

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

    // Method untuk navigasi ke FragmentProgram
    private void navigateToProgram() {
        // Membuat instance FragmentProgram
        FragmentProgram fragmentProgram = new FragmentProgram();

        // Menggunakan FragmentActivity untuk mendapatkan FragmentManager
        FragmentActivity activity = (FragmentActivity) context;

        // Memulai transaksi fragment
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        // Gantikan fragment yang ada di R.id.fragment_container dengan FragmentProgram
        transaction.replace(R.id.layoutDepartment, fragmentProgram);
        // Menambahkan transaksi ke BackStack (agar dapat kembali ke fragment sebelumnya)
        transaction.addToBackStack(null);
        // Menyelesaikan transaksi
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
            // Call navigateToProgram when button is clicked
            navigateToProgram();


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
