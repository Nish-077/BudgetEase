package com.BudgetEase.Models;

import java.time.LocalDateTime;
import java.time.Duration;

public interface Reward {
    String getRewardType();

    default long getTimeSinceEarnedInDays(LocalDateTime earnedAt) {
        return Duration.between(earnedAt, LocalDateTime.now()).toDays();
    }
}
