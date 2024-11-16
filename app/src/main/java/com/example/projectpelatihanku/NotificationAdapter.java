package com.example.projectpelatihanku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<MyNotification> {
    private final List<MyNotification> notifications;
    private final Context context;
    private final OnNotificationCheckedChangeListener listener;

    // Constructor untuk adapter
    public NotificationAdapter(@NonNull Context context, List<MyNotification> notifications, OnNotificationCheckedChangeListener listener) {
        super(context, 0, notifications);
        this.context = context;
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_with_indicator, parent, false);
        }

        TextView notificationText = convertView.findViewById(R.id.notificationText);
        View indicator = convertView.findViewById(R.id.indicator);
        CheckBox checkBoxHapus = convertView.findViewById(R.id.checkBox);

        MyNotification notification = notifications.get(position);
        notificationText.setText(notification.getTitle());

        if (notification.isRead()) {
            indicator.setBackground(ContextCompat.getDrawable(context, R.drawable.indicator_read));
        } else {
            indicator.setBackground(ContextCompat.getDrawable(context, R.drawable.indicator_unread));
        }

        checkBoxHapus.setChecked(notification.isChecked());

        checkBoxHapus.setOnCheckedChangeListener(null);
        checkBoxHapus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notification.setChecked(isChecked);
            if (listener != null) {
                listener.onNotificationCheckedChange();
            }
        });

        return convertView;
    }
}