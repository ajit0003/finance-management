package com.aks.finance.tracker.repositories;

import com.aks.finance.tracker.models.Expenditure;
import java.util.List;
import java.util.Optional;

public interface ExpenditureRepository {

    public int updateExpenditure(Expenditure expenditure);

    public Expenditure createExpenditure(Expenditure expenditure);

    public Optional<Expenditure> findExpenditure(Long categoryId, Integer month, Integer year);

    public List<Expenditure> findAllExpendituresByMonthAndYear(Integer month, Integer year);

}
