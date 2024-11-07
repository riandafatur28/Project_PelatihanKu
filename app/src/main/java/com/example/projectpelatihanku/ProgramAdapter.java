package com.example.projectpelatihanku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {
    private List<Program> programList;
    private Context context;

    public ProgramAdapter(List<Program> programList, Context context) {
        this.programList = programList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_program, parent, false);
        return new ProgramViewHolder(view);  // Return the view holder with the inflated view
    }

    private void navigateToDepartment() {
        FragmentDepartment fragmentDepartment = new FragmentDepartment();
        FragmentActivity activity = (FragmentActivity) context;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutprogram, fragmentDepartment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        Program program = programList.get(position);
        holder.textNamaProgram.setText(program.getNama());
        holder.textDeskripsi.setText(program.getDeskripsi());

        // Load the program image using Glide
        Glide.with(context).load(program.getImageUrl()).into(holder.imageProgram);

        // Button to navigate to FragmentDepartment
        holder.imageArrow2.setOnClickListener(v -> navigateToDepartment());
        navigateToDepartment();
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }



    static class ProgramViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageProgram;
        TextView textNamaProgram, textDeskripsi, btnDetail;
        ImageView imageArrow2; // Reference to the back arrow

        ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProgram = itemView.findViewById(R.id.imageprogram);
            textNamaProgram = itemView.findViewById(R.id.text_nama_program);
            textDeskripsi = itemView.findViewById(R.id.text_deskripsi_program);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            imageArrow2 = itemView.findViewById(R.id.imageArrow2); // Link to the ImageView arrow
        }
    }
}
