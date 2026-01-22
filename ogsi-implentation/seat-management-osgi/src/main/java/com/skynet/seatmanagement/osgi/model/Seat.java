package com.skynet.seatmanagement.osgi.model;

/**
 * Seat Model
 * Represents a seat in the aircraft
 */
public class Seat {
    private Integer id;
    private String column;
    private Integer row;
    private String type; // First, Business, Economy
    
    public Seat() {}
    
    public Seat(Integer id, String column, Integer row, String type) {
        this.id = id;
        this.column = column;
        this.row = row;
        this.type = type;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getColumn() {
        return column;
    }
    
    public void setColumn(String column) {
        this.column = column;
    }
    
    public Integer getRow() {
        return row;
    }
    
    public void setRow(Integer row) {
        this.row = row;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Get seat label (e.g., "A5", "B12")
     */
    public String getLabel() {
        return column + row;
    }
    
    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", label='" + getLabel() + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
