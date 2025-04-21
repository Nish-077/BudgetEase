package com.BudgetEase.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    public List<Transaction> findByBudgetId(String budgetId);
    public List<Transaction> findByGoalId(String goalId);
    public List<Transaction> findByUserId(String userId); 
    public Transaction findByTransactionId(String transactionId);
    // public List<Transaction> findTransactionsByBudgetId(String budgetId);
}
