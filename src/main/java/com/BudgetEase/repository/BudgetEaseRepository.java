package com.BudgetEase.repository;

import com.BudgetEase.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BudgetEaseRepository extends MongoRepository {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUserName(String userName);
}
