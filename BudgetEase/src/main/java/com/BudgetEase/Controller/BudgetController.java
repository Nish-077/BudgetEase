package com.BudgetEase.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BudgetEase.BudgetEaseService.BudgetService;
import com.BudgetEase.BudgetEaseService.TransactionService;
import com.BudgetEase.BudgetEaseService.UserService;
import com.BudgetEase.Models.Budget;
import com.BudgetEase.dtos.BudgetCreate;
import com.BudgetEase.utils.ApiResponse;
import com.BudgetEase.utils.GetCurrentUser;

import jakarta.validation.Valid;

import java.util.List;

@RequestMapping("/api/budget")
@RestController
public class BudgetController {

    private BudgetService budgetService;
    private UserService userService;
    private TransactionService transactionService;

    public BudgetController(BudgetService budgetService, UserService userService, TransactionService transactionService){
        this.budgetService=budgetService;
        this.userService=userService;
        this.transactionService=transactionService;
    }

    @PostMapping("/createBudget")
    public ResponseEntity<?> createBudget(@Valid @RequestBody BudgetCreate request){
        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        if(getCurrentUser.obtainUser() == null){
            return ResponseEntity.status(400).body(new ApiResponse("User not logged in"));
        }

        Budget budget = budgetService.createBudget(request);

        if(budget == null){
            return ResponseEntity.status(500).body(new ApiResponse("Error in creating budget"));
        }

        return ResponseEntity.ok(new ApiResponse("Budget created"));

    }

    @GetMapping("/getBudgets")
    public ResponseEntity<?> getBudgets(){

        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        return ResponseEntity.ok(userService.listBudgets(getCurrentUser.obtainUser().getUserId()));
    }

    @DeleteMapping("/deleteBudget/{budgetId}")
    public ResponseEntity<?> deleteBudget(@PathVariable String budgetId){
        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        userService.deleteBudget(budgetId, getCurrentUser.obtainUser().getUserId());

        Budget budget = budgetService.deleteBudgetById(budgetId);

        if(budget == null){
            return ResponseEntity.status(500).body(new ApiResponse("Budget deletion failed"));
        }

        return ResponseEntity.ok(new ApiResponse("Budget deleted"));

    }

    @GetMapping("/getBudgetById/{budgetId}")
    public ResponseEntity<?> getBudgetById(@PathVariable String budgetId) {
        try {
            Budget budget = budgetService.getBudgetById(budgetId);
            return ResponseEntity.ok(budget);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage()));
        }
    }

    @PutMapping("/updateBudget")
    public ResponseEntity<?> updateBudget(@Valid @RequestBody Budget request) {
        try {
            Budget updatedBudget = budgetService.updateBudget(request);
            return ResponseEntity.ok(new ApiResponse("Budget updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage()));
        }
    }

    @GetMapping("/getBudgetsByCategory/{category}")
    public ResponseEntity<?> getBudgetsByCategory(@PathVariable String category) {
        List<Budget> budgets = budgetService.getBudgetByCategory(category);
        if (budgets.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No budgets found for the given category"));
        }
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/findBudgetByName/{budgetName}")
    public ResponseEntity<?> findBudgetByName(@PathVariable String budgetName){

        return ResponseEntity.ok(budgetService.findBudgetByName(budgetName));
    }
}
