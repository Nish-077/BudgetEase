package com.BudgetEase.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "badges")
public class BadgeReward implements Reward{
    @Id
    private String badgeId;
    private String badgeName;
    private BadgeLevel badgeLevel;
    private String iconUrl;
    private LocalDateTime earnedAt;
    private String description;

    @Override
    public String getRewardType() {
        return "Badge";
    }
}
