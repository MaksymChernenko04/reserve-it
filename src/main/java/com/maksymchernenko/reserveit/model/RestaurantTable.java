package com.maksymchernenko.reserveit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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

    public RestaurantTable(Restaurant restaurant, Integer seatsNumber) {
        this.restaurant = restaurant;
        this.seatsNumber = seatsNumber;

        restaurant.setNumberOfTables(restaurant.getNumberOfTables() + 1);
        number = restaurant.getNumberOfTables();
    }
}
