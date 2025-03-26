package com.BudgetEase.Models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "budgets")
public class Budget {

    @Id
    private String budgetId;
    private double allocatedAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @DBRef
    private User user;
    @DBRef
    private List<Category> category;

    public boolean isWithinBudget(double currentSpending) {
        return currentSpending <= this.allocatedAmount;
    }

    public double remainingAmount(double currentSpending) {
        return (this.allocatedAmount - currentSpending);
    }

    public boolean isActiveOn(LocalDateTime date) {
        return ((date.isAfter(this.startDate) || date.isEqual(this.startDate)) &&
                (date.isBefore(this.endDate) || date.isEqual(this.endDate)));
    }

}
