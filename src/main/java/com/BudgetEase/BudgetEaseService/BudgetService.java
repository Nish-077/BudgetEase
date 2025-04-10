package com.BudgetEase.BudgetEaseService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.repository.BudgetRepository;

@Service
public class BudgetService {

    private BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository){
        this.budgetRepository=budgetRepository;
    }

    public Budget createBudget(Budget budget){
        return budgetRepository.save(budget);
    }

    public Budget getBudgetById(String budgetId){
        Budget budget = budgetRepository.findById(budgetId).orElseThrow( () -> new IllegalArgumentException("Budget with this id") );

        return budget;
    }

    public List<Budget> getBudgetByCategory(String category){
        List<Budget> budgets = budgetRepository.findBudgetByCategoryName(category);

        return budgets;
    }

    public Budget updateBudget(Budget budget){
        Budget budgetExists = budgetRepository.findById(budget.getBudgetId()).orElseThrow( () -> new IllegalArgumentException("Budget id doesn't ") );

        budgetExists.setBudgetId(budget.getBudgetId());
        budgetExists.setCategoryName(budget.getCategoryName());
        budgetExists.setAmount(budget.getAmount());
        budgetExists.setName(budget.getName());
        budgetExists.setStartDate(budget.getStartDate());
        budgetExists.setEndDate(budget.getEndDate());

        return budgetRepository.save(budgetExists);
    }

}
