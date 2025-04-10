package com.BudgetEase.Strategies;

import org.springframework.beans.factory.annotation.Autowired;

import com.BudgetEase.BudgetEaseService.TransactionService;
import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.FinancialTarget;

public class BudgetOverdueStrategy implements OverdueStrategy {

    @Autowired
    private TransactionService transactionService;

    @Override
    public boolean isOverdue(FinancialTarget financialTarget){
        Budget budget = (Budget) financialTarget;

        return transactionService.getCurrentSpending(budget.getBudgetId()) > budget.getAmount() && budget.isActive();
    }

}
