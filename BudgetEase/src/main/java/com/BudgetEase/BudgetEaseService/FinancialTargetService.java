package com.BudgetEase.BudgetEaseService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.FinancialTarget;
import com.BudgetEase.Models.Goal;
import com.BudgetEase.Models.NotificationLevel;
import com.BudgetEase.Models.NotificationType;
import com.BudgetEase.Models.TargetStatus;
import com.BudgetEase.Strategies.OverdueStrategy;
import com.BudgetEase.Strategies.OverdueStrategyFactory;
import com.BudgetEase.Strategies.ProgressStrategy;
import com.BudgetEase.Strategies.ProgressStrategyFactory;
import com.BudgetEase.repository.BudgetRepository;
import com.BudgetEase.repository.GoalRepository;

@Service
public class FinancialTargetService {

    @Autowired
    private ProgressStrategyFactory progressFactory;

    @Autowired
    private OverdueStrategyFactory overdueFactory;

    private RewardService rewardService;

    private BudgetRepository budgetRepository;
    private GoalRepository goalRepository;
    private NotificationService notificationService;


    public FinancialTargetService(RewardService rewardService, GoalRepository goalRepository, BudgetRepository budgetRepository, NotificationService notificationService){
        this.rewardService=rewardService;
        this.goalRepository=goalRepository;
        this.budgetRepository=budgetRepository;
        this.notificationService=notificationService;
    }

    public double calculateProgress(FinancialTarget target, String userId) {
        ProgressStrategy strategy = progressFactory.getProgressStrategy(target);

        Boolean early = false;

        if(LocalDateTime.now().isBefore(target.getEndDate())){
            early=true;
        }

        Boolean isFiveTimes=false;
        double progress = strategy.calculateProgress(target);
        // System.out.println("PROGRESS CALCULATED : "+ progress);
        // System.out.println("PROGRESS IN TARGET : "+ target.getProgress());

        // if(target.getProgress() != progress){
        //     System.out.println("IN FIRST != IF");
        //     target.setProgress(progress);
        //     System.out.println("PROGRESS AFTER SETTING : "+ target.getProgress());

        // }

        if(progress >= 100){

            if(target.getProgress() != progress){
                target.setProgress(progress);
                rewardService.rewardForBudgetGoalCompletion(userId,early,isFiveTimes);
            }

            target.setTargetStatus(TargetStatus.COMPLETED);

            // if(target.getNoOfTimesCompleted() )

            // target.setNoOfTimesCompleted(target.getNoOfTimesCompleted()+1);
            // if(target.getNoOfTimesCompleted()==5){
            //     isFiveTimes=true;
            // }

        }

        if(target instanceof Budget){

            if(progress>=100){
                notificationService.createNotification("BUDGET EXCEEDED", NotificationType.BUDGET_WARNING, NotificationLevel.ALERT);
            }

            Budget budget = (Budget) target;
            budgetRepository.save(budget);
        }

        if(target instanceof Goal){

            if(progress>=100){
                notificationService.createNotification("GOAL COMPLETED", NotificationType.GOAL_COMPLETED, NotificationLevel.SUCCESS);
            }

            Goal goal = (Goal) target;
            goalRepository.save(goal);
        }

        return progress;
    }

    public boolean checkIfOverdue(FinancialTarget target) {
        OverdueStrategy strategy = overdueFactory.gOverdueStrategy(target);

        Boolean check = strategy.isOverdue(target);

        if(target instanceof Budget){

            if(check){
                notificationService.createNotification("BUDGET FAILED!", NotificationType.BUDGET_WARNING, NotificationLevel.ERROR);
            }
        }


        if(target instanceof Goal){

            if(check){
                notificationService.createNotification("GOAL OVERDUE", NotificationType.GOAL_OVERDUE, NotificationLevel.ERROR);
            }
        }
        return strategy.isOverdue(target);
    }

}
