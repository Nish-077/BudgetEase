package com.BudgetEase.Strategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.BudgetEase.BudgetEaseService.TransactionService;
import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.FinancialTarget;

@Component
public class BudgetProgressStrategy implements ProgressStrategy {

    @Autowired
    private TransactionService transactionService;

    @Override
    public double calculateProgress(FinancialTarget financialTarget){
        Budget budget = (Budget) financialTarget;
        double currentSpending = transactionService.getCurrentSpending(budget.getBudgetId());
        return currentSpending/budget.getAllocatedAmount();
    }
}
