package com.maksymchernenko.reserveit.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable table;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "day_time")
    private LocalDateTime dayTime;

    @Column(name = "guests_number")
    private Integer guestsNumber;

    public Reservation(RestaurantTable table, User client, User manager, Status status, LocalDateTime dayTime) {
        this.table = table;
        this.client = client;
        this.manager = manager;
        this.status = status;
        this.dayTime = dayTime;
    }
}
