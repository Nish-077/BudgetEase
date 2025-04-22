package com.BudgetEase.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BudgetEase.BudgetEaseService.RewardService;
import com.BudgetEase.BudgetEaseService.UserService;
import com.BudgetEase.Models.BadgeReward;
import com.BudgetEase.Models.CompositeReward;
import com.BudgetEase.Models.PointBasedReward;
import com.BudgetEase.Models.Reward;
import com.BudgetEase.Models.RewardAccount;
import com.BudgetEase.utils.GetCurrentUser;

@RestController
@RequestMapping("/api/rewards")
public class RewardAccountController {

    private final RewardService rewardService;
    private final UserService userService;

    public RewardAccountController(RewardService rewardService, UserService userService) {
        this.rewardService = rewardService;
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<RewardAccountResponse> getRewardsForUser() {
        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);
        List<RewardAccount> accounts = rewardService.getAccountByUserId(getCurrentUser.obtainUser().getUserId());

        System.out.println(accounts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList()));

        return accounts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private RewardAccountResponse mapToDTO(RewardAccount account) {
        RewardAccountResponse dto = new RewardAccountResponse();
        dto.setUserId(account.getUserId());
        dto.setTotalPoints(account.getTotalPoints());

        // Flatten composite rewards before mapping to DTO
        List<Reward> flattenedRewards = flattenRewards(account.getRewards());
        List<RewardDTO> rewardDTOs = flattenedRewards.stream()
                .map(this::mapRewardToDTO)
                .collect(Collectors.toList());

        dto.setRewards(rewardDTOs);
        return dto;
    }

    private List<Reward> flattenRewards(List<Reward> rewards) {
        return rewards.stream()
                .flatMap(reward -> {
                    if (reward instanceof CompositeReward) {
                        CompositeReward composite = (CompositeReward) reward;
                        return flattenRewards(composite.getRewards()).stream();  // recursive flattening
                    } else {
                        return List.of(reward).stream();
                    }
                })
                .collect(Collectors.toList());
    }

    private RewardDTO mapRewardToDTO(Reward reward) {
        RewardDTO dto = new RewardDTO(reward);

        if (reward instanceof PointBasedReward) {
            PointBasedReward pointReward = (PointBasedReward) reward;
            dto.setPoints(pointReward.getPointsRequired());
            dto.setName(pointReward.getRewardName());  // assumes this exists
        } else if (reward instanceof BadgeReward) {
            BadgeReward badgeReward = (BadgeReward) reward;
            dto.setBadgeLevel(badgeReward.getBadgeLevel());
            dto.setName(badgeReward.getBadgeName());  // assumes this exists
        } else {
            dto.setRewardType("UNKNOWN");
        }

        return dto;
    }
}
