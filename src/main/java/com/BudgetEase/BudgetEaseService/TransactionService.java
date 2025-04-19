package com.BudgetEase.BudgetEaseService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.Goal;
import com.BudgetEase.Models.Transaction;
import com.BudgetEase.Models.TransactionType;
import com.BudgetEase.dtos.TransactionUpdate;
import com.BudgetEase.repository.BudgetRepository;
import com.BudgetEase.repository.GoalRepository;
import com.BudgetEase.repository.TransactionRepository;

@Service
public class TransactionService {

    TransactionRepository transactionRepository;
    BudgetRepository budgetRepository;
    GoalRepository goalRepository;

    public TransactionService(TransactionRepository transactionRepository,
                          BudgetRepository budgetRepository,
                          GoalRepository goalRepository) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.goalRepository = goalRepository;
    }

    public Transaction addTransactionToBudget(Transaction transaction) throws IllegalArgumentException{
        String budgetId = transaction.getBudgetId();

        // Check if transaction type is EXPENSE
        if (!transaction.getType().equals(TransactionType.EXPENSE)) {
            throw new IllegalArgumentException("Only EXPENSE transactions can be associated with a budget.");
        }

        // Fetch and validate budget
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("Budget with ID " + budgetId + " not found"));

        if (!budget.isActiveOn(transaction.getDate())) {
            throw new IllegalArgumentException("Budget is not active");
        }

        // All good — associate and save
        return transactionRepository.save(transaction);
    }

    public Transaction addTransactionToGoal(Transaction transaction) throws IllegalArgumentException{
        String goalId = transaction.getGoalId();

        if(!transaction.getType().equals(TransactionType.INCOME)){
            throw new IllegalArgumentException("Only INCOMES can be associated with budget");
        }

        Goal goal = goalRepository.findById(goalId).orElseThrow( () -> new IllegalArgumentException("Goal ID does not exist") );

        if(!goal.isActiveOn(transaction.getDate())){
            throw new IllegalArgumentException("Goal is inactive");
        }

        return transactionRepository.save(transaction);
    }

    public List<Transaction> fetchTransactionByBudgetId(String budgetId){
        List<Transaction> transactions = transactionRepository.findByBudgetId(budgetId);

        if (transactions.isEmpty()) {
            throw new NoTransactionException("No transactions from "+budgetId);
        }
    
        return transactions;
    }

    public List<Transaction> fetchTransactionByGoalId(String goalId){
        List<Transaction> transactions = transactionRepository.findByGoalId(goalId);

        if(transactions.isEmpty()){
            throw new NoTransactionException("No transactions from "+goalId);
        }

        return transactions;
    }

    public double getCurrentSpending(String budgetId){
        return transactionRepository.findByBudgetId(budgetId).stream()
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    public double getCurrentGain(String goalId){
        return transactionRepository.findByGoalId(goalId).stream()
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    public Transaction createTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Transaction deleteTransaction(String transactionId){

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow( () -> new IllegalArgumentException("transactionId doesn't exist") );

        transactionRepository.deleteById(transactionId);

        return transaction;
    }

    public List<Transaction> getTransactions(String userId){
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        return transactions;
    }

    public List<Transaction> fetchTransactionByUserId(String userId) {
        return transactionRepository.findByUserId(userId);
    }
    
    public List<Transaction> createTransactionsFromList(List<Transaction> transactions){

        transactions.forEach( (transaction) -> transactionRepository.save(transaction) );

        return transactions;
    }

    public Transaction findTransactionById(String transactionId){
        return transactionRepository.findByTransactionId(transactionId);
    }

    public Transaction updateTransaction(TransactionUpdate updatedTransaction, String transactionId){

        Transaction existingTransaction = transactionRepository.findById(transactionId).orElseThrow( () -> new IllegalArgumentException("Transaction not found") );

        Transaction finalTransaction = Transaction.builder()
            .transactionId(transactionId)
            .amount(updatedTransaction.getAmount())
            .date(updatedTransaction.getDate())
            .description(updatedTransaction.getDescription())
            .merchant(updatedTransaction.getMerchant())
            .type(updatedTransaction.getType())
            .status(updatedTransaction.getPaymentStatus())
            .build();
        
        return finalTransaction;
    }

}
