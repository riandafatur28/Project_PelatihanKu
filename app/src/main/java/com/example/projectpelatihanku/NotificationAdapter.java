package com.example.projectpelatihanku;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectpelatihanku.Models.Notification;
import com.example.projectpelatihanku.api.ApiClient;
import com.example.projectpelatihanku.helper.FragmentHelper;

import java.util.List;

/**
 * Adapter untuk RecyclerView daftar notifikasi
 *
 * @see Notification
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Notification> notificationsList;
    private final Context context;
    private String message;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notificationsList = notifications;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        Notification notification = notificationsList.get(position);
        holder.txtNotification.setText(notification.getMessage());
        if (notification.isRead() == 1) {
            setIsRead(holder);
        } else {
            setIsUnread(holder);
        }

        holder.itemView.setOnLongClickListener(v -> {
            showDeleteConfirmationDialog(notification);
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            message = notification.getMessage();
            setIsRead(holder);
            FragmentNotifikasi.updateIsRead(notification, new ApiClient(), context);
            NavigateToFragement(holder, message);
        });
    }

    /**
     * Menampilkan dialog konfirmasi sebelum menghapus notifikasi
     *
     * @param notification Notifikasi yang akan dihapus
     * @see FragmentNotifikasi#updateIsDeleted(Notification, ApiClient, Context)
     */
    private void showDeleteConfirmationDialog(Notification notification) {
        new AlertDialog.Builder(context)
                .setTitle("Hapus Notifikasi")
                .setMessage("Apakah Anda yakin ingin menghapus notifikasi ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    FragmentNotifikasi.updateIsDeleted(notification, new ApiClient(), context);
                    int position = notificationsList.indexOf(notification);
                    if (position != -1) {
                        notificationsList.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNotification;
        public View indicatorStatus;

        public NotificationViewHolder(View view) {
            super(view);
            txtNotification = view.findViewById(R.id.notificationText);
            indicatorStatus = view.findViewById(R.id.indicator);
        }
    }

    /**
     * Navigasi ke fragment detail notifikasi
     *
     * @param holder  ViewHolder dari item notifikasi yang diklik
     * @param message Pesan dari notifikasi yang diklik
     * @see FragmentHelper#navigateToFragment(FragmentActivity, int, Fragment, boolean, String)
     * @see FragmentDetailNotifikasi#FragmentDetailNotifikasi(String)
     */
    private void NavigateToFragement(NotificationViewHolder holder, String message) {
        FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();
        View recyclerView = activity.findViewById(R.id.recyclerViewNotifikasi);
        if (recyclerView != null) {
            recyclerView.setVisibility(View.GONE);
        }
        FragmentHelper.navigateToFragment(activity, R.id.layerNotifikasi,
                new FragmentDetailNotifikasi(message), true, null);
    }

    /**
     * Mengatur status notifikasi menjadi sudah dibaca
     *
     * @param holder ViewHolder dari item notifikasi yang akan diatur
     */
    public static void setIsRead(NotificationViewHolder holder) {
        holder.indicatorStatus.setBackgroundResource(R.drawable.indicator_read);
    }

    /**
     * Mengatur status notifikasi menjadi belum dibaca (default)
     *
     * @param holder
     */
    public static void setIsUnread(NotificationViewHolder holder) {
        holder.indicatorStatus.setBackgroundResource(R.drawable.indicator_unread);
    }
}