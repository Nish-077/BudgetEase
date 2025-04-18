package com.BudgetEase.Strategies;

import com.BudgetEase.Models.FinancialPlan;

public interface OverdueStrategy {
    public boolean isOverdue(FinancialPlan financialPlan);
}
