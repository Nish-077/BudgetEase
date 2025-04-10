package com.BudgetEase.Models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class FinancialTarget {
    protected String name;

    protected double amount;

    protected LocalDateTime startDate;

    protected LocalDateTime endDate;

    public boolean isActive(){
        return ( LocalDateTime.now().isAfter(this.startDate) && LocalDateTime.now().isBefore(this.endDate) );
    }

    public boolean startDateBeforeEndDate(){
        return ( this.startDate.isBefore(this.endDate) );
    }

    public abstract double progress();

    public abstract boolean isOverdue();


}
