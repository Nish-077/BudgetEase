package com.BudgetEase.Notifications;

import java.util.ArrayList;
import java.util.List;

import com.BudgetEase.Models.Notification;

public class NotificationPublisher {
     private final List<NotificationListener> listeners = new ArrayList<>();

    public void addListener(NotificationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NotificationListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(Notification notification) {
        for (NotificationListener listener : listeners) {
            listener.onNotificationReceived(notification);
        }
    }
}
