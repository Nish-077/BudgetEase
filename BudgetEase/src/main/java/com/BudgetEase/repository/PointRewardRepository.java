package com.BudgetEase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.PointBasedReward;

public interface PointRewardRepository extends MongoRepository<PointBasedReward, String> {
    
}
