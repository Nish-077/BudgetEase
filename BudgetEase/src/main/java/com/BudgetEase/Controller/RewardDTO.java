package com.BudgetEase.Controller;

import java.util.List;
import java.util.stream.Collectors;

import com.BudgetEase.Models.BadgeLevel;
import com.BudgetEase.Models.BadgeReward;
import com.BudgetEase.Models.CompositeReward;
import com.BudgetEase.Models.PointBasedReward;
import com.BudgetEase.Models.Reward;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RewardDTO {

    private String rewardType;
    private String name;
    private Integer points;
    private BadgeLevel badgeLevel;
    private List<RewardDTO> components;

    public RewardDTO(Reward reward){
        if(reward instanceof PointBasedReward){
            this.rewardType="POINT";
            this.name=((PointBasedReward)reward).getRewardName();
            this.points=((PointBasedReward)reward).getPointsRequired();
        }

        else if (reward instanceof BadgeReward){
            this.rewardType="BADGE";
            this.name=((BadgeReward)reward).getBadgeName();
            this.badgeLevel=((BadgeReward)reward).getBadgeLevel();
        }

        else if (reward instanceof CompositeReward){
            this.rewardType="COMPOSITE";
            this.name="Composite Reward";
            this.components = ((CompositeReward)reward).getRewards()
                .stream()
                .map(RewardDTO::new)
                .collect(Collectors.toList());
        }
    }

}
