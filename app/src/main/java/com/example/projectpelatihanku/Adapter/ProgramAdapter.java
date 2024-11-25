package com.example.projectpelatihanku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectpelatihanku.FragmentDetailProgram;
import com.example.projectpelatihanku.Models.Program;
import com.example.projectpelatihanku.R;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.FunctionHelper;
import com.example.projectpelatihanku.helper.GlideHelper;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

/**
 * Adapter RecyclerView Program untuk menampilkan daftar program
 * dari department yang dipilih pengguna
 */
public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {
    private final List<Program> programList;
    private final Context context;

    /**
     * Constructor untuk kelas ProgramAdapter
     *
     * @param programList daftar program dari department
     * @param context     konteks aplikasi
     * @see Program
     */
    public ProgramAdapter(List<Program> programList, Context context) {
        this.programList = programList;
        this.context = context;
    }

    /**
     * Membuat ViewHolder untuk setiap item dalam RecyclerView
     * Layout inflater digunakan untuk membuat view dari layout XML item_program
     *
     * @param parent   referensi ke parent tempat item akan ditempatkan (RecyclerView)
     * @param viewType Tipe view yang akan dibuat.
     * @return ViewHolder yang telah dibuat.
     */
    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_program, parent, false);
        return new ProgramViewHolder(view);
    }

    /**
     * Mengisi data dari program ke ViewHolder
     *
     * @param holder   ViewHolder yang berisi data program
     * @param position posisi item dalam daftar program
     * @see GlideHelper#loadImage(Context, ImageView, String)
     */
    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        Program program = programList.get(position);
        holder.textNamaProgram.setText(program.getNama());
        String deskripsi = FunctionHelper.potongString(program.getDeskripsi(), 4);
        holder.textDeskripsi.setText(deskripsi);

        GlideHelper.loadImage(holder.itemView.getContext(), holder.imageProgram, program.getImageUrl());

        if (holder.btnDetail != null) {
            holder.btnDetail.setOnClickListener(v -> {
                navigateToDetailProgram(holder, program.getId());
            });
        } else {
        }
    }

    /**
     * Mengembalikan jumlah item dalam daftar program
     *
     * @return jumlah item dalam daftar program
     */
    @Override
    public int getItemCount() {
        return programList.size();
    }

    /**
     * Navigasi ke FragmentDetailProgram
     *
     * @param holder     ViewHolder yang berisi data program
     * @param programsId id program yang dipilih
     * @see FragmentDetailProgram#FragmentDetailProgram(String)
     * @see FragmentHelper#navigateToFragment(FragmentActivity, int, Fragment, boolean, String)
     */
    private void navigateToDetailProgram(ProgramViewHolder holder, String programsId) {
        FragmentDetailProgram fragmentDetailProgram = new FragmentDetailProgram(programsId);
        FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();
        FragmentHelper.navigateToFragment(activity, R.id.navActivity, fragmentDetailProgram,
                true, "programDetail");
    }

    /**
     * ViewHolder untuk setiap item dalam RecyclerView
     */
    static class ProgramViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageProgram;
        TextView textNamaProgram, textDeskripsi, btnDetail;
        ImageView imageArrow2;

        ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProgram = itemView.findViewById(R.id.imageprogram);
            textNamaProgram = itemView.findViewById(R.id.text_nama_program);
            textDeskripsi = itemView.findViewById(R.id.text_deskripsi_program);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            imageArrow2 = itemView.findViewById(R.id.btnBack);
        }
    }
}
