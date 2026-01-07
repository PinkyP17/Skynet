package com.skynet.passengerprofile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Passenger Entity - represents a passenger in the system
 * 
 * Maps to the 'passengers' table in the database
 */
@Entity
@Table(name = "passengers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "birthDate")
    private String birthDate; // SQLite stores as text, can parse to LocalDate if needed

    @Column(name = "gender")
    private String gender; // "M" or "F"

    @Column(name = "country")
    private String country;

    @Column(name = "profilePicture")
    private String profilePicture; // File path or URL

    // New columns added for enhancements
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "meal_preference")
    private String mealPreference; // "Vegetarian", "Halal", "Vegan", "None"

    @Column(name = "profile_completeness")
    private Integer profileCompleteness; // 0-100 percentage

    // Relationships
    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TravelDocument> travelDocuments;

    @OneToOne(mappedBy = "passenger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LoyaltyAccount loyaltyAccount;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavoriteRoute> favoriteRoutes;

    // Helper methods
    
    /**
     * Calculate profile completeness based on filled fields
     * @return percentage (0-100)
     */
    public int calculateProfileCompleteness() {
        int totalFields = 11; // Total editable fields
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
        
        // Bonus for having documents
        if (travelDocuments != null && !travelDocuments.isEmpty()) filledFields++;

        return (int) ((filledFields / (double) totalFields) * 100);
    }

    /**
     * Get full name
     * @return firstName + lastName
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
