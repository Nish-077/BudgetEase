package com.BudgetEase.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.RewardAccount;

public interface RewardAccountRepository extends MongoRepository<RewardAccount, String> {
    public List<RewardAccount> findByUserId(String userId);

}
