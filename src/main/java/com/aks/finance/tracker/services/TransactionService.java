package com.aks.finance.tracker.services;

import com.aks.finance.tracker.beans.TransactionRequestBean;
import com.aks.finance.tracker.beans.TransactionResponseBean;
import com.aks.finance.tracker.enums.Month;
import com.aks.finance.tracker.enums.TransactionType;
import com.aks.finance.tracker.models.Expenditure;
import com.aks.finance.tracker.models.Transaction;
import com.aks.finance.tracker.repositories.ExpenditureRepository;
import com.aks.finance.tracker.repositories.TransactionRepository;
import java.time.Year;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private ExpenditureRepository expenditureRepository;

    public TransactionService(TransactionRepository transactionRepository, ExpenditureRepository expenditureRepository) {
        this.transactionRepository = transactionRepository;
        this.expenditureRepository = expenditureRepository;
    }

    public TransactionResponseBean createTransaction(TransactionRequestBean requestBean) {

        Transaction transaction = Transaction.builder()
                                             .date(requestBean.getDate())
                                             .transactionType(requestBean.getTransactionType())
                                             .amount(requestBean.getAmount())
                                             .transactionCode(UUID.randomUUID().toString())
                                             .categoryId(requestBean.getCategoryId())
                                             .build();

        transaction = transactionRepository.save(transaction);

        Optional<Expenditure> optionalExpenditure = expenditureRepository.findExpenditure(transaction.getCategoryId(),
                                                                                          transaction.getDate().getMonth().getValue(),
                                                                                          transaction.getDate().getYear());

        if(optionalExpenditure.isPresent()) {
            Expenditure expenditure = optionalExpenditure.get();
            double totalExpenditure = expenditure.getAmount();
            if(transaction.getTransactionType() == TransactionType.CREDIT) {
                totalExpenditure -= transaction.getAmount();
            } else {
                totalExpenditure += transaction.getAmount();
            }
            expenditure.setAmount(totalExpenditure);
            expenditureRepository.updateExpenditure(expenditure);
        } else {
            double amount = transaction.getTransactionType() == TransactionType.CREDIT
                            ? transaction.getAmount() *(-1)
                            : transaction.getAmount();
            Expenditure expenditure = Expenditure.builder()
                                                 .month(Month.fromValue(transaction.getDate().getMonth().getValue()))
                                                 .year(Year.of(transaction.getDate().getYear()))
                                                 .amount(amount)
                                                 .categoryId(transaction.getCategoryId())
                                                 .build();
            expenditureRepository.createExpenditure(expenditure);
        }

        return TransactionResponseBean.builder()
                                      .transactionType(transaction.getTransactionType())
                                      .date(transaction.getDate())
                                      .amount(transaction.getAmount())
                                      .categoryId(transaction.getCategoryId())
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
                                   .categoryId(transaction.getCategoryId())
                                   .amount(transaction.getAmount())
                                   .transactionType(transaction.getTransactionType())
                                   .date(transaction.getDate())
                                   .build());
        }
    }

}