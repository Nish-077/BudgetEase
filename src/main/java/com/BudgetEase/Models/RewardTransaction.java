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
@Document(collection = "reward_transactions")
public class RewardTransaction {

    @Id
    private String transactionId;
    private LocalDateTime timestamp;
    private int pointsChanged;

    public String getFormattedTransaction() {
        return String.format("Transaction ID: %s, Timestamp: %s, Points Changed: %d", 
                              transactionId, timestamp, pointsChanged);
    }
}
