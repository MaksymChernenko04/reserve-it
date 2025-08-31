package com.maksymchernenko.reserveit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

/**
 * Represents a restaurant table in the system.
 * <p>
 * Has a relationship with {@link Restaurant} and stores table number and number of seats.
 */
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "restaurant_table")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Restaurant restaurant;

    @Column
    private Integer number;

    @Column(name = "seats_number")
    private Integer seatsNumber;

    /**
     * Instantiates a new Restaurant table.
     *
     * @param restaurant  the restaurant
     * @param seatsNumber the number of seats
     */
    public RestaurantTable(Restaurant restaurant,
                           Integer seatsNumber) {
        this.restaurant = restaurant;
        this.seatsNumber = seatsNumber;
    }

    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", restaurant=" + restaurant +
                ", number=" + number +
                ", seatsNumber=" + seatsNumber +
                '}';
    }
}
