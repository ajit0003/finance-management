package com.aks.finance.tracker.repositories;

import com.aks.finance.tracker.models.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category create(Category category);

    Optional<Category> findByName(String category);

    List<Category> findAllCategories();
}
