package com.BudgetEase.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidUserCredentialsException.class)
    public ResponseEntity<String> handleUserDoesNotExistsException(InvalidUserCredentialsException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<String> handleBudgetNotFoundException(BudgetNotFoundException exc){
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException exc){
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoTransactionsFoundException.class)
    public ResponseEntity<String> handleNoTransactions(NoTransactionsFoundException exc){
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.CONFLICT);
    }
}
