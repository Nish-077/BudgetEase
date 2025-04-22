package com.BudgetEase.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BudgetEase.Models.Notification;

public interface NotificationRepository extends MongoRepository<Notification,String> {

}
