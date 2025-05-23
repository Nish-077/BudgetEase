package com.BudgetEase.Models;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private LocalDateTime createdAt;
    private String profilePictureUrl;

    @DBRef
    private RewardAccount rewardAccount;
   
    @DBRef
    private List<FinancialTarget> plans;
    @DBRef
    private List<Notification> notifications;
    @DBRef
    private List<Budget> budgets;
    @DBRef
    private List<Goal> goals;

}
