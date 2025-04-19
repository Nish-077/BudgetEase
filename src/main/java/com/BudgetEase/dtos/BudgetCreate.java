package com.BudgetEase.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BudgetCreate {

    private String budgetId;

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double amount;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    @Future
    private LocalDateTime endDate;

    @NotBlank
    @Size(max = 50)
    private String categoryName;
}
