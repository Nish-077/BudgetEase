package com.BudgetEase.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.BudgetEase.Models.Budget;

public interface BudgetRepository extends MongoRepository {
    @Query( " { 'user._id':$0, 'startDate':{ $lt:$2 },  'endDate': { $gt:$1 } } " )
    public List<Budget> overLappingBudget( String userId, LocalDateTime startDate, LocalDateTime endDate );

    
}
