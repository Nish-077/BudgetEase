package com.BudgetEase.Strategies;

// import java.beans.JavaBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.FinancialTarget;
import com.BudgetEase.Models.Goal;

@Component
public class ProgressStrategyFactory {

    @Autowired
    private BudgetProgressStrategy budgetProgressStrategy;

    @Autowired
    private GoalProgressStrategy goalProgressStrategy;

    public ProgressStrategy getProgressStrategy(FinancialTarget financialTarget){
        if(financialTarget instanceof Budget) return budgetProgressStrategy;
        if(financialTarget instanceof Goal) return goalProgressStrategy;

        throw new IllegalArgumentException("Unknown target type");
    }

}
