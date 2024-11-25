package com.example.projectpelatihanku.Models;

public class Dashboard {
    private final String tableName;
    private final int total;

    public Dashboard(String tableName, int total) {
        this.tableName = tableName;
        this.total = total;
    }

    public String getTableName() {
        return tableName;
    }

    public int getTotal() {
        return total;
    }
}
