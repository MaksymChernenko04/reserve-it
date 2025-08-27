package com.maksymchernenko.reserveit.service;

import com.maksymchernenko.reserveit.model.WorkingTime;

import java.time.DayOfWeek;
import java.util.Map;

public interface WorkingTimeService {

    void createWorkingTime(WorkingTime workingTime);
    Map<DayOfWeek, WorkingTime> getWorkingTimeMap(long restaurantId);
    void delete(long restaurantId, DayOfWeek day);
}
