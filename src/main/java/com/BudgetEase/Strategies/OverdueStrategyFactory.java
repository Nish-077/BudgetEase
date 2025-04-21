package com.BudgetEase.Strategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.FinancialTarget;
import com.BudgetEase.Models.Goal;

@Component
public class OverdueStrategyFactory {

    @Autowired
    private BudgetOverdueStrategy budgetOverdueStrategy;

    @Autowired
    private GoalOverdueStrategy goalOverdueStrategy;

    public OverdueStrategy gOverdueStrategy(FinancialTarget financialTarget){
        if(financialTarget instanceof Budget) return budgetOverdueStrategy;
        if(financialTarget instanceof Goal) return goalOverdueStrategy;

        throw new IllegalArgumentException("Argument invalid!");
    }

}
