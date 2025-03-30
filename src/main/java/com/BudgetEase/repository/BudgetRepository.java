package com.BudgetEase.repository;

import java.time.LocalDateTime;
import java.util.List;
// import java.util.Locale.Category;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.Category;

public interface BudgetRepository extends MongoRepository<Budget,String> {
    @Query( " { 'user.id':$0, 'startDate':{ $lt:$2 },  'endDate': { $gt:$1 } } " )
    public List<Budget> overLappingBudget( String userId, LocalDateTime startDate, LocalDateTime endDate );

    @Query( " { 'category':?0 } " )
    public Budget findBudgetByCategory( Category category );
}
