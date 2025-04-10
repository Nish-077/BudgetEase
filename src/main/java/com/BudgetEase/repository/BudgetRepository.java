package com.BudgetEase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.Budget;

public interface BudgetRepository extends MongoRepository<Budget,String> {

}
