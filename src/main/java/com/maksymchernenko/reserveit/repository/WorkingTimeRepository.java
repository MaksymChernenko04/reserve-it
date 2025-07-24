package com.maksymchernenko.reserveit.repository;

import com.maksymchernenko.reserveit.model.WorkingTime;

public interface WorkingTimeRepository {
    WorkingTime save(WorkingTime workingTime);
}
