package com.aks.finance.tracker.repositories.impl;

import static java.util.Objects.isNull;

import com.aks.finance.tracker.models.Category;
import com.aks.finance.tracker.repositories.CategoryRepository;
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
public class CategoryRepositoryImpl implements CategoryRepository {

    private SimpleJdbcInsert simpleJdbcInsert;
    private JdbcTemplate jdbcTemplate;

    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("categories")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Category create(Category category) {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("category", category.getCategory());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        category.setId(id);

        return category;
    }

    @Override
    public Optional<Category> findByName(String category) {
        try {
            Category categoryResult = jdbcTemplate.queryForObject("SELECT * FROM categories WHERE category = ?", new Object[] {category}, (rs, rowNum) -> {
                return Category.builder()
                               .id(rs.getLong("id"))
                               .category(rs.getString("category"))
                               .build();
            });

            return isNull(categoryResult) ? Optional.empty() : Optional.of(categoryResult);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Category> findAllCategories() {
        return jdbcTemplate.query("SELECT * FROM categories", (rs, rowNum) -> {
            return Category.builder()
                           .id(rs.getLong("id"))
                           .category(rs.getString("category"))
                           .build();
        });
    }
}
