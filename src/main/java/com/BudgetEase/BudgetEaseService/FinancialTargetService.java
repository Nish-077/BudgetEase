package com.BudgetEase.BudgetEaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BudgetEase.Models.FinancialTarget;
import com.BudgetEase.Strategies.OverdueStrategy;
import com.BudgetEase.Strategies.OverdueStrategyFactory;
import com.BudgetEase.Strategies.ProgressStrategy;
import com.BudgetEase.Strategies.ProgressStrategyFactory;

@Service
public class FinancialTargetService {

    @Autowired
    private ProgressStrategyFactory progressFactory;

    @Autowired
    private OverdueStrategyFactory overdueFactory;

    public double calculateProgress(FinancialTarget target) {
        ProgressStrategy strategy = progressFactory.getProgressStrategy(target);
        return strategy.calculateProgress(target);
    }

    public boolean checkIfOverdue(FinancialTarget target) {
        OverdueStrategy strategy = overdueFactory.gOverdueStrategy(target);
        return strategy.isOverdue(target);
    }

}
