package com.aks.finance.tracker.services;

import com.aks.finance.tracker.beans.CategoryRequestBean;
import com.aks.finance.tracker.beans.CategoryResponseBean;
import com.aks.finance.tracker.models.Category;
import com.aks.finance.tracker.repositories.CategoryRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseBean createCategory(CategoryRequestBean requestBean) {
        Category category = Category.builder().category(requestBean.getCategory()).build();

        category = categoryRepository.create(category);

        return new CategoryResponseBean(category.getId(), category.getCategory());
    }

    public Optional<CategoryResponseBean> findByName(String category) {
        return categoryRepository.findByName(category)
                                 .map(cat -> new CategoryResponseBean(cat.getId(), cat.getCategory()));
    }

}
