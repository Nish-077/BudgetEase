package com.BudgetEase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.Goal;
import com.BudgetEase.Models.TargetStatus;

public interface GoalRepository extends MongoRepository<Goal,String> { 
    public Goal findByName(String name);
    long countByUserIdAndStatus(String userId, TargetStatus status);
}
