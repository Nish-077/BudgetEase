package com.BudgetEase.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.BudgetEase.Models.Category;
import com.BudgetEase.Models.Transaction;

public interface TransactionRepository extends MongoRepository{
    @Query( " { 'category':?0, 'isExpense':true } " )
    public List<Transaction> findByCategory(Category category);
}
