package com.example.projectpelatihanku.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.example.projectpelatihanku.Models.Requirements;
import com.example.projectpelatihanku.R;

import java.util.List;

/**
 * Adapter untuk menampilkan persyaratan program
 */
public class RequirementsAdapter extends RecyclerView.Adapter<RequirementsAdapter.ViewHolder> {

    private List<Requirements> requirementsList;
    private Context context;

    /**
     * Constructor untuk RequirementsAdapter
     *
     * @param requirementsList daftar persyaratan program
     * @param context          konteks aplikasi
     * @see Requirements
     */
    public RequirementsAdapter(List<Requirements> requirementsList, Context context) {
        this.requirementsList = requirementsList;
        this.context = context;
    }

    /**
     * Membuat ViewHolder untuk setiap item dalam RecyclerView
     * Layout inflater digunakan untuk membuat view dari layout XML requirement_items
     *
     * @param parent   referensi ke parent tempat item akan ditempatkan (RecyclerView)
     * @param viewType Tipe view yang akan dibuat.
     * @return ViewHolder yang telah dibuat.
     */
    @NonNull
    @Override
    public RequirementsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.requirement_items, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Mengisi data dari persyaratan ke ViewHolder
     *
     * @param holder   ViewHolder yang berisi data persyaratan
     * @param position posisi item dalam daftar persyaratan
     */
    @Override
    public void onBindViewHolder(@NonNull RequirementsAdapter.ViewHolder holder, int position) {
        Requirements requirements = requirementsList.get(position);
        holder.txtRequirements.setText(requirements.getRequirement());
    }

    /**
     * Mengembalikan jumlah item dalam daftar persyaratan
     *
     * @return jumlah item dalam daftar persyaratan
     */
    @Override
    public int getItemCount() {
        return requirementsList.size();
    }

    /**
     * ViewHolder untuk setiap item dalam RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRequirements;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRequirements = itemView.findViewById(R.id.txtRequirement);
        }
    }
}
