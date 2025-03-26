package com.BudgetEase.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BadgeReward implements Reward{

    @Id
    private String badgeId;
    private String badgeName;
    private BadgeLevel badgeLevel;
    private String iconUrl;
    private LocalDateTime earnedAt;

    @DBRef
    private User user;

    public String getDescription() {
        //needs update
        return "";
    }

    public String getRewardType() {
        return "Badge";
    }

}
