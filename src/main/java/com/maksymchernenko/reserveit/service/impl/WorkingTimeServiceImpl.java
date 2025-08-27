package com.maksymchernenko.reserveit.service.impl;

import com.maksymchernenko.reserveit.model.WorkingTime;
import com.maksymchernenko.reserveit.repository.WorkingTimeRepository;
import com.maksymchernenko.reserveit.service.WorkingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Map;

@Service
public class WorkingTimeServiceImpl implements WorkingTimeService {

    private final WorkingTimeRepository workingTimeRepository;

    @Autowired
    public WorkingTimeServiceImpl(WorkingTimeRepository workingTimeRepository) {
        this.workingTimeRepository = workingTimeRepository;
    }

    @Transactional
    @Override
    public void createWorkingTime(WorkingTime workingTime) {
        Map<DayOfWeek, WorkingTime> workingTimeMap = workingTimeRepository.getWorkingTimeMap(workingTime.getRestaurant().getId());

        DayOfWeek yesterday = workingTime.getDayOfWeek().minus(1);
        WorkingTime yesterdayWorkingTime = workingTimeMap.get(yesterday);
        if (yesterdayWorkingTime != null
                && yesterdayWorkingTime.getOpenTime().isAfter(yesterdayWorkingTime.getCloseTime())
                && yesterdayWorkingTime.getCloseTime().isAfter(workingTime.getOpenTime())) {
            if (yesterdayWorkingTime.getCloseTime().isAfter(workingTime.getCloseTime())) return;

            workingTime.setOpenTime(yesterdayWorkingTime.getCloseTime());
        }

        workingTimeRepository.save(workingTime);
    }

    @Override
    public Map<DayOfWeek, WorkingTime> getWorkingTimeMap(long restaurantId) {
        return workingTimeRepository.getWorkingTimeMap(restaurantId);
    }

    @Transactional
    @Override
    public void delete(long restaurantId,
                       DayOfWeek day) {
        workingTimeRepository.delete(restaurantId, day);
    }
}
