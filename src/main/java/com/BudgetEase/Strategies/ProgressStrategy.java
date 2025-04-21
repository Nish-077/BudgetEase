package com.BudgetEase.Strategies;

import com.BudgetEase.Models.FinancialTarget;

public interface ProgressStrategy {
    public double calculateProgress(FinancialTarget financialTarget);
}
