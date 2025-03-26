package com.BudgetEase.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String transactionId;
    private double amount;
    private LocalDateTime date;
    private TransactionType type;
    private String description;

    @DBRef
    private User user;
    @DBRef
    private Category category;

    public boolean isExpense(){
        return this.type == TransactionType.EXPENSE;
    }

    public boolean isIncome(){
        return this.type == TransactionType.INCOME;
    }
}
