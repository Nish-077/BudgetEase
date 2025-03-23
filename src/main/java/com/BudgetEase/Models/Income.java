package com.BudgetEase.Models;

// import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
// import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
// @NoArgsConstructor
@AllArgsConstructor
@Document(collection = "income")
public class Income extends Transaction {
    // public Income(String transactionId, Category category, double amount, LocalDateTime date, String description,User user,Budget budget ){
    //     super(transactionId,category,amount,date,description,user,budget);
    // }

}
