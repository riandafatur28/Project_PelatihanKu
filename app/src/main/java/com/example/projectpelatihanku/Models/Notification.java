package com.example.projectpelatihanku.Models;

public class Notification {
    private String message;
    private int isRead;
    private String id;

    public Notification(String id, String message, int isRead) {
        this.message = message;
        this.isRead = isRead;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public int isRead() {
        return isRead;
    }

    public String getId() {
        return id;
    }
}