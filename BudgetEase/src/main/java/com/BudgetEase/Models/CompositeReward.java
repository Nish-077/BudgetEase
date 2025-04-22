package com.BudgetEase.Models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "composite-rewards")
public class CompositeReward extends Reward {

    private List<Reward> rewards;

    public CompositeReward(List<Reward> rewards) {
        this.rewards = rewards;
    }

    @Override
    public String getRewardType() {
        return "Composite";
    }

    public List<Reward> getRewards() {
        return rewards;
    }
}
