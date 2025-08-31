package com.maksymchernenko.reserveit.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Represents a restaurant working time in the system.
 * <p>
 * Has a relationship with {@link Restaurant} and stores day of week, restaurant open and close time.
 */
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "working_time")
public class WorkingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Restaurant restaurant;

    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    /**
     * Instantiates a new Working time.
     *
     * @param restaurant the restaurant
     * @param dayOfWeek  the day of the week
     * @param openTime   the restaurant open time
     * @param closeTime  the restaurant close time
     */
    public WorkingTime(Restaurant restaurant,
                       DayOfWeek dayOfWeek,
                       LocalTime openTime,
                       LocalTime closeTime) {
        this.restaurant = restaurant;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    @Override
    public String toString() {
        return "WorkingTime{" +
                "id=" + id +
                ", restaurant=" + restaurant +
                ", dayOfWeek=" + dayOfWeek +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                '}';
    }
}
