package com.aks.finance.tracker.controllers;

import static java.util.Objects.isNull;

import com.aks.finance.tracker.beans.BudgetRequestBean;
import com.aks.finance.tracker.beans.CategoryResponseBean;
import com.aks.finance.tracker.services.BudgetService;
import com.aks.finance.tracker.services.CategoryService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private BudgetService budgetService;
    private CategoryService categoryService;

    public BudgetController(BudgetService budgetService, CategoryService categoryService) {
        this.budgetService = budgetService;
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<?> createBudget(@Valid @RequestBody BudgetRequestBean budgetRequestBean) {
        Optional<CategoryResponseBean> optCatBean = categoryService.findByName(budgetRequestBean.getBudgetCategory());
        if(!optCatBean.isPresent()) {
            return ResponseEntity.badRequest().body("Category provided does not exist.");
        } else {
            budgetRequestBean.setCategoryId(optCatBean.get().getId());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(budgetRequestBean));
    }

    @GetMapping("/progress")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> getProgress(@RequestParam("month") Integer month, @RequestParam("year") Integer year) {
        if(isNull(month) || isNull(year) || month <= 0 || year <= 0) {
            return ResponseEntity.badRequest().body("Please provide a valid month and year as an Integer.");
        }

        return budgetService.getBudgetProgress(month, year)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> getBudget(@PathVariable Long id) {
        return budgetService.findById(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

}
