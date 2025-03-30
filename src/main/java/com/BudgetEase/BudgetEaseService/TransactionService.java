package com.BudgetEase.BudgetEaseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BudgetEase.BudgetEaseService.dtos.TransactionUpdate;
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

    public Transaction updateTransaction( String transactionId, final TransactionUpdate transactionUpdate ){
        return transactionRepository.findById(transactionId).map( 
            existingTransaction -> {
                final Transaction updatedTransaction = Transaction.builder()
                .transactionId(transactionId)
                .amount(transactionUpdate.getAmount())
                .date(transactionUpdate.getDate())
                .type(transactionUpdate.getType())
                .description(transactionUpdate.getDescription())
                .category(transactionUpdate.getCategory())
                .build();

                return transactionRepository.save(updatedTransaction);
            }
         ).orElseThrow( () -> new TransactionNotFoundException("Transaction with this id not found") );
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
