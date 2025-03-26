package com.BudgetEase.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "goals")
public class Goal {

    @Id
    private String goalId;
    private String goalName;
    private double targetAmount;
    private double currentAmount;
    private LocalDateTime deadline;
    private GoalStatus status;

    @DBRef
    private User user;

}
