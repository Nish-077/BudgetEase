package com.BudgetEase.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public abstract class FinancialPlan {
    protected String categoryName;
    protected String description;
    protected boolean remindersEnabled;
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;
    protected int rewardPoints;

    public boolean isActiveOn(LocalDateTime date) {
        return (startDate != null && endDate != null) &&
               (date.isEqual(startDate) || date.isAfter(startDate)) &&
               (date.isEqual(endDate) || date.isBefore(endDate));
    }

    abstract double progressPercentage(double currentSpending);
}