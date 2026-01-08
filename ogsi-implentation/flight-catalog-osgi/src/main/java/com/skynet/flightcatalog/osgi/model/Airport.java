package com.skynet.flightcatalog.osgi.model;

/**
 * Airport Model for Flight Catalog OSGi Bundle
 */
public class Airport {
    private Integer id;
    private String name;
    private String city;
    private String country;
    private String IATA;
    private String ICAO;

    public Airport() {
    }

    public Airport(Integer id, String name, String city, String country, String IATA, String ICAO) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.IATA = IATA;
        this.ICAO = ICAO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIATA() {
        return IATA;
    }

    public void setIATA(String IATA) {
        this.IATA = IATA;
    }

    public String getICAO() {
        return ICAO;
    }

    public void setICAO(String ICAO) {
        this.ICAO = ICAO;
    }
}
