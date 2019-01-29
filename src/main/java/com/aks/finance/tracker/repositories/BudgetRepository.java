package com.aks.finance.tracker.repositories;

import com.aks.finance.tracker.models.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository {

    Budget save(Budget transaction);

    Optional<Budget> findById(Long id);

    List<Budget> findByMonthAndYear(Integer month, Integer year);
}
