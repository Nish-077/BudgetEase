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
    private String userName;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private String profilePictureUrl;

    @DBRef
    private RewardAccount rewardAccount;
    @DBRef
    private List<Transaction> transactions;
    @DBRef
    private List<Budget> budgets;
    @DBRef
    private List<Category> categories;
    @DBRef
    private List<Goal> goals;
    @DBRef
    private List<Reward> rewards;
    @DBRef
    private List<Notification> notifications;

    public boolean isValidUserName(String name) {
        //logic
        return true;
    }

    public boolean isValidEmail(String email) {
        //logic
        return true;
    }

    public boolean isValidPassword(String password) {
        //logic
        return true;
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        //logic
        return true;
    }

}
