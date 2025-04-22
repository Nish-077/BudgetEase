package com.BudgetEase.Notifications;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.BudgetEase.Models.Notification;

@Component
public class WebSocketNotificationListener implements NotificationListener {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketNotificationListener(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate=messagingTemplate;
    }

    @Override
    public void onNotificationReceived(Notification notification){
        // Send to a WebSocket topic (all subscribed clients will receive it in real-time!)
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

}
