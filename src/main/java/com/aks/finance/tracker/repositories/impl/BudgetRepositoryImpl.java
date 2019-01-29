package com.aks.finance.tracker.repositories.impl;

import static java.util.Objects.isNull;

import com.aks.finance.tracker.enums.Category;
import com.aks.finance.tracker.enums.Month;
import com.aks.finance.tracker.models.Budget;
import com.aks.finance.tracker.repositories.BudgetRepository;
import com.google.common.collect.Maps;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class BudgetRepositoryImpl implements BudgetRepository {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public BudgetRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("budgets")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Budget save(Budget budget) {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("budget_amt", budget.getBudgetAmount());
        parameters.put("budget_month", budget.getBudgetMonth().getMonth());
        parameters.put("budget_year", budget.getBudgetYear().getValue());
        parameters.put("budget_category", budget.getBudgetCategory().getCategory());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        budget.setId(id);

        return budget;
    }

    @Override
    public Optional<Budget> findById(Long id) {
        try {
            Budget budget = jdbcTemplate.queryForObject("SELECT * FROM budgets WHERE id = ?", new Object[]{id}, (rs, rowNum) -> {
                return Budget
                    .builder()
                    .budgetAmount(rs.getDouble("budget_amt"))
                    .budgetCategory(Category.fromValue(rs.getString("budget_category")))
                    .budgetMonth(Month.fromValue(rs.getInt("budget_month")))
                    .budgetYear(Year.of(rs.getInt("budget_year")))
                    .id(rs.getLong("id"))
                    .build();
                });

            return isNull(budget) ? Optional.empty() : Optional.of(budget);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Budget> findByMonthAndYear(Integer month, Integer year) {
        return jdbcTemplate.query("SELECT * FROM budgets WHERE budget_month = ? AND budget_year= ?", new Object[]{month, year}, (rs, rowNum) -> {
            return Budget.builder()
                         .budgetAmount(rs.getDouble("budget_amt"))
                         .budgetCategory(Category.fromValue(rs.getString("budget_category")))
                         .budgetMonth(Month.fromValue(rs.getInt("budget_month")))
                         .budgetYear(Year.of(rs.getInt("budget_year")))
                         .build();
        });

    }
}
