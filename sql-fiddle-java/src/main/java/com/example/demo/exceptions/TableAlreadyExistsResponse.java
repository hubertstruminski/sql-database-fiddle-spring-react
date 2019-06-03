package com.example.demo.exceptions;

public class TableAlreadyExistsResponse {

    private String tableName;

    public TableAlreadyExistsResponse(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
