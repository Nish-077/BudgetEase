package com.BudgetEase.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.TargetStatus;

public interface BudgetRepository extends MongoRepository<Budget,String> {
    // public Budget findBudgetByUserId(String userId);
    public List<Budget> findBudgetByCategoryName(String categoryName); 
    public Budget findByName(String name);
    // long countByUserIdAndStatus(String userId, TargetStatus status);
}
