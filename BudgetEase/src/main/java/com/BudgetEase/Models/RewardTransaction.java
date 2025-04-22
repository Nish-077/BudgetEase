package com.BudgetEase.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reward_transactions")
public class RewardTransaction {

    @Id
    private String rewardTransactionId;
    private LocalDateTime timestamp;
    private int pointsChanged;

    public String getFormattedTransaction() {
        return String.format("Transaction ID: %s, Timestamp: %s, Points Changed: %d", 
                              rewardTransactionId, timestamp, pointsChanged);
    }
}
