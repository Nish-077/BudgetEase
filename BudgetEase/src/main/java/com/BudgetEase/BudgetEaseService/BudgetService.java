package com.BudgetEase.BudgetEaseService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.dtos.BudgetCreate;
import com.BudgetEase.repository.BudgetRepository;
import com.BudgetEase.utils.GetCurrentUser;

@Service
public class BudgetService {

    private BudgetRepository budgetRepository;
    private UserService userService;

    public BudgetService(BudgetRepository budgetRepository, UserService userService){
        this.budgetRepository=budgetRepository;
        this.userService=userService;
    }

    public Budget createBudget(BudgetCreate createBudget){

        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        Budget budget = new Budget();

        budget.setAllocatedAmount(createBudget.getAmount());
        budget.setCategoryName(createBudget.getCategoryName());
        budget.setStartDate(createBudget.getStartDate());
        budget.setEndDate(createBudget.getEndDate());
        budget.setName(createBudget.getName());

        budgetRepository.save(budget);

        return userService.addBudget(budget, getCurrentUser.obtainUser().getUserId());
    }

    public Budget getBudgetById(String budgetId){
        Budget budget = budgetRepository.findById(budgetId).orElseThrow( () -> new IllegalArgumentException("Budget with this id") );

        return budget;
    }

    public List<Budget> getBudgetByCategory(String category){
        List<Budget> budgets = budgetRepository.findBudgetByCategoryName(category);

        return budgets;
    }

    public Budget updateBudget(Budget budget){
        Budget budgetExists = budgetRepository.findById(budget.getBudgetId()).orElseThrow( () -> new IllegalArgumentException("Budget id doesn't ") );

        budgetExists.setBudgetId(budget.getBudgetId());
        budgetExists.setCategoryName(budget.getCategoryName());
        budgetExists.setAllocatedAmount(budget.getAllocatedAmount());
        budgetExists.setDescription(budget.getDescription());
        budgetExists.setName(budget.getName());
        budgetExists.setStartDate(budget.getStartDate());
        budgetExists.setEndDate(budget.getEndDate());

        return budgetRepository.save(budgetExists);
    }

    // public Budget findBudgetById(String budgetId){
    //     Budget budget = budgetRepository.findById(budgetId).orElseThrow( () -> new IllegalArgumentException("Invalid budget id") );

    //     return budget;
    // }

    public Budget deleteBudgetById(String budgetId){

        Budget budget = budgetRepository.findById(budgetId).orElseThrow( () -> new IllegalArgumentException("Budget with id not found") );

        budgetRepository.deleteById(budgetId);

        return budget;
    }

    public Budget findBudgetByName(String budgetName){
        return budgetRepository.findByName(budgetName);
    }
}
