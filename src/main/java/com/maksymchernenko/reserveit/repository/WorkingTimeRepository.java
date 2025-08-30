package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.WorkingTime;
import com.maksymchernenko.reserveit.model.Restaurant;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for accessing and managing {@link WorkingTime} entries in the database.
 * <p>
 * Provides CRUD methods.
 */
public interface WorkingTimeRepository {

    /**
     * Saves working time.
     *
     * @param workingTime the working time
     * @return the saved working time
     */
    WorkingTime save(WorkingTime workingTime);

    /**
     * Gets a working time map by {@link Restaurant} id.
     *
     * @param restaurantId the restaurant id
     * @return the working time map
     */
    Map<DayOfWeek, WorkingTime> getWorkingTimeMap(long restaurantId);

    /**
     * Gets working times by {@link Restaurant} id and a number of days.
     *
     * @param restaurantId the restaurant id
     * @param daysNumber   the number of days
     * @return the list of working times
     */
    List<WorkingTime> getByDaysNumber(long restaurantId,
                                      int daysNumber);

    /**
     * Deletes a working time by {@link Restaurant} id and a {@link DayOfWeek}.
     *
     * @param restaurantId the restaurant id
     * @param day          the day of the week
     */
    void delete(long restaurantId,
                DayOfWeek day);
}
