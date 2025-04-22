package com.BudgetEase.Notifications;

import com.BudgetEase.Models.Notification;

public interface NotificationListener {
    void onNotificationReceived(Notification notification);
}
