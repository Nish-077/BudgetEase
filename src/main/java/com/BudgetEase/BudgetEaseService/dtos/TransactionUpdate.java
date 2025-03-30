package com.BudgetEase.BudgetEaseService.dtos;

import java.time.LocalDateTime;

import com.BudgetEase.Models.Category;
import com.BudgetEase.Models.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionUpdate {
    private double amount;

    private LocalDateTime date;

    private TransactionType type;

    private String description;

    private Category category;

}
