package com.BudgetEase.BudgetEaseService;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;

import com.BudgetEase.Exceptions.CategoryNotFoundException;
import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.Category;
import com.BudgetEase.Models.Transaction;
import com.BudgetEase.repository.BudgetRepository;
import com.BudgetEase.repository.CategoryRepository;
import com.BudgetEase.repository.TransactionRepository;

public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, 
                           TransactionRepository transactionRepository, 
                           BudgetRepository budgetRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
    }

    public Category createCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category deleteCategory(String categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow( () -> new CategoryNotFoundException("Category not found!") );

        List<Transaction> transactions = transactionRepository.findByCategory(category);

        if(!transactions.isEmpty()){
            transactions.forEach( 
                transaction -> {
                    transaction.setCategory(null);
                    transactionRepository.save(transaction);
                }
            );
        }

        Budget budget = budgetRepository.findBudgetByCategory(category);

        if(budget != null){
            budget.setCategory(null);
            budgetRepository.save(budget);
        }

        categoryRepository.deleteById(categoryId);

        return category;
    }

    public Category updateCategory(String categoryName, String categoryId){
        return categoryRepository.findById(categoryId).map(
            exisingCategory -> {
                final Category updatedCategory = Category.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .build();

                return updatedCategory;
            }
        ).orElseThrow(() -> new CategoryNotFoundException("Category with this id not found!"));
    }

}
