package com.maksymchernenko.reserveit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column
    private Integer number;

    @Column(name = "seats_number")
    private Integer seatsNumber;

    public RestaurantTable(Restaurant restaurant, Integer number, Integer seatsNumber) {
        this.restaurant = restaurant;
        this.number = number;
        this.seatsNumber = seatsNumber;
    }
}
