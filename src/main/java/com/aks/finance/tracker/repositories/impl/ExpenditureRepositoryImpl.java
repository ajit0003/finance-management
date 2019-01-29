package com.aks.finance.tracker.repositories.impl;

import static java.util.Objects.isNull;

import com.aks.finance.tracker.enums.Month;
import com.aks.finance.tracker.models.Expenditure;
import com.aks.finance.tracker.repositories.ExpenditureRepository;
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
public class ExpenditureRepositoryImpl implements ExpenditureRepository {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public ExpenditureRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("expenditures")
        .usingGeneratedKeyColumns("id");
    }

    @Override
    public int updateExpenditure(Expenditure expenditure) {
        return jdbcTemplate.update("UPDATE expenditures SET amopunt = ? WHERE id = ?", expenditure.getAmount(), expenditure.getId());
    }

    @Override
    public Expenditure createExpenditure(Expenditure expenditure) {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("category_id", expenditure.getCategoryId());
        parameters.put("amount", expenditure.getAmount());
        parameters.put("expenditure_year", expenditure.getYear().getValue());
        parameters.put("expenditure_month", expenditure.getMonth().getMonth());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        expenditure.setId(id);

        return expenditure;
    }

    @Override
    public Optional<Expenditure> findExpenditure(Long categoryId, Integer month, Integer year) {
        try {
            Expenditure expenditure = jdbcTemplate.queryForObject("SELECT * " +
                                                                  "FROM expenditures " +
                                                                  "WHERE category_id = ? " +
                                                                  "AND expendiutre_month = ? " +
                                                                  "AND expenditure_year = ?",
                                                                  new Object[] {categoryId, month, year}, (rs, rowNum) -> {
                return Expenditure
                    .builder()
                    .id(rs.getLong("id"))
                    .year(Year.of(rs.getInt("expenditure_year")))
                    .amount(rs.getDouble("amount"))
                    .month(Month.fromValue(rs.getInt("expenditure_month")))
                    .categoryId(rs.getLong("category_id"))
                    .build();
                });

            return isNull(expenditure) ? Optional.empty() : Optional.of(expenditure);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Expenditure> findAllExpendituresByMonthAndYear(Integer month, Integer year) {
        return jdbcTemplate.query("SELECT * FROM expenditures WHERE expenditure_month = ? AND expenditure_year= ?", new Object[]{month, year}, (rs, rowNum) -> {
            return Expenditure.builder()
                              .amount(rs.getDouble("amount"))
                              .categoryId(rs.getLong("category_id"))
                              .month(Month.fromValue(rs.getInt("expenditure_month")))
                              .year(Year.of(rs.getInt("expenditure_year")))
                              .id(rs.getLong("id"))
                              .build();
        });
    }
}
