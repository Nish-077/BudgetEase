package com.BudgetEase.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.checkerframework.checker.units.qual.m;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BudgetEase.BudgetEaseService.BudgetService;
import com.BudgetEase.BudgetEaseService.FinancialTargetService;
import com.BudgetEase.BudgetEaseService.GoalService;
import com.BudgetEase.BudgetEaseService.UserService;
import com.BudgetEase.Models.FinancialTarget;
import com.BudgetEase.utils.ApiResponse;
import com.BudgetEase.utils.GetCurrentUser;

@RestController
@RequestMapping("/api/financial-target")
public class FinancialTargetController {

    private final FinancialTargetService financialTargetService;
    private final BudgetService budgetService;
    private final GoalService goalService;
    private final UserService userService;

    public FinancialTargetController(FinancialTargetService financialTargetService, BudgetService budgetService, GoalService goalService, UserService userService) {
        this.financialTargetService = financialTargetService;
        this.budgetService = budgetService;
        this.goalService = goalService;
        this.userService=userService;
    }

    // Endpoint to calculate progress for a Budget or Goal
    @GetMapping("/progress/{type}/{id}")
    public ResponseEntity<?> calculateProgress(@PathVariable String type, @PathVariable String id) {

        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        FinancialTarget target = getFinancialTarget(type, id);

        if (target == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Financial target not found"));
        }

        // Boolean early = false;

        // if(LocalDateTime.now().isBefore(target.getEndDate())){
        //     early=true;
        // }

        double progress = financialTargetService.calculateProgress(target,getCurrentUser.obtainUser().getUserId());
        HashMap<String, Double> map = new HashMap<>();
        map.put("progress", progress);
        return ResponseEntity.ok(map);
    }

    // Endpoint to check if a Budget or Goal is overdue
    @GetMapping("/overdue/{type}/{id}")
    public ResponseEntity<?> checkIfOverdue(@PathVariable String type, @PathVariable String id) {
        FinancialTarget target = getFinancialTarget(type, id);
        if (target == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Financial target not found"));
        }

        boolean isOverdue = financialTargetService.checkIfOverdue(target);
        HashMap<String,Boolean> map = new HashMap<>();
        map.put("isOverdue", isOverdue);
        return ResponseEntity.ok(map);
    }

    // Helper method to fetch the FinancialTarget (Budget or Goal) based on type and ID
    private FinancialTarget getFinancialTarget(String type, String id) {
        switch (type.toLowerCase()) {
            case "budget":
                return budgetService.getBudgetById(id);
            case "goal":
                return goalService.getGoalById(id);
            default:
                return null;
        }
    }
}