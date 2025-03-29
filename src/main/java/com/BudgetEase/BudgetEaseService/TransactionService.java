package com.BudgetEase.BudgetEaseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BudgetEase.Exceptions.TransactionNotFoundException;
import com.BudgetEase.Models.Transaction;
import com.BudgetEase.repository.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository=transactionRepository;
    }

    public void createTransaction( Transaction transaction ){
        transactionRepository.save(transaction);
    }

    public List<Transaction> getAllIncome(){
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream().filter(Transaction::isIncome).toList();
    }

    public List<Transaction> getAllExpense(){
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream().filter(Transaction::isExpense).toList();
    }

    public void updateTransaction( Transaction updatedTransaction ){
        Optional<Transaction> checkTransaction = transactionRepository.findById(updatedTransaction.getTransactionId());

        if(!checkTransaction.isPresent()){
            throw new TransactionNotFoundException("Transaction not found");
        }

        transactionRepository.save(updatedTransaction);
    }

    public Transaction deleteTransaction( String transactionId ){
        Optional<Transaction> checkTransaction = transactionRepository.findById(transactionId);

        if(!checkTransaction.isPresent()){
            throw new TransactionNotFoundException("Transaction not found");
        }

        transactionRepository.deleteById(transactionId);

        return checkTransaction.get();
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByDateBetween(start, end);
    }

    public List<Transaction> getTransactionsByUser(String userId) {
        return transactionRepository.findByUserId(userId);
    }
    
}
