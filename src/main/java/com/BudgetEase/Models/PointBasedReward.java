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
@Document(collection = "point-rewards")
public class PointBasedReward implements Reward {

    @Id
    private String rewardId;
    private String rewardName;
    private String description;
    private int pointsRequired;
    private String iconUrl;
    private LocalDateTime earnedAt;

    @Override
    public String getRewardType() {
        return "Points-Based";
    }

    public boolean canRedeem(int availablePoints) {
        return availablePoints >= pointsRequired;
    }

    public int getPointsShortfall(int availablePoints) {
        return (pointsRequired - availablePoints);
    }

}
