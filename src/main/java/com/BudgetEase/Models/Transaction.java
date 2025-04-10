package com.BudgetEase.Models;

import java.time.LocalDateTime;

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

    private TransactionType type;

    private double amount;

    private LocalDateTime date;

    private String description;

    private String merchant;

    private PaymentStatus status;

    private String userId;
    private String budgetId;
    private String goalId;

}
