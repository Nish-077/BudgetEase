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
public class PointBasedReward extends Reward {

    @Id
    private String rewardId;
    private String rewardName;
    private int pointsRequired;

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
