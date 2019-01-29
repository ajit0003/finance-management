package com.aks.finance.tracker.controllers;

import com.aks.finance.tracker.beans.CategoryResponseBean;
import com.aks.finance.tracker.beans.TransactionRequestBean;
import com.aks.finance.tracker.beans.TransactionResponseBean;
import com.aks.finance.tracker.services.CategoryService;
import com.aks.finance.tracker.services.TransactionService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private TransactionService transactionService;
    private CategoryService categoryService;

    public TransactionController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequestBean requestBean) {
        Optional<CategoryResponseBean> optCatBean = categoryService.findByName(requestBean.getCategory());
        if(!optCatBean.isPresent()) {
            return ResponseEntity.badRequest().body("Category provided does not exist.");
        } else {
            requestBean.setCategoryId(optCatBean.get().getId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(requestBean));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> getTransaction(@PathVariable Long id) {
        return transactionService.getTransaction(id)
                                 .map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.notFound().build());
    }

}