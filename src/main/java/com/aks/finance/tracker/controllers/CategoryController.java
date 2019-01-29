package com.aks.finance.tracker.controllers;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.aks.finance.tracker.beans.CategoryRequestBean;
import com.aks.finance.tracker.beans.CategoryResponseBean;
import com.aks.finance.tracker.services.CategoryService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CategoryResponseBean createCategory(@Valid @RequestBody CategoryRequestBean requestBean) {
        return categoryService.createCategory(requestBean);
    }
}
