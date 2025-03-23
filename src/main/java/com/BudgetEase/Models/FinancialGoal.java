package com.BudgetEase.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "financialGoal")
public class FinancialGoal {
    @Id
    private String goalId;

    @DBRef
    private User user;

    private String goalName;
    private double targetAmount;
    private double currentAmount;
    private LocalDateTime deadline;
}
