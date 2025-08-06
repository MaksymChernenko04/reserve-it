package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.model.WorkingTime;

import java.time.DayOfWeek;
import java.util.Map;

public interface WorkingTimeService {
    WorkingTime createWorkingTime(WorkingTime workingTime);
    Map<DayOfWeek, WorkingTime> getWorkingTimeMap(long id);
    void deleteAll(long restaurantId);
    void delete(long restaurantId, DayOfWeek day);
}
