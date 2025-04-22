package com.BudgetEase.BudgetEaseService;

import com.BudgetEase.Models.*;
import com.BudgetEase.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RewardService {

    @Autowired
    private BadgeRewardRepository badgeRewardRepository;

    @Autowired
    private PointRewardRepository pointRewardRepository;

    @Autowired
    private CompositeRewardRepository compositeRewardRepository;

    @Autowired
    private RewardAccountRepository rewardAccountRepository;

    // -- Creation Helpers (single responsibility) --

    private PointBasedReward createPointReward(String name, int points) {
        return new PointBasedReward(null, name, points);
    }

    private BadgeReward createBadgeReward(String name, BadgeLevel level) {
        return new BadgeReward(null, name, level);
    }

    private CompositeReward createCompositeReward(List<Reward> rewards) {
        return new CompositeReward(rewards);
    }

    // -- Save Methods --

    public PointBasedReward savePointReward(PointBasedReward reward) {
        return pointRewardRepository.save(reward);
    }

    public BadgeReward saveBadgeReward(BadgeReward reward) {
        return badgeRewardRepository.save(reward);
    }

    public CompositeReward saveCompositeReward(CompositeReward reward) {
        return compositeRewardRepository.save(reward);
    }

    // -- Reward Grant Logic (matches your business rules) --

    // Grant reward for adding budget or goal
    public Reward rewardForAddingBudgetOrGoal(String userId) {
        Reward reward = savePointReward(createPointReward("Added Budget/Goal", 10));
        applyRewardToUser(userId, reward);
        return reward;
    }

    // Grant reward for adding transaction
    public Reward rewardForAddingTransaction(String userId) {
        Reward reward = savePointReward(createPointReward("Added Transaction", 25));
        applyRewardToUser(userId, reward);
        return reward;
    }

    // Grant reward for reaching milestone of 5 transactions
    public Reward rewardForTwentyTransactions(String userId) {
        Reward reward = savePointReward(createPointReward("Milestone: 20 Transactions", 50));
        applyRewardToUser(userId, reward);
        return reward;
    }

    // Grant reward for completing budget or goal, possibly with early completion and 5 goals
// public Reward rewardForBudgetGoalCompletion(String userId, boolean early, boolean fiveCompleted) {
//     List<Reward> rewards = new ArrayList<>();

//     // Add completion reward (100 points) and Bronze Badge
//     rewards.add(createPointReward("Completion Reward", 100));
//     rewards.add(createBadgeReward("Completion", BadgeLevel.BRONZE));

//     // Add Silver Badge for early completion
//     if (early) {
//         rewards.add(createBadgeReward("Early Completion", BadgeLevel.SILVER));
//     }

//     // Add Gold Badge for completing 5 goals
//     if (fiveCompleted) {
//         rewards.add(createBadgeReward("Master Finisher", BadgeLevel.GOLD));
//     }

//     // Save all individual rewards first
//     List<Reward> savedRewards = new ArrayList<>();
//     for (Reward reward : rewards) {
//         if (reward instanceof PointBasedReward) {
//             Reward saved = savePointReward((PointBasedReward) reward);
//             savedRewards.add(saved);
//             applyRewardToUser(userId, saved);   // Apply the reward to user's RewardAccount
//         } else if (reward instanceof BadgeReward) {
//             Reward saved = saveBadgeReward((BadgeReward) reward);
//             savedRewards.add(saved);
//             applyRewardToUser(userId, saved);   // Apply the reward to user's RewardAccount
//         }
//     }

//     // Save the composite reward that groups them
//     Reward composite = saveCompositeReward(createCompositeReward(savedRewards));

//     applyRewardToUser(userId, composite);  // Apply the composite reward to the user too

//     return composite;
// }

public Reward rewardForBudgetGoalCompletion(String userId, boolean early, boolean fiveCompleted) {
    List<Reward> rewards = new ArrayList<>();

    // Create rewards
    rewards.add(createPointReward("Completion Reward", 100));
    rewards.add(createBadgeReward("Completion", BadgeLevel.BRONZE));
    if (early) rewards.add(createBadgeReward("Early Completion", BadgeLevel.SILVER));
    if (fiveCompleted) rewards.add(createBadgeReward("Master Finisher", BadgeLevel.GOLD));

    // Save all rewards (but don't apply them individually)
    List<Reward> savedRewards = new ArrayList<>();
    for (Reward reward : rewards) {
        if (reward instanceof PointBasedReward) {
            savedRewards.add(savePointReward((PointBasedReward) reward));
        } else if (reward instanceof BadgeReward) {
            savedRewards.add(saveBadgeReward((BadgeReward) reward));
        }
    }

    // Save composite reward and apply it to user only
    Reward composite = saveCompositeReward(createCompositeReward(savedRewards));
    applyRewardToUser(userId, composite);

    return composite;
}


    public void applyRewardToUser(String userId, Reward reward) {
        Optional<RewardAccount> optionalAccount = rewardAccountRepository.findById(userId);

        RewardAccount account = optionalAccount.orElseGet(() ->
            // new RewardAccount(userId, 0, new ArrayList<>(), new ArrayList<>())
            // new RewardAccount(null, 0, userId, null, null)
            new RewardAccount(null, 0, userId, new ArrayList<>())
        );

        // if (reward instanceof PointBasedReward) {
        //     account.setTotalPoints(account.getTotalPoints() + ((PointBasedReward) reward).getPointsRequired());
        // }

        account.getRewards().add(reward);

        rewardAccountRepository.save(account);
    }

    public List<RewardAccount> getAccountByUserId(String userId){
        return rewardAccountRepository.findByUserId(userId);
    }

}
