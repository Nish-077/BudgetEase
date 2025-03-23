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
public abstract class Transaction {
    @Id
    private String transactionId;

    @DBRef
    private Category category;

    private double amount;
    private LocalDateTime date;
    private String description;

    @DBRef
    private User user;

    @DBRef
    private Budget budget;

    // public abstract void applyToBudget(Budget budget);
}
