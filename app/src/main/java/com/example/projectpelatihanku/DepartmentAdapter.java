package com.example.projectpelatihanku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
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

    @Override
    public void onBindViewHolder(@NonNull InstitusiViewHolder holder, int position) {
        Department department = institusiList.get(position);
        holder.textNamaInstitusi.setText(department.getNama()); // Memanggil metode yang benar
        holder.textDeskripsi.setText(department.getDeskripsi());
        Glide.with(context).load(department.getImageUrl()).into(holder.imageInstitusi);
        // Menambahkan listener untuk tombol lihat program
        holder.btnLihatProgram.setOnClickListener(v -> {
            FragmentProgram fragmentProgramInstitusi = FragmentProgram.newInstance(String.valueOf(department));
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragmentProgramInstitusi);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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
            imageInstitusi = itemView.findViewById(R.id.imageInstitusi);
            textNamaInstitusi = itemView.findViewById(R.id.text_nama_institusi);
            textDeskripsi = itemView.findViewById(R.id.text_deskripsi);
            btnLihatProgram = itemView.findViewById(R.id.btn_lihat_program);
        }
    }
}
