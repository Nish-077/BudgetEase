package com.BudgetEase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.CompositeReward;

public interface CompositeRewardRepository extends MongoRepository<CompositeReward, String> {
    
}
