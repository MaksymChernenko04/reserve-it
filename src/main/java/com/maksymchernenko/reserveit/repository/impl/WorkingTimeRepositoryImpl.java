package com.maksymchernenko.reserveit.repository.impl;

import com.maksymchernenko.reserveit.model.WorkingTime;
import com.maksymchernenko.reserveit.repository.WorkingTimeRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WorkingTimeRepositoryImpl implements WorkingTimeRepository {

    private final EntityManager entityManager;

    @Autowired
    public WorkingTimeRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public WorkingTime save(WorkingTime workingTime) {
        entityManager.persist(workingTime);

        return entityManager.find(WorkingTime.class, workingTime.getId());
    }
}
