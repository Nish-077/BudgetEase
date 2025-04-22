package com.BudgetEase.Strategies;

import com.BudgetEase.Models.FinancialTarget;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.BudgetEase.BudgetEaseService.TransactionService;
import com.BudgetEase.Models.Budget;

@Component
public class BudgetOverdueStrategy implements OverdueStrategy {

    @Autowired
    private TransactionService transactionService;

    @Override
    public boolean isOverdue(FinancialTarget financialPlan){
        Budget budget = (Budget) financialPlan;

        return transactionService.getCurrentSpending(budget.getBudgetId()) >= budget.getAllocatedAmount() && budget.isActiveOn(LocalDateTime.now());
    }

}
