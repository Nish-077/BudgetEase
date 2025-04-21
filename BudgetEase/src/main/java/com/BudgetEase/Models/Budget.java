package com.BudgetEase.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Budget") 
public class Budget extends FinancialTarget {

    @Id
    private String budgetId;
    private double allocatedAmount;

    @Override
    public double progressPercentage(double currentSpending) {
        if (allocatedAmount == 0) {
            return 0;
        }
        return ((allocatedAmount - currentSpending) / allocatedAmount) * 100;
    }

    public boolean isWithinBudget(double currentSpendings) {
        return currentSpendings <= allocatedAmount;
    }

    public double remainingAmount(double currentSpendings) {
        return allocatedAmount - currentSpendings;
    }

    public double calculateOverspending(double currentSpendings) {
        if (currentSpendings > allocatedAmount) {
            return currentSpendings - allocatedAmount;
        }
        return 0;
    }

    public void resetBudget() {
        this.allocatedAmount = 0;
    }

    public boolean isNearingLimit(double currentSpendings, double thresholdPercentage) {
        double thresholdAmount = allocatedAmount * (thresholdPercentage / 100);
        return currentSpendings >= (allocatedAmount - thresholdAmount);
    }
}
