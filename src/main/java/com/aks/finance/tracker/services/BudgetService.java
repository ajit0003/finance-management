package com.aks.finance.tracker.services;

import com.aks.finance.tracker.beans.BudgetProgressResponseBean;
import com.aks.finance.tracker.beans.BudgetRequestBean;
import com.aks.finance.tracker.beans.BudgetResponseBean;
import com.aks.finance.tracker.models.Budget;
import com.aks.finance.tracker.models.Category;
import com.aks.finance.tracker.models.Expenditure;
import com.aks.finance.tracker.repositories.BudgetRepository;
import com.aks.finance.tracker.repositories.CategoryRepository;
import com.aks.finance.tracker.repositories.ExpenditureRepository;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    private BudgetRepository budgetRepository;
    private ExpenditureRepository expenditureRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository,
                         ExpenditureRepository expenditureRepository,
                         CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.expenditureRepository = expenditureRepository;
        this.categoryRepository = categoryRepository;
    }

    public BudgetResponseBean createBudget(BudgetRequestBean budgetRequestBean) {
        Budget budget = Budget.builder()
                              .budgetYear(budgetRequestBean.getBudgetYear())
                              .budgetMonth(budgetRequestBean.getBudgetMonth())
                              .categoryId(budgetRequestBean.getCategoryId())
                              .budgetAmount(budgetRequestBean.getBudgetAmt())
                              .build();

        budget = budgetRepository.save(budget);

        return BudgetResponseBean.builder()
                                 .budgetAmt(budget.getBudgetAmount())
                                 .categoryId(budget.getCategoryId())
                                 .category(budgetRequestBean.getBudgetCategory())
                                 .budgetMonth(budget.getBudgetMonth())
                                 .budgetYear(budget.getBudgetYear())
                                 .id(budget.getId())
                                 .build();
    }

    public Optional<BudgetResponseBean> findById(Long id) {
        Optional<Budget> optionalBudget = budgetRepository.findById(id);

        return optionalBudget.map(budget -> BudgetResponseBean.builder()
                                                              .budgetAmt(budget.getBudgetAmount())
                                                              .categoryId(budget.getCategoryId())
                                                              .budgetMonth(budget.getBudgetMonth())
                                                              .budgetYear(budget.getBudgetYear())
                                                              .id(budget.getId())
                                                              .build());

    }

    public Optional<BudgetProgressResponseBean> getBudgetProgress(Integer month, Integer year) {
        List<Category> categories = categoryRepository.findAllCategories();
        List<Budget> budgets = budgetRepository.findByMonthAndYear(month, year);
        List<Expenditure> expenditures = expenditureRepository.findAllExpendituresByMonthAndYear(month, year);

        double totalBudget = budgets.stream().mapToDouble(Budget::getBudgetAmount).sum();
        double totalExpenditure = expenditures.stream().mapToDouble(Expenditure::getAmount).sum();

        Map<String, Double> categoryBalance = Maps.newHashMap();

        for(Category category : categories) {
            double budget = budgets.stream()
                                   .filter(b -> b.getCategoryId().longValue() == category.getId().longValue())
                                   .mapToDouble(Budget::getBudgetAmount)
                                   .findFirst()
                                   .orElse(0d);

            double expenditure = expenditures.stream()
                                             .filter(e -> e.getCategoryId().longValue() == category.getId().longValue())
                                             .mapToDouble(Expenditure::getAmount)
                                             .findFirst()
                                             .orElse(0d);

            categoryBalance.put(category.getCategory(), budget - expenditure);
        }

        return Optional.of(BudgetProgressResponseBean.builder()
                                                     .year(year)
                                                     .month(month)
                                                     .totalBalance(totalBudget - totalExpenditure)
                                                     .categoryBalance(categoryBalance)
                                                     .totalBudget(totalBudget)
                                                     .build());

    }

//    public Optional<BudgetProgressResponseBean> getBudgetProgress(Integer month, Integer year) {
//        List<Budget> budgets = budgetRepository.findByMonthAndYear(month, year);
//
//        if(isNull(budgets) || budgets.isEmpty()) {
//            return Optional.empty();
//        }
//
//        Map<Category, Double> categoryBudget = budgets
//            .stream()
//            .collect(Collectors.toMap(Budget::getBudgetCategory, Budget::getBudgetAmount));
//
//        double totalBudgetForMonth = budgets.stream().mapToDouble(Budget::getBudgetAmount).sum();
//
//        List<Transaction> transactions = transactionRepository.findByMonthAndYear(month, year);
//
//        List<Transaction> creditTransactions = transactions
//            .stream()
//            .filter(t -> t.getTransactionType() == TransactionType.CREDIT)
//            .collect(Collectors.toList());
//
//        List<Transaction> debitTransactions = transactions
//            .stream()
//            .filter(t -> t.getTransactionType() == TransactionType.DEBIT)
//            .collect(Collectors.toList());
//
//        double totalNetExpenditure = debitTransactions.stream().mapToDouble(Transaction::getAmount).sum()
//            - creditTransactions.stream().mapToDouble(Transaction::getAmount).sum();
//
//        Map<Category, Double> expenditureByCategory = transactions
//            .stream()
//            .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
//
//        Map<Category, Double> catergoryBalance = Maps.newHashMap();
//
//        Arrays.stream(Category.values()).forEach(cat -> {
//            double budget = categoryBudget.getOrDefault(cat, 0d);
//            double expenditure = expenditureByCategory.getOrDefault(cat, 0d);
//
//            catergoryBalance.put(cat, budget - expenditure);
//        });
//
//        return Optional.of(BudgetProgressResponseBean.builder()
//                                                     .month(month)
//                                                     .year(year)
//                                                     .totalBudget(totalBudgetForMonth)
//                                                     .totalBalance(totalBudgetForMonth - totalNetExpenditure)
//                                                     .entertainmentBalance(catergoryBalance.get(Category.ENTERTAINMENT))
//                                                     .foodBalance(catergoryBalance.get(Category.FOOD))
//                                                     .medicalBalance(catergoryBalance.get(Category.MEDICAL))
//                                                     .travelBalance(catergoryBalance.get(Category.TRAVEL))
//                                                     .miscellaneousBalance(catergoryBalance.get(Category.MISCELLANEOUS))
//                                                     .build());
//
//    }
}
