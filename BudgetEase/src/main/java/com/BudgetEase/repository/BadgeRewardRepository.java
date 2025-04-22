package com.BudgetEase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.BadgeReward;

public interface BadgeRewardRepository extends MongoRepository<BadgeReward, String> {

}
