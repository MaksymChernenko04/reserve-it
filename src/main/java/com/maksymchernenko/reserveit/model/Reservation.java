package com.maksymchernenko.reserveit.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a reservation in the system.
 * <p>
 * Has relationships with {@link RestaurantTable}, {@link User} (client),
 * {@link User} (manager), and stores {@link Status}, date, time
 * and number of guests.
 */
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

    private Status status;

    @Column(name = "day_time")
    private LocalDateTime dayTime;

    @Column(name = "guests_number")
    private Integer guestsNumber;

    /**
     * Instantiates a new Reservation.
     *
     * @param table        the reserved table
     * @param client       the client
     * @param manager      the manager
     * @param status       the reservation status
     * @param dayTime      the reservation day and time
     * @param guestsNumber the number of guests
     */
    public Reservation(RestaurantTable table,
                       User client,
                       User manager,
                       Status status,
                       LocalDateTime dayTime,
                       Integer guestsNumber) {
        this.table = table;
        this.client = client;
        this.manager = manager;
        this.status = status;
        this.dayTime = dayTime;
        this.guestsNumber = guestsNumber;
    }

    /**
     * Represents a reservation status in the system.
     */
    @NoArgsConstructor
    public enum Status {
        /**
         * The reservation is created and is waiting for manager review.
         */
        PENDING,
        /**
         * The reservation has been confirmed by a manager.
         */
        RESERVED,
        /**
         * The reservation has been canceled by either the manager or the client.
         */
        CANCELED,
        /**
         * The reservation is finished, meaning the time reserved for it has passed.
         */
        FINISHED
    }
}
