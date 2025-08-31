package com.maksymchernenko.reserveit.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant in the system.
 * <p>
 * Has relationships with {@link WorkingTime} and {@link RestaurantTable},
 * stores name and address.
 */
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WorkingTime> workingTimes = new ArrayList<>();

    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RestaurantTable> restaurantTables = new ArrayList<>();

    /**
     * Instantiates a new Restaurant.
     *
     * @param name    the restaurant name
     * @param address the restaurant address
     */
    public Restaurant(String name,
                      String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
