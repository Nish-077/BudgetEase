package com.BudgetEase.Exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message){
        super(message);
    }
}
