package com.BudgetEase.BudgetEaseService;
import com.BudgetEase.Models.Notification;
import com.BudgetEase.Models.NotificationLevel;
import com.BudgetEase.Models.NotificationType;
import com.BudgetEase.Notifications.NotificationListener;
import com.BudgetEase.Notifications.NotificationLoggerListener;
import com.BudgetEase.Notifications.NotificationPublisher;
import com.BudgetEase.Notifications.WebSocketNotificationListener;
import com.BudgetEase.repository.*;
import com.BudgetEase.utils.GetCurrentUser;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPublisher publisher;
    private final UserService userService;

    public NotificationService(NotificationRepository notificationRepository, UserService userService, NotificationLoggerListener notificationLoggerListener, WebSocketNotificationListener webSocketNotificationListener) {
        this.notificationRepository = notificationRepository;
        this.userService=userService;
        this.publisher = new NotificationPublisher();

        this.publisher.addListener(notificationLoggerListener);
        this.publisher.addListener(webSocketNotificationListener);
    }

    public void addListener(NotificationListener listener) {
        publisher.addListener(listener);
    }

    public Notification createNotification(String message, NotificationType type, NotificationLevel level) {
        Notification notification = new Notification(
            null, message, false, type, level, LocalDateTime.now()
        );


        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);
        String userId = getCurrentUser.obtainUser().getUserId();

        notification = notificationRepository.save(notification);
        userService.addNotificationToUser(userId, notification);
        publisher.notifyListeners(notification);
        return notification;
    }

    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        notification.markAsRead();
        notificationRepository.save(notification);
    }
}