package com.BudgetEase.Strategies;

import org.springframework.beans.factory.annotation.Autowired;

import com.BudgetEase.BudgetEaseService.TransactionService;
import com.BudgetEase.Models.FinancialTarget;
import com.BudgetEase.Models.Goal;

public class GoalProgressStrategy implements ProgressStrategy {

    @Autowired
    private TransactionService transactionService;

    @Override
    public double calculateProgress(FinancialTarget financialTarget){
        Goal goal = (Goal) financialTarget;

        return transactionService.getCurrentGain(goal.getGoalId()) / goal.getAmount();
    }

}
