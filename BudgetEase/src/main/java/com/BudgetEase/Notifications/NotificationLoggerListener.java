// NotificationLoggerListener.java
package com.BudgetEase.Notifications;

import com.BudgetEase.Models.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationLoggerListener implements NotificationListener {
    @Override
    public void onNotificationReceived(Notification notification) {
        System.out.println("🔔 New Notification: " + notification.getMessage());
    }
}