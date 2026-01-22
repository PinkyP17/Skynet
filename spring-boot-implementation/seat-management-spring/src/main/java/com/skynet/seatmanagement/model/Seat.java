package com.skynet.seatmanagement.model;

import jakarta.persistence.*;

/**
 * Seat Entity - represents a seat in the aircraft
 * 
 * Maps to the 'seats' table in the database
 */
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "column")
    private String column;

    @Column(name = "row")
    private Integer row;

    @Column(name = "type")
    private String type; // First, Business, Economy

    public Seat() {}

    public Seat(String column, Integer row, String type) {
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
