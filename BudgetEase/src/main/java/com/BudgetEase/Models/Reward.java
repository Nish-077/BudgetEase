package com.BudgetEase.Models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Reward {

    private String iconUrl;
    private LocalDateTime earnedAt;

    public abstract String getRewardType();

    public long getTimeSinceEarnedInDays(LocalDateTime earnedAt) {
        return Duration.between(earnedAt, LocalDateTime.now()).toDays();
    }
}
