package com.BudgetEase.Models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.BudgetEase.repository.TransactionRepository;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "budgets")
public class Budget {

    @Id
    private String budgetId;

    // @NotBlank only applicable for String types
    @NotBlank(message = "Input should not be blank")
    private String budgetName;

    @NotNull(message = "Input should not be blank!")
    @Positive(message = "Allocated amount should be greater than zero")
    private double allocatedAmount;

    @NotNull(message = "Input should not be blank!")
    @Future(message = "Date should be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "Input should not be blank!")
    @Future(message = "Date should be in the future")
    private LocalDateTime endDate;

    @DBRef
    @NotNull(message = "User should not be null")
    private User user;

    @DBRef
    private Category category;

    public boolean isWithinBudget(double currentSpending) {
        return currentSpending <= this.allocatedAmount;
    }

    public double remainingAmount(TransactionRepository transactionRepository) {
        // Get all expense transactions related to this budget
        double currentSpending = transactionRepository.findByCategory(this.category).stream()
        .filter(Transaction::isExpense) // Only include expenses
        .mapToDouble(Transaction::getAmount)
        .sum();

        return this.allocatedAmount - currentSpending;
    }


    public boolean isActiveOn(LocalDateTime date) {
        return ((date.isAfter(this.startDate) || date.isEqual(this.startDate)) &&
                (date.isBefore(this.endDate) || date.isEqual(this.endDate)));
    }

    // Add @Valid annotation in controller
    @AssertTrue(message = "Start date must be before end date")
    public boolean isStartBeforeEndDate(){
        if( this.startDate == null || this.endDate == null ){
            return true;
        }

        return this.startDate.isBefore(endDate);
    }

}
