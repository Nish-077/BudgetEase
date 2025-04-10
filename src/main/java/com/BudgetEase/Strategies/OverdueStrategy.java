package com.BudgetEase.Strategies;

import com.BudgetEase.Models.FinancialTarget;

public interface OverdueStrategy {
    public boolean isOverdue(FinancialTarget financialTarget);
}
