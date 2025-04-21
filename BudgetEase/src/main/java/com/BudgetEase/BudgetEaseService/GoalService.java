package com.BudgetEase.BudgetEaseService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.BudgetEase.Models.Goal;
import com.BudgetEase.repository.GoalRepository;

@Service
public class GoalService {

    private GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository){
        this.goalRepository=goalRepository;
    }

    public List<Goal> getAllGoals(){
        return goalRepository.findAll();
    }

    public Goal createGoal(Goal goal){
        return goalRepository.save(goal);
    }

    public Goal deleteGoal(String goalId){
        Goal goal = goalRepository.findById(goalId).orElseThrow( () -> new IllegalArgumentException("Goal of this id does not exist") );

        goalRepository.deleteById(goalId);

        return goal;
    }

    public Goal updateGoal(Goal goal){
        Goal goalExists = goalRepository.findById(goal.getGoalId()).orElseThrow( () -> new IllegalArgumentException("Goal with this id ") );

        goalExists.setGoalId(goal.getGoalId());
        goalExists.setTargetAmount(goal.getTargetAmount());
        goalExists.setName(goal.getName());
        goalExists.setPurpose(goal.getPurpose());
        goalExists.setStartDate(goal.getStartDate());
        goalExists.setDescription(goal.getDescription());
        goalExists.setCategoryName(goal.getCategoryName());
        goalExists.setEndDate(goal.getEndDate());

        return goalRepository.save(goalExists);
    }

    public boolean checkStartAndEndDate(Goal goal){
        return goal.startDateBeforeEndDate();
    }

    public Goal getGoalById(String goalId){
        Goal goal = goalRepository.findById(goalId).orElseThrow( () -> new IllegalArgumentException("No goal with this id") );

        return goal;
    }

    public Goal getGoalByName(String goalName){
        return goalRepository.findByName(goalName);
    }
}
