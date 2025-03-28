package com.BudgetEase.BudgetEaseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.BudgetEase.Models.Category;
import com.BudgetEase.Exceptions.BudgetNotFoundException;
import com.BudgetEase.Models.Budget;
import com.BudgetEase.repository.BudgetRepository;
import com.BudgetEase.repository.TransactionRepository;

public class BudgetService {
    private BudgetRepository budgetRepository;
    private TransactionRepository transactionRepository;

    public void createBudget( Budget budget ){

        budgetRepository.save(budget);
    }

    public void addBudgetToCategory( Category category, Budget budget ){
        budget.setCategory(category);
    }

    public Budget deleteBudget( String budgetId ){
        Optional deletedBudget = budgetRepository.findById(budgetId);

        if(!deletedBudget.isPresent()){
            throw new BudgetNotFoundException("Budget of this id is invalid");
        }

        budgetRepository.deleteById(budgetId);

        return (Budget) deletedBudget.get();

    }
    
    public void updateBudget( Budget budget ){

        Optional<Budget> checkBudget = budgetRepository.findById(budget.getBudgetId());
        if(!checkBudget.isPresent()){
            throw new BudgetNotFoundException("Budget with id not found!");
        }

        budgetRepository.save(budget);
    }

    public Double checkRemainingAmount( String budgetId ){
        Optional<Budget> checkBudget = budgetRepository.findById(budgetId);

        if(!checkBudget.isPresent()){
            throw new BudgetNotFoundException("Budget with given id not found");
        }

        Budget budget = checkBudget.get();

        return budget.remainingAmount(transactionRepository);
    }


}
