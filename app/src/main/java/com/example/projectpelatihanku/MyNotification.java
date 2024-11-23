package com.example.projectpelatihanku;

public class MyNotification {
    private String title;
    private boolean isRead;
    private boolean checked;
    private String id;

    public MyNotification(String id, String title, boolean isRead) {
        this.title = title;
        this.isRead = isRead;
        this.id = id;
        this.checked = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isRead() {
        return isRead;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getId() {
        return id;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}