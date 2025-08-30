package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.model.Restaurant;
import com.maksymchernenko.reserveit.model.WorkingTime;

import java.time.DayOfWeek;
import java.util.Map;

/**
 * Service interface for managing {@link WorkingTime} objects.
 * <p>
 * Defines business logic methods to manage working times.
 */
public interface WorkingTimeService {

    /**
     * Creates a new working time.
     *
     * @param workingTime the new working time
     */
    void createWorkingTime(WorkingTime workingTime);

    /**
     * Gets a working time map by {@link Restaurant} id.
     *
     * @param restaurantId the restaurant id
     * @return the working time map
     */
    Map<DayOfWeek, WorkingTime> getWorkingTimeMap(long restaurantId);

    /**
     * Deletes a working time by {@link Restaurant} id.
     *
     * @param restaurantId the restaurant id
     * @param day          the {@link DayOfWeek}
     */
    void delete(long restaurantId,
                DayOfWeek day);
}
