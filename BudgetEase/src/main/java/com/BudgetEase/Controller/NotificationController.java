// NotificationController.java
package com.BudgetEase.Controller;

import com.BudgetEase.Models.Notification;
import com.BudgetEase.Models.NotificationLevel;
import com.BudgetEase.Models.NotificationType;
import com.BudgetEase.utils.GetCurrentUser;
import com.BudgetEase.BudgetEaseService.NotificationService;
import com.BudgetEase.BudgetEaseService.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
// @CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService=userService;
    }

    @PostMapping("/create")
    public Notification create(@RequestParam String message, @RequestParam NotificationType type, @RequestParam NotificationLevel level) {
        return notificationService.createNotification(message, type, level);
    }

    @PostMapping("/mark-read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        HashMap<String,Boolean> map = new HashMap<>();
        map.put("markedAsRead", true);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/getNotifications")
    public ResponseEntity<?> getNotificationsOfUser(){

        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        HashMap<String, List<Notification>> map = new HashMap<>();
        map.put("notifications", userService.getNotificationsByUserId(getCurrentUser.obtainUser().getUserId()));

        System.out.println(map);

        return ResponseEntity.ok(map);
    }
}
