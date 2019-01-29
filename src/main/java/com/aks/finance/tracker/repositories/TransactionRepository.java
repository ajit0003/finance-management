package com.aks.finance.tracker.repositories;

import com.aks.finance.tracker.models.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    boolean update(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findByMonthAndYear(Integer month, Integer year);
}