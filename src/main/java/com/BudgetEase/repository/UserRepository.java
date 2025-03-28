package com.BudgetEase.repository;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUserName(String userName);
}
