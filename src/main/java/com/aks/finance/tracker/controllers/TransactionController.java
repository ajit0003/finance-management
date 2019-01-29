package com.aks.finance.tracker.controllers;

import com.aks.finance.tracker.beans.TransactionRequestBean;
import com.aks.finance.tracker.beans.TransactionResponseBean;
import com.aks.finance.tracker.services.TransactionService;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private Validator validator;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TransactionResponseBean createTransaction(@RequestBody TransactionRequestBean requestBean) {

        Set<ConstraintViolation<TransactionRequestBean>> violations = validator.validate(requestBean);

        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return TransactionResponseBean.builder().build();
    }
}