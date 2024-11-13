package com.example.projectpelatihanku;

public class DashboardData {
    private final String tableName;
    private final int total;

    public DashboardData(String tableName, int total) {
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
