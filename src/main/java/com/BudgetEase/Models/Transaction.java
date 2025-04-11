package com.BudgetEase.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "Transaction")
public class Transaction {
    @Id
    private String transactionId;

    @NotNull
    private TransactionType type;

    @NotNull
    @Min(value = (long)0.01)
    private double amount;

    @NotNull
    private LocalDateTime date;

    @NotBlank
    private String description;

    @NotBlank
    private String merchant;

    @NotNull
    private PaymentStatus status;

    private String userId;
    private String budgetId;
    private String goalId;

}
