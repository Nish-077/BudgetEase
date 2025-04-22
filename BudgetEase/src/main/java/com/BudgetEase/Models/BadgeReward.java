package com.BudgetEase.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "badges")
public class BadgeReward extends Reward{
    @Id
    private String badgeId;
    private String badgeName;
    private BadgeLevel badgeLevel;

    @Override
    public String getRewardType() {
        return "Badge";
    }
}
