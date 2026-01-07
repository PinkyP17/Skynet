package com.skynet.common.model;

public class Seat {
    private int id;
    private String column;
    private int row;
    private String type;

    public Seat() {
    }

    public Seat(int id, String column, int row, String type) {
        this.id = id;
        this.column = column;
        this.row = row;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}