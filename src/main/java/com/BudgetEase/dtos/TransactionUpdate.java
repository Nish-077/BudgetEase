package com.BudgetEase.dtos;

import java.time.LocalDateTime;

import com.BudgetEase.Models.PaymentStatus;
import com.BudgetEase.Models.TransactionType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionUpdate {

    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private double amount;

    // @NotNull(message = "Date cannot be null")
    private LocalDateTime date;

    // @NotNull(message = "Transaction type cannot be null")
    private TransactionType type;

    // @NotNull(message = "Payment status cannot be null")
    private PaymentStatus paymentStatus;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    // @NotBlank(message = "Merchant name cannot be blank")
    @Size(max = 100, message = "Merchant name cannot exceed 100 characters")
    private String merchant;
}
