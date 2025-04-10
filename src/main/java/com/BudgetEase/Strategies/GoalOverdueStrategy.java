package com.BudgetEase.Strategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.BudgetEase.BudgetEaseService.TransactionService;
import com.BudgetEase.Models.FinancialTarget;
import com.BudgetEase.Models.Goal;

@Component
public class GoalOverdueStrategy implements OverdueStrategy {

    @Autowired
    private TransactionService transactionService;

    @Override
    public boolean isOverdue(FinancialTarget financialTarget){
        Goal goal = (Goal) financialTarget;

        return transactionService.getCurrentGain(goal.getGoalId()) < goal.getAmount() && goal.isActive();

    }
}
