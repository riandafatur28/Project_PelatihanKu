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

public class InstitusiAdapter extends RecyclerView.Adapter<InstitusiAdapter.InstitusiViewHolder> {
    private List<Institusi> institusiList;
    private Context context;

    public InstitusiAdapter(List<Institusi> institusiList, Context context) {
        this.institusiList = institusiList;
        this.context = context;
    }

    @NonNull
    @Override
    public InstitusiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_institusi, parent, false);
        return new InstitusiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstitusiViewHolder holder, int position) {
        Institusi institusi = institusiList.get(position);
        holder.textNamaInstitusi.setText(institusi.getNama()); // Memanggil metode yang benar
        holder.textDeskripsi.setText(institusi.getDeskripsi());
        Glide.with(context).load(institusi.getImageUrl()).into(holder.imageInstitusi);

        // Menambahkan listener untuk tombol lihat program
        holder.btnLihatProgram.setOnClickListener(v -> {
//            FragmentProgramInstitusi fragmentProgramInstitusi = FragmentProgramInstitusi.newInstance(institusi);
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, fragmentProgramInstitusi);
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
