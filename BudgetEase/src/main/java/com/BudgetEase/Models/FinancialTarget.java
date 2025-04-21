package com.BudgetEase.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class FinancialTarget {
    protected String categoryName;
    protected String description;
    protected String name;
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;
    protected int rewardPoints;

    public boolean isActiveOn(LocalDateTime date) {
        return (startDate != null && endDate != null) &&
               (date.isEqual(startDate) || date.isAfter(startDate)) &&
               (date.isEqual(endDate) || date.isBefore(endDate));
    }

    public boolean startDateBeforeEndDate(){
        return this.startDate.isBefore(this.endDate);
    }

    abstract double progressPercentage(double currentSpending);
}