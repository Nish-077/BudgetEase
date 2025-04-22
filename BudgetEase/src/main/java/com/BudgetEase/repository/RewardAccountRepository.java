package com.BudgetEase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.RewardAccount;

public interface RewardAccountRepository extends MongoRepository<RewardAccount, String> {

}
