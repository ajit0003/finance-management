package com.aks.finance.tracker.services;

import com.aks.finance.tracker.models.Transaction;
import com.aks.finance.tracker.repositories.TransactionRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Optional<Transaction> createTransaction(Transaction transaction) {
        return Optional.empty();
    }
}