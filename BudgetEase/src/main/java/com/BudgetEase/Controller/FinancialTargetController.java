package com.BudgetEase.Controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BudgetEase.BudgetEaseService.BudgetService;
import com.BudgetEase.BudgetEaseService.FinancialTargetService;
import com.BudgetEase.BudgetEaseService.GoalService;
import com.BudgetEase.Models.FinancialTarget;
import com.BudgetEase.utils.ApiResponse;

@RestController
@RequestMapping("/api/financial-target")
public class FinancialTargetController {

    private final FinancialTargetService financialTargetService;
    private final BudgetService budgetService;
    private final GoalService goalService;

    public FinancialTargetController(FinancialTargetService financialTargetService, BudgetService budgetService, GoalService goalService) {
        this.financialTargetService = financialTargetService;
        this.budgetService = budgetService;
        this.goalService = goalService;
    }

    // Endpoint to calculate progress for a Budget or Goal
    @GetMapping("/progress/{type}/{id}")
    public ResponseEntity<?> calculateProgress(@PathVariable String type, @PathVariable String id) {
        FinancialTarget target = getFinancialTarget(type, id);
        if (target == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Financial target not found"));
        }

        double progress = financialTargetService.calculateProgress(target);
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
        return ResponseEntity.ok(new ApiResponse("Is overdue: " + isOverdue));
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