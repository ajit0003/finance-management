package com.aks.finance.tracker.services;

import com.aks.finance.tracker.beans.TransactionRequestBean;
import com.aks.finance.tracker.beans.TransactionResponseBean;
import com.aks.finance.tracker.models.Transaction;
import com.aks.finance.tracker.repositories.TransactionRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponseBean createTransaction(TransactionRequestBean requestBean) {

        Transaction transaction = Transaction.builder()
                                             .date(requestBean.getDate())
                                             .transactionType(requestBean.getTransactionType())
                                             .amount(requestBean.getAmount())
                                             .transactionCode(UUID.randomUUID().toString())
                                             .transactionCategory(requestBean.getTransactionCategory())
                                             .build();

        transaction = transactionRepository.save(transaction);

        return TransactionResponseBean.builder()
                                      .transactionType(transaction.getTransactionType())
                                      .date(transaction.getDate())
                                      .amount(transaction.getAmount())
                                      .transactionCategory(transaction.getTransactionCategory())
                                      .id(transaction.getId())
                                      .transactionCode(transaction.getTransactionCode())
                                      .build();
    }

    public Optional<TransactionResponseBean> getTransaction(Long id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        if(!optionalTransaction.isPresent()) {
            return Optional.empty();
        } else {
            Transaction transaction = optionalTransaction.get();
            return Optional.of(TransactionResponseBean
                                   .builder()
                                   .transactionCode(transaction.getTransactionCode())
                                   .id(transaction.getId())
                                   .transactionCategory(transaction.getTransactionCategory())
                                   .amount(transaction.getAmount())
                                   .transactionType(transaction.getTransactionType())
                                   .date(transaction.getDate())
                                   .build());
        }
    }

}