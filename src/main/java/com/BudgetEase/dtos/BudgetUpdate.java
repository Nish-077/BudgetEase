package com.BudgetEase.dtos;

import java.time.LocalDateTime;

import com.BudgetEase.Models.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetUpdate {
    private String budgetName;

    private double allocatedAmount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Category category;
}
