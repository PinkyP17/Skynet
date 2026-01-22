package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Airline;
import models.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class FlightDao implements Dao<Flight> {
    private static FlightDao flightDao;
    public static FlightDao getInstance() {
        if (flightDao == null) {
            flightDao = new FlightDao();
        }
        return flightDao;
    }

    public ObservableList<Flight> getFlightsList() {
        return FXCollections.observableList(this.readAll());
    }

    AirportDao airportDao = new AirportDao();
    @Override
    public int create(Flight flight) {
        Connection conn = DataSource.getConnection();
        String statement = "INSERT INTO flights (dep_datetime, arr_datetime, first_price, business_price, economy_price, luggage_price, weight_price, id_airline, dep_airport, arr_airport) VALUES (?,?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement query = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            query.setString(1, flight.getDepDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            query.setString(2, flight.getArrDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            query.setDouble(3, flight.getFirstPrice());
            query.setDouble(4, flight.getBusinessPrice());
            query.setDouble(5, flight.getEconomyPrice());
            query.setDouble(6, flight.getLuggagePrice());
            query.setDouble(7, flight.getWeightPrice());
            query.setInt(8, flight.getAirline().getId());
            query.setInt(9, flight.getDepAirport().getId());
            query.setInt(10, flight.getArrAirport().getId());
            query.executeUpdate();
            ResultSet id = query.getGeneratedKeys();
            if (id.next()) {
                flight.setId(id.getInt(1));
            }
            query.close();
            return id.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Flight read(int id) {
        Connection conn = DataSource.getConnection();
        Flight flight = null;
        
        if (conn == null) {
            System.err.println("ERROR: Database connection is null in FlightDao.read(int)");
            return null;
        }
        
        String statement = "SELECT * FROM flights WHERE id = ?;";
        try {
            PreparedStatement query = conn.prepareStatement(statement);
            query.setInt(1, id);

            ResultSet res = query.executeQuery();

            if (res.next()) {
                try {
                    flight = new Flight();
                    flight.setId(res.getInt("id"));
                    flight.setDepDatetime(LocalDateTime.parse(res.getString("dep_datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    flight.setArrDatetime(LocalDateTime.parse(res.getString("arr_datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    flight.setFirstPrice(res.getDouble("first_price"));
                    flight.setBusinessPrice(res.getDouble("business_price"));
                    flight.setEconomyPrice(res.getDouble("economy_price"));
                    flight.setLuggagePrice(res.getDouble("luggage_price"));
                    flight.setWeightPrice(res.getDouble("weight_price"));
                    flight.setAirline(res.getInt("id_airline"));
                    
                    int depAirportId = res.getInt("dep_airport");
                    int arrAirportId = res.getInt("arr_airport");
                    var depAirport = airportDao.read(depAirportId);
                    var arrAirport = airportDao.read(arrAirportId);
                    
                    if (depAirport == null) {
                        System.err.println("WARNING: Departure airport with ID " + depAirportId + " not found for flight ID " + id);
                    }
                    if (arrAirport == null) {
                        System.err.println("WARNING: Arrival airport with ID " + arrAirportId + " not found for flight ID " + id);
                    }
                    
                    flight.setDepAirport(depAirport);
                    flight.setArrAirport(arrAirport);
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to parse flight with ID " + id + ": " + e.getMessage());
                    e.printStackTrace();
                    flight = null;
                }
            }

            query.close();

        } catch (SQLException e) {
            System.err.println("ERROR: SQLException in FlightDao.read(int): " + e.getMessage());
            e.printStackTrace();
        }

        return flight;
    }

    public List<Flight> read(Airline airline) {
        Connection conn = DataSource.getConnection();
        LinkedList<Flight> list = new LinkedList<>();

        if (conn == null) {
            System.err.println("ERROR: Database connection is null in FlightDao.read(Airline)");
            return list; // Return empty list instead of null
        }

        try {
            PreparedStatement query = conn.prepareStatement("SELECT * FROM flights WHERE id_airline = ? ORDER BY dep_datetime;");
            query.setInt(1, airline.getId());
            ResultSet res = query.executeQuery();
            while (res.next()) {
                try {
                    Flight flight = new Flight();
                    flight.setId(res.getInt("id"));
                    flight.setDepDatetime(LocalDateTime.parse(res.getString("dep_datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    flight.setArrDatetime(LocalDateTime.parse(res.getString("arr_datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    flight.setFirstPrice(res.getDouble("first_price"));
                    flight.setBusinessPrice(res.getDouble("business_price"));
                    flight.setEconomyPrice(res.getDouble("economy_price"));
                    flight.setLuggagePrice(res.getDouble("luggage_price"));
                    flight.setWeightPrice(res.getDouble("weight_price"));
                    flight.setAirline(res.getInt("id_airline"));
                    
                    int depAirportId = res.getInt("dep_airport");
                    int arrAirportId = res.getInt("arr_airport");
                    var depAirport = airportDao.read(depAirportId);
                    var arrAirport = airportDao.read(arrAirportId);
                    
                    if (depAirport == null || arrAirport == null) {
                        System.err.println("WARNING: Airport not found for flight ID " + res.getInt("id"));
                        continue;
                    }
                    
                    flight.setDepAirport(depAirport);
                    flight.setArrAirport(arrAirport);

                    list.addFirst(flight);
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to parse flight with ID " + res.getInt("id") + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            query.close();
            return list;
        } catch (SQLException e) {
            System.err.println("ERROR: SQLException in FlightDao.read(Airline): " + e.getMessage());
            e.printStackTrace();
            return list; // Return empty list instead of null
        }
    }

    @Override
    public List<Flight> readAll() {
        Connection conn = DataSource.getConnection();
        LinkedList<Flight> list = new LinkedList<>();

        if (conn == null) {
            System.err.println("ERROR: Database connection is null in FlightDao.readAll()");
            return list; // Return empty list instead of null
        }

        try {
            PreparedStatement query = conn.prepareStatement("SELECT * FROM flights;");
            ResultSet res = query.executeQuery();
            int count = 0;
            while (res.next()) {
                try {
                    Flight flight = new Flight();
                    flight.setId(res.getInt("id"));
                    flight.setDepDatetime(LocalDateTime.parse(res.getString("dep_datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    flight.setArrDatetime(LocalDateTime.parse(res.getString("arr_datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    flight.setFirstPrice(res.getDouble("first_price"));
                    flight.setBusinessPrice(res.getDouble("business_price"));
                    flight.setEconomyPrice(res.getDouble("economy_price"));
                    flight.setLuggagePrice(res.getDouble("luggage_price"));
                    flight.setWeightPrice(res.getDouble("weight_price"));
                    flight.setAirline(res.getInt("id_airline"));
                    
                    // Check if airports exist before setting them
                    int depAirportId = res.getInt("dep_airport");
                    int arrAirportId = res.getInt("arr_airport");
                    var depAirport = airportDao.read(depAirportId);
                    var arrAirport = airportDao.read(arrAirportId);
                    
                    if (depAirport == null) {
                        System.err.println("WARNING: Departure airport with ID " + depAirportId + " not found for flight ID " + res.getInt("id"));
                        continue; // Skip this flight if airport is missing
                    }
                    if (arrAirport == null) {
                        System.err.println("WARNING: Arrival airport with ID " + arrAirportId + " not found for flight ID " + res.getInt("id"));
                        continue; // Skip this flight if airport is missing
                    }
                    
                    flight.setDepAirport(depAirport);
                    flight.setArrAirport(arrAirport);
                    
                    list.addFirst(flight);
                    count++;
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to parse flight with ID " + res.getInt("id") + ": " + e.getMessage());
                    e.printStackTrace();
                    // Continue to next flight instead of failing completely
                }
            }
            query.close();
            System.out.println("INFO: Loaded " + count + " flights from database");
            return list;
        } catch (SQLException e) {
            System.err.println("ERROR: SQLException in FlightDao.readAll(): " + e.getMessage());
            e.printStackTrace();
            return list; // Return empty list instead of null
        }
    }

    @Override
    public void update(int id, Flight flight) {
        Connection conn = DataSource.getConnection();
        Flight original =  this.read(id);

        String statement = "UPDATE flights SET dep_datetime= ?, arr_datetime= ?, first_price= ?, business_price= ?, economy_price= ?, luggage_price= ?, weight_price= ?, id_airline= ?, dep_airport= ?, arr_airport= ? WHERE id = ?;";
        try {
            PreparedStatement query = conn.prepareStatement(statement);
            query.setInt(11, id);

            if (flight.getDepDatetime() != null) {
                query.setString(1, flight.getDepDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
            else {
                query.setString(1, original.getDepDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }

            if (flight.getArrDatetime() != null) {
                query.setString(2, flight.getArrDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
            else {
                query.setString(2, original.getArrDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }

            if (flight.getFirstPrice() != -1) {
                query.setDouble(3, flight.getFirstPrice());
            }
            else {
                query.setDouble(3, original.getFirstPrice());
            }

            if (flight.getBusinessPrice() != -1) {
                query.setDouble(4, flight.getBusinessPrice());
            }
            else {
                query.setDouble(4, original.getBusinessPrice());
            }
            
            if (flight.getEconomyPrice() != -1) {
                query.setDouble(5, flight.getEconomyPrice());
            }
            else {
                query.setDouble(5, original.getEconomyPrice());
            }

            if (flight.getLuggagePrice() != -1) {
                query.setDouble(6, flight.getLuggagePrice());
            }
            else {
                query.setDouble(6, original.getLuggagePrice());
            }

            if (flight.getWeightPrice() != -1) {
                query.setDouble(7, flight.getWeightPrice());
            }
            else {
                query.setDouble(7, original.getWeightPrice());
            }

            if (flight.getAirline() != null) {
                query.setInt(8, flight.getAirline().getId());
            }
            else {
                query.setInt(8, original.getAirline().getId());
            }

            if (flight.getDepAirport() != null) {
                query.setInt(9, flight.getDepAirport().getId());
            }
            else {
                query.setInt(9, original.getDepAirport().getId());
            }

            if (flight.getArrAirport() != null) {
                query.setInt(10, flight.getArrAirport().getId());
            }
            else {
                query.setInt(10, original.getArrAirport().getId());
            }

            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        Connection conn = DataSource.getConnection();
        try {
            PreparedStatement query = conn.prepareStatement("DELETE FROM flights WHERE id = ? ;");
            query.setInt(1, id);
            query.executeUpdate();
            query.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
