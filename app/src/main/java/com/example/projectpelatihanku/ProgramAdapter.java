package com.example.projectpelatihanku;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {
    private final List<Program> programList;
    private final Context context;

    public ProgramAdapter(List<Program> programList, Context context) {
        this.programList = programList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_program, parent, false);
        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        Program program = programList.get(position);
        holder.textNamaProgram.setText(program.getNama());
        holder.textDeskripsi.setText(program.getDeskripsi());

        Glide.with(holder.itemView.getContext()).load(program.getImageUrl()).into(holder.imageProgram);

        if (holder.imageArrow2 != null) {
            holder.imageArrow2.setOnClickListener(v -> navigateToDepartment(holder));
        } else {
        }

        if (holder.btnDetail != null) {
            holder.btnDetail.setOnClickListener(v -> {
                saveProgramIdToPreferences(program.getId());
                navigateToDetailProgram(holder);
            });
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

    private void navigateToDepartment(ProgramViewHolder holder) {
        FragmentDepartment fragmentDepartment = new FragmentDepartment();
        FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutprogram, fragmentDepartment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToDetailProgram(ProgramViewHolder holder) {
        FragmentDetailProgram fragmentDetailProgram = new FragmentDetailProgram();
        FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutprogram, fragmentDetailProgram);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveProgramIdToPreferences(String programId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("programPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("programId", programId);
        editor.apply();
    }

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
            imageArrow2 = itemView.findViewById(R.id.imageArrow2);
        }
    }
}
