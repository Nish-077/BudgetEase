package com.BudgetEase.Models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "budget")
public class Budget {
    @Id
    private String budgetId;

    @DBRef
    private User user;

    private double totalAmount;
    private double allocatedAmount;
    private double remainingAmount;

    @DBRef
    private List<Transaction> transactions;

}
