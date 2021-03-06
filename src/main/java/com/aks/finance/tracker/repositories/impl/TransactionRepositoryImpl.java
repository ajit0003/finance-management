package com.aks.finance.tracker.repositories.impl;

import static java.util.Objects.isNull;

import com.aks.finance.tracker.enums.TransactionType;
import com.aks.finance.tracker.models.Transaction;
import com.aks.finance.tracker.repositories.TransactionRepository;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public TransactionRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("transactions")
        .usingGeneratedKeyColumns("id");
    }

    @Override
    public Transaction save(Transaction transaction) {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("transaction_type", transaction.getTransactionType().getType());
        parameters.put("transaction_code", transaction.getTransactionCode());
        parameters.put("transaction_amount", transaction.getAmount());
        parameters.put("transaction_date", transaction.getDate());
        parameters.put("category_id", transaction.getCategoryId());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        transaction.setId(id);

        return transaction;
    }

    @Override
    public boolean update(Transaction transaction) {
        return jdbcTemplate.update("UPDATE transactions " +
                                   "SET transaction_type = ?, transaction_code = ?, transaction_amount = ?, trasnaction_date = ?, category_id = ? " +
                                   "WHERE id = ?",
        transaction.getTransactionType().getType(),
        transaction.getTransactionCode(),
        transaction.getAmount(),
        transaction.getDate(),
        transaction.getCategoryId(),
        transaction.getId()) == 1;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        try {
            Transaction transaction = jdbcTemplate.queryForObject("SELECT * " +
                                                                  "FROM transactions " +
                                                                  "WHERE id = ?",
                                                                  new Object[] {id},
            (rs, rowNum) -> {
                return Transaction
                    .builder()
                    .id(rs.getLong("id"))
                    .transactionType(TransactionType.fromValue(rs.getString("transaction_type")))
                    .transactionCode(rs.getString("transaction_code"))
                    .amount(rs.getDouble("transaction_amount"))
                    .date(rs.getTimestamp("transaction_date").toLocalDateTime())
                    .categoryId(rs.getLong("category_id"))
                    .build();
            });

            return isNull(transaction) ? Optional.empty() : Optional.of(transaction);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Transaction> findByMonthAndYear(Integer month, Integer year) {
        return jdbcTemplate.query("SELECT * FROM transactions WHERE MONTH(transaction_date) = ? AND YEAR(transaction_date) = ?", new Object[] {month, year},
        (rs, rowNum) -> {
            return Transaction
                .builder()
                .id(rs.getLong("id"))
                .transactionType(TransactionType.fromValue(rs.getString("transaction_type")))
                .transactionCode(rs.getString("transaction_code"))
                .amount(rs.getDouble("transaction_amount"))
                .date(rs.getTimestamp("transaction_date").toLocalDateTime())
                .categoryId(rs.getLong("category_id"))
                .build();
        });
    }

}