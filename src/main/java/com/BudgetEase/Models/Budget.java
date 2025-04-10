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
@Document(collection = "Budget")
public class Budget extends FinancialTarget {

    @Id
    private String budgetId;

    private String categoryName;

    @Override
    public double progress(){
        return 0.0;
    }

    @Override
    public boolean isOverdue(){
        return false;
    }
}
