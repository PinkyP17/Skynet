package com.skynet.passengerprofile.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Passenger Entity - represents a passenger in the system
 *
 * Maps to the 'passengers' table in the database
 */
@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "birthDate")
    private String birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "country")
    private String country;

    @Column(name = "profilePicture")
    private String profilePicture;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "meal_preference")
    private String mealPreference;

    @Column(name = "profile_completeness")
    private Integer profileCompleteness;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TravelDocument> travelDocuments;

    @OneToOne(mappedBy = "passenger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LoyaltyAccount loyaltyAccount;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavoriteRoute> favoriteRoutes;

    // Constructors
    public Passenger() {}

    public Passenger(Long id, String firstName, String lastName, String birthDate, String gender,
                     String country, String profilePicture, String phoneNumber, String address,
                     String nationality, String mealPreference, Integer profileCompleteness) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.country = country;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.nationality = nationality;
        this.mealPreference = mealPreference;
        this.profileCompleteness = profileCompleteness;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getMealPreference() { return mealPreference; }
    public void setMealPreference(String mealPreference) { this.mealPreference = mealPreference; }

    public Integer getProfileCompleteness() { return profileCompleteness; }
    public void setProfileCompleteness(Integer profileCompleteness) { this.profileCompleteness = profileCompleteness; }

    public List<TravelDocument> getTravelDocuments() { return travelDocuments; }
    public void setTravelDocuments(List<TravelDocument> travelDocuments) { this.travelDocuments = travelDocuments; }

    public LoyaltyAccount getLoyaltyAccount() { return loyaltyAccount; }
    public void setLoyaltyAccount(LoyaltyAccount loyaltyAccount) { this.loyaltyAccount = loyaltyAccount; }

    public List<FavoriteRoute> getFavoriteRoutes() { return favoriteRoutes; }
    public void setFavoriteRoutes(List<FavoriteRoute> favoriteRoutes) { this.favoriteRoutes = favoriteRoutes; }

    // Helper methods
    public int calculateProfileCompleteness() {
        int totalFields = 11;
        int filledFields = 0;

        if (firstName != null && !firstName.isEmpty()) filledFields++;
        if (lastName != null && !lastName.isEmpty()) filledFields++;
        if (birthDate != null && !birthDate.isEmpty()) filledFields++;
        if (gender != null && !gender.isEmpty()) filledFields++;
        if (country != null && !country.isEmpty()) filledFields++;
        if (phoneNumber != null && !phoneNumber.isEmpty()) filledFields++;
        if (address != null && !address.isEmpty()) filledFields++;
        if (nationality != null && !nationality.isEmpty()) filledFields++;
        if (mealPreference != null && !mealPreference.isEmpty()) filledFields++;
        if (profilePicture != null && !profilePicture.isEmpty()) filledFields++;
        if (travelDocuments != null && !travelDocuments.isEmpty()) filledFields++;

        return (int) ((filledFields / (double) totalFields) * 100);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
