package com.BudgetEase.BudgetEaseService;

// import java.time.LocalDateTime;
// import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.BudgetEase.Models.Category;
import com.BudgetEase.BudgetEaseService.dtos.BudgetUpdate;
import com.BudgetEase.Exceptions.BudgetNotFoundException;
import com.BudgetEase.Models.Budget;
import com.BudgetEase.repository.BudgetRepository;
import com.BudgetEase.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BudgetService {
    private BudgetRepository budgetRepository;
    private TransactionRepository transactionRepository;

    public void createBudget( Budget budget ){

        budgetRepository.save(budget);
    }

    public void addBudgetToCategory( Category category, String budgetId ){

        Optional<Budget> checkBudget = budgetRepository.findById(budgetId);

        if(!checkBudget.isPresent()){
            throw new BudgetNotFoundException("Budget with id not found!");
        }

        Budget budget = checkBudget.get();

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
    
    public Budget updateBudget( String budgetId, final BudgetUpdate budgetUpdate ){
        return budgetRepository.findById(budgetId).map(
            existingBudget -> {
                final Budget updatedBudget = Budget.builder()
                .budgetId(budgetId)
                .budgetName(budgetUpdate.getBudgetName())
                .allocatedAmount(budgetUpdate.getAllocatedAmount())
                .startDate(budgetUpdate.getStartDate())
                .endDate(budgetUpdate.getEndDate())
                .category(budgetUpdate.getCategory())
                .build();

                return budgetRepository.save(updatedBudget);
            }
        ).orElseThrow( () -> new BudgetNotFoundException("Budget with given id not found") );
        
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
