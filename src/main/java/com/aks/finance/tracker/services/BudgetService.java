package com.aks.finance.tracker.services;

import static java.util.Objects.isNull;

import com.aks.finance.tracker.beans.BudgetProgressResponseBean;
import com.aks.finance.tracker.beans.BudgetRequestBean;
import com.aks.finance.tracker.beans.BudgetResponseBean;
import com.aks.finance.tracker.enums.Category;
import com.aks.finance.tracker.enums.Month;
import com.aks.finance.tracker.models.Budget;
import com.aks.finance.tracker.models.Transaction;
import com.aks.finance.tracker.repositories.BudgetRepository;
import com.aks.finance.tracker.repositories.TransactionRepository;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    BudgetRepository budgetRepository;
    TransactionRepository transactionRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository, TransactionRepository transactionRepository) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
    }

    public BudgetResponseBean createBudget(BudgetRequestBean budgetRequestBean) {
        Budget budget = Budget.builder()
                              .budgetYear(budgetRequestBean.getBudgetYear())
                              .budgetMonth(budgetRequestBean.getBudgetMonth())
                              .budgetCategory(budgetRequestBean.getBudgetCatergory())
                              .budgetAmount(budgetRequestBean.getBudgetAmt())
                              .build();

        budget = budgetRepository.save(budget);

        return BudgetResponseBean.builder()
                                 .budgetAmt(budget.getBudgetAmount())
                                 .budgetCatergory(budget.getBudgetCategory())
                                 .budgetMonth(budget.getBudgetMonth())
                                 .budgetYear(budget.getBudgetYear())
                                 .id(budget.getId())
                                 .build();
    }

    public Optional<BudgetResponseBean> findById(Long id) {
        Optional<Budget> optionalBudget = budgetRepository.findById(id);

        return optionalBudget.map(budget -> BudgetResponseBean.builder()
                                                              .budgetAmt(budget.getBudgetAmount())
                                                              .budgetCatergory(budget.getBudgetCategory())
                                                              .budgetMonth(budget.getBudgetMonth())
                                                              .budgetYear(budget.getBudgetYear())
                                                              .id(budget.getId())
                                                              .build());

    }

    public Optional<BudgetProgressResponseBean> getBudgetProgress(Integer month, Integer year) {
        List<Budget> budgets = budgetRepository.findByMonthAndYear(month, year);

        if(isNull(budgets) || budgets.isEmpty()) {
            return Optional.empty();
        }

        Map<Category, Double> categoryBudget = budgets
            .stream()
            .collect(Collectors.toMap(Budget::getBudgetCategory, Budget::getBudgetAmount));

        double totalBudgetForMonth = budgets.stream().mapToDouble(Budget::getBudgetAmount).sum();

        List<Transaction> transactions = transactionRepository.findByMonthAndYear(month, year);

        double totalExpenditure = transactions.stream().mapToDouble(Transaction::getAmount).sum();

        Map<Category, Double> expenditureByCategory = transactions
            .stream()
            .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));

        Map<Category, Double> catergoryBalance = Maps.newHashMap();

        Arrays.stream(Category.values()).forEach(cat -> {
            double budget = categoryBudget.getOrDefault(cat, 0d);
            double expenditure = expenditureByCategory.getOrDefault(cat, 0d);

            catergoryBalance.put(cat, budget - expenditure);
        });

        return Optional.of(BudgetProgressResponseBean.builder()
                                                     .month(month)
                                                     .year(year)
                                                     .totalBudget(totalBudgetForMonth)
                                                     .totalBalance(totalBudgetForMonth - totalExpenditure)
                                                     .entertainmentBalance(catergoryBalance.get(Category.ENTERTAINMENT))
                                                     .foodBalance(catergoryBalance.get(Category.FOOD))
                                                     .medicalBalance(catergoryBalance.get(Category.MEDICAL))
                                                     .travelBalance(catergoryBalance.get(Category.TRAVEL))
                                                     .miscellaneousBalance(catergoryBalance.get(Category.MISCELLANEOUS))
                                                     .build());

    }
}
