package com.BudgetEase.Models;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public boolean isExpense(){
        return type == TransactionType.EXPENSE;
    }
    public boolean isIncome(){
        return type == TransactionType.INCOME;
    }

}
