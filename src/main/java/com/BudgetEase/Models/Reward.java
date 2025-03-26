package com.BudgetEase.Models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rewards")
public interface Reward {
    String getRewardType();
    String getDescription();
}

