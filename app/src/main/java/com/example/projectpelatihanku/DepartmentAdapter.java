package com.example.projectpelatihanku;

import static com.example.projectpelatihanku.MainActivity.hideBottomNavigationView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectpelatihanku.Models.Department;
import com.example.projectpelatihanku.helper.FragmentHelper;
import com.example.projectpelatihanku.helper.GlideHelper;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Adapter untuk RecyclerView Department
 *
 * @see Department
 * @see GlideHelper
 * @see FragmentHelper
 */
public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.InstitusiViewHolder> {
    private List<Department> departmentList;
    private Context context;

    /**
     * Constructor untuk kelas DepartmentAdapter
     *
     * @param departmentList daftar department
     * @param context        konteks aplikasi
     */
    public DepartmentAdapter(List<Department> departmentList, Context context) {
        this.departmentList = departmentList;
        this.context = context;
    }

    /**
     * Membuat ViewHolder untuk setiap item dalam RecyclerView
     * Layout inflater digunakan untuk membuat view dari layout XML item_department
     *
     * @param parent   referensi ke parent tempat item akan ditempatkan (RecyclerView)
     * @param viewType Tipe view yang akan dibuat.
     * @return ViewHolder yang telah dibuat.
     */
    @NonNull
    @Override
    public InstitusiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_department, parent, false);
        return new InstitusiViewHolder(view);
    }

    /**
     * Navigasi ke FragmentProgram
     *
     * @param departmentId   id department yang dipilih
     * @param departmentName nama department yang dipilih
     * @see FragmentProgram#FragmentProgram(String, String)
     * @see FragmentHelper#navigateToFragment
     */
    private void navigateToProgram(String departmentId, String departmentName) {

        FragmentActivity activity = (FragmentActivity) context;
        hideBottomNavigationView();
        FragmentProgram fragmentProgram = new FragmentProgram(departmentId, departmentName);
        FragmentHelper.navigateToFragment(activity, R.id.layoutDepartment, fragmentProgram, true,
                null);
    }


    /**
     * Mengisi data dari department ke ViewHolder
     *
     * @param holder   ViewHolder yang akan diisi data
     * @param position posisi item dalam daftar
     * @see Department
     * @see InstitusiViewHolder
     * @see GlideHelper#loadImage(Context, ImageView, String)
     * @see #navigateToProgram(String, String)
     */
    @Override
    public void onBindViewHolder(@NonNull InstitusiViewHolder holder, int position) {
        Department department = departmentList.get(position);
        holder.txtNamaDepartment.setText(department.getNama());
        holder.textDeskripsi.setText(department.getDeskripsi());

        GlideHelper.loadImage(context, holder.departmentImage, department.getImageUrl());

        holder.btnLihatProgram.setOnClickListener(v -> {
            String departmentId = department.getId();
            String departmentName = department.getNama();

            navigateToProgram(departmentId, departmentName);
        });
    }


    /**
     * Mengambil jumlah item dalam daftar department
     *
     * @return jumlah item dalam daftar department
     */
    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    /**
     * ViewHolder untuk setiap item dalam RecyclerView
     */
    static class InstitusiViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView departmentImage;
        TextView txtNamaDepartment, textDeskripsi, btnLihatProgram;

        InstitusiViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentImage = itemView.findViewById(R.id.imageDepartment);
            txtNamaDepartment = itemView.findViewById(R.id.text_nama_department);
            textDeskripsi = itemView.findViewById(R.id.text_deskripsi);
            btnLihatProgram = itemView.findViewById(R.id.btn_lihat_program);
        }
    }
}
