package com.BudgetEase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.Category;

public interface CategoryRepository extends MongoRepository<Category,String> {

}
