package com.skynet.common.model;

public class Airline {
    private int id;
    private String name;
    private String iata;
    private String logoPath;

    public Airline() {
    }

    public Airline(int id, String name, String iata, String logoPath) {
        this.id = id;
        this.name = name;
        this.iata = iata;
        this.logoPath = logoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}