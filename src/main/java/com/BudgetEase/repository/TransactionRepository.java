package com.BudgetEase.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.BudgetEase.Models.Category;
import com.BudgetEase.Models.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction,String>{
    @Query( " { 'category':?0, 'isExpense':true } " )
    public List<Transaction> findByCategory(Category category);

    @Query( " { 'user.id':?0 } " )
    public List<Transaction> findByUserId(String userId);

    @Query( " { 'date':{ 'gte':?0, 'lte':?1 } } " )
    public List<Transaction> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
