module FlightBookingApplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires javafx.swing;
    requires spring.web;
    requires spring.core;
    requires spring.beans;
    requires com.fasterxml.jackson.databind;

    opens application;
    opens controller;
    opens models;
    opens dto;
}