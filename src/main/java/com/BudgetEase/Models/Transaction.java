package com.BudgetEase.Models;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String transactionId;
    private double amount;
    private LocalDateTime date;
    private TransactionType type;
    private String description;
    private String merchant;
    private PaymentStatus status;

    public boolean isExpense(){
        return type == TransactionType.EXPENSE;
    }
    public boolean isIncome(){
        return type == TransactionType.INCOME;
    }

}
