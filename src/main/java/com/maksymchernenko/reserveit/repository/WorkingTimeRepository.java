package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.WorkingTime;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public interface WorkingTimeRepository {
    WorkingTime save(WorkingTime workingTime);
    Map<DayOfWeek, WorkingTime> getWorkingTimeMap(long restaurantId);
    List<WorkingTime> getByDaysNumber(long restaurantId, int daysNumber);
    void deleteAll(long restaurantId);
    void delete(long restaurantId, DayOfWeek day);
}
