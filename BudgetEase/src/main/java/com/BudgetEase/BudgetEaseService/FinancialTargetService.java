package com.BudgetEase.BudgetEaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BudgetEase.Models.FinancialTarget;
import com.BudgetEase.Models.TargetStatus;
import com.BudgetEase.Strategies.OverdueStrategy;
import com.BudgetEase.Strategies.OverdueStrategyFactory;
import com.BudgetEase.Strategies.ProgressStrategy;
import com.BudgetEase.Strategies.ProgressStrategyFactory;

@Service
public class FinancialTargetService {

    @Autowired
    private ProgressStrategyFactory progressFactory;

    @Autowired
    private OverdueStrategyFactory overdueFactory;

    private RewardService rewardService;

    public double calculateProgress(FinancialTarget target, Boolean early, String userId) {
        ProgressStrategy strategy = progressFactory.getProgressStrategy(target);

        Boolean isFiveTimes=false;
        if(strategy.calculateProgress(target) == 100){
            target.setTargetStatus(TargetStatus.COMPLETED);
            target.setNoOfTimesCompleted(target.getNoOfTimesCompleted()+1);
            if(target.getNoOfTimesCompleted()==5){
                isFiveTimes=true;
            }
            rewardService.rewardForBudgetGoalCompletion(userId,early,isFiveTimes);
        }

        return strategy.calculateProgress(target);
    }

    public boolean checkIfOverdue(FinancialTarget target) {
        OverdueStrategy strategy = overdueFactory.gOverdueStrategy(target);
        return strategy.isOverdue(target);
    }

}
