package com.BudgetEase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.Goal;

public interface GoalRepository extends MongoRepository<Goal,String> { 
}
