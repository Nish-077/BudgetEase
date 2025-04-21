package com.BudgetEase.repository;

import com.BudgetEase.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUserName(String userName);
    public User findByUserId(String id);
    public User findByEmailOrUserName(String email, String username);
}
