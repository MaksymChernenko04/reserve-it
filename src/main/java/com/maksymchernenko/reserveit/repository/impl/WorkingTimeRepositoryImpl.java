package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.WorkingTime;
import com.maksymchernenko.reserveit.repository.WorkingTimeRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WorkingTimeRepositoryImpl implements WorkingTimeRepository {

    private final EntityManager entityManager;

    @Autowired
    public WorkingTimeRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public WorkingTime save(WorkingTime workingTime) {
        if (workingTime.getId() == null) {
            entityManager.persist(workingTime);

            return workingTime;
        } else {
            return entityManager.merge(workingTime);
        }
    }

    @Override
    public Map<DayOfWeek, WorkingTime> getWorkingTimeMap(long restaurantId) {
        List<WorkingTime> workingTimes = entityManager.createQuery("FROM WorkingTime WHERE restaurant.id = :id", WorkingTime.class)
                .setParameter("id", restaurantId)
                .getResultList();

        Map<DayOfWeek, WorkingTime> workingTimeMap = new HashMap<>();
        for (WorkingTime workingTime : workingTimes) {
            workingTimeMap.put(workingTime.getDayOfWeek(), workingTime);
        }

        return workingTimeMap;
    }

    @Override
    public List<WorkingTime> getByDaysNumber(long restaurantId, int daysNumber) {
        DayOfWeek start = LocalDate.now().getDayOfWeek();
        List<DayOfWeek> days = new ArrayList<>(daysNumber);
        for (int i = 0; i < daysNumber; i++) {
            days.add(start.plus(i));
        }

        return entityManager.createQuery(
                        "SELECT wt FROM WorkingTime wt " +
                                "WHERE wt.restaurant.id = :id AND wt.dayOfWeek IN :days " +
                                "ORDER BY wt.dayOfWeek", WorkingTime.class)
                .setParameter("id", restaurantId)
                .setParameter("days", days)
                .getResultList();
    }

    @Override
    public void deleteAll(long restaurantId) {
        entityManager.createQuery("DELETE FROM WorkingTime WHERE restaurant.id = :id")
                .setParameter("id", restaurantId)
                .executeUpdate();
    }

    @Override
    public void delete(long restaurantId, DayOfWeek day) {
        entityManager.createQuery("DELETE FROM WorkingTime WHERE restaurant.id = :id AND dayOfWeek = :day")
                .setParameter("id", restaurantId)
                .setParameter("day", day)
                .executeUpdate();
    }
}
