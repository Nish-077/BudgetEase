package com.BudgetEase.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "reward_accounts")
public class RewardAccount {

    @Id
    private String accountId;
    private int totalPoints;

    @DBRef
    private List<RewardTransaction> transactions;
    @DBRef
    private List<Reward> rewards;

    public void addTransaction(RewardTransaction transaction) {
        this.transactions.add(transaction);
    }
}
