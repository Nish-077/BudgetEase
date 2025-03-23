package com.BudgetEase.Models;

import java.time.LocalDateTime;
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
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String userId;
    private String username;
    private String passwordHash;
    private String phoneNumber;
    private String email;
    private LocalDateTime createdAt;
    private String profilePictureUrl;

    @DBRef
    private List<Transaction> transactions;

    @DBRef
    private List<FinancialGoal> goals;

}
