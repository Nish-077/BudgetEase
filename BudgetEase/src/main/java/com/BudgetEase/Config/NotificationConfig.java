// NotificationConfig.java
package com.BudgetEase.Config;

import com.BudgetEase.Notifications.NotificationLoggerListener;
import com.BudgetEase.BudgetEaseService.NotificationService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    public NotificationConfig(NotificationService service, NotificationLoggerListener logger) {
        service.addListener(logger);
    }
}