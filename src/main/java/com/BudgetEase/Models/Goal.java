package com.BudgetEase.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "Goal")
public class Goal extends FinancialTarget{
    @Id
    private String goalId;

    private String purpose;

    @Override
    public boolean isOverdue(){
        return false;
    }

    @Override
    public double progress(){
        return 0.0;
    }

}
