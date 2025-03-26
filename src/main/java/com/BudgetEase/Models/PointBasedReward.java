package com.BudgetEase.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PointBasedReward implements Reward{

    @Id
    private String rewardId;
    private String rewardName;
    private int pointsRequired;
    private LocalDateTime earnedAt;

    @DBRef
    private User user;

    public String getDescription(){
        //needs update
        return "";
    }

    public String getRewardType(){
        return "Points-Based";
    }

    public boolean canRedeem(int availablePoints) {
        return availablePoints >= pointsRequired;
    }

    public int getPointsShortfall(int availablePoints) {
        return (pointsRequired - availablePoints);
    }

}
