package com.BudgetEase.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "goals")
public class Goal extends FinancialTarget {
    @Id
    private String goalId;
    private double targetAmount;
    private GoalStatus status;
    private String purpose;

    @Override
    public double progressPercentage(double currentSpending) {
        if (targetAmount == 0) {
            return 0;
        }
        return (currentSpending / targetAmount) * 100;
    }

    public boolean isOverDue(double currentSpendings) {
        return LocalDateTime.now().isAfter(endDate);
    }

    public void markAsCompleted() {
        this.status = GoalStatus.COMPLETED;
    }

    public void extendDeadline(int days) {
        endDate = endDate.plusDays(days);
    }
}
