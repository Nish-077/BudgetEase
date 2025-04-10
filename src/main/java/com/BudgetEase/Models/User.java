package com.BudgetEase.Models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String userId;

    @NotBlank(message = "Username is mandatory")
    private String userName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Input is mandatory!")
    private String email;

    @Size(min=10, max=10, message = "Enter Valid 10 digit Phone Number")
    private String phoneNumber;

    private String passwordHash;
    private LocalDateTime createdAt;

    private String profilePictureUrl;

    @DBRef
    private RewardAccount rewardAccount;
    
    @DBRef
    private List<Budget> budgets;
    
    @DBRef
    private List<Goal> goals;
    @DBRef
    private List<Reward> rewards;
    @DBRef
    private List<Notification> notifications;

}
