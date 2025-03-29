package com.BudgetEase.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.BudgetEase.Models.TransactionType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Field must not be empty!")
    @Min(value = (long) 0.01, message = "Amount cannot be zero")
    private double amount;

    @NotNull(message = "Field must not be empty!")
    private LocalDateTime date;

    @NotNull(message = "Field must not be empty!")
    private TransactionType type;

    private String description;

    @DBRef
    @NotNull(message = "User should not be null")
    private User user;
    @DBRef
    private Category category;

    public boolean isExpense(){
        // return this.type == TransactionType.EXPENSE;
        return this.type == TransactionType.EXPENSE;
    }

    public boolean isIncome(){
        return this.type == TransactionType.INCOME;
    }
}
