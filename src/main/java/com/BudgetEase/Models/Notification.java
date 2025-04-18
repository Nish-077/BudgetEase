package com.BudgetEase.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @Id
    private String notificationId;
    private String message;
    private boolean isRead;
    private NotificationType type;
    private NotificationLevel level;
    private LocalDateTime createdAt;

    public void markAsRead() {
        this.setRead(true);
    }

    public void markAsUnread() {
        this.setRead(false);
    }

    public int ageInDays() {
        LocalDateTime date = LocalDateTime.now();
        return (int) ChronoUnit.DAYS.between(this.createdAt, date);
    }

}
