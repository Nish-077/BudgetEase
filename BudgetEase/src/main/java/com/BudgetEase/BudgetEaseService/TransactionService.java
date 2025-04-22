package com.BudgetEase.BudgetEaseService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;

import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.Goal;
import com.BudgetEase.Models.NotificationLevel;
import com.BudgetEase.Models.NotificationType;
import com.BudgetEase.Models.Transaction;
import com.BudgetEase.Models.TransactionType;
import com.BudgetEase.Models.User;
import com.BudgetEase.dtos.TransactionUpdate;
import com.BudgetEase.repository.BudgetRepository;
import com.BudgetEase.repository.GoalRepository;
import com.BudgetEase.repository.TransactionRepository;
import com.BudgetEase.utils.GetCurrentUser;

@Service
public class TransactionService {

    TransactionRepository transactionRepository;
    BudgetRepository budgetRepository;
    GoalRepository goalRepository;
    UserService userService;
    RewardService rewardService;
    NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository,
                          BudgetRepository budgetRepository,
                          GoalRepository goalRepository,
                          UserService userService,
                          RewardService rewardService,
                          NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.goalRepository = goalRepository;
        this.userService = userService;
        this.rewardService=rewardService;
        this.notificationService=notificationService;
    }

    public Transaction addTransactionToBudget(Transaction transaction, String budgetId) throws IllegalArgumentException{

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

    public Transaction addTransactionToGoal(Transaction transaction, String goalId) throws IllegalArgumentException{

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
        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        User user = getCurrentUser.obtainUser();

        long count = transactionRepository.countByUserId(user.getUserId());

        if(count==19){
            rewardService.rewardForTwentyTransactions(user.getUserId());
        }

        notificationService.createNotification("TRANSACTION CREATED", NotificationType.TRANSACTION_ADDED, NotificationLevel.SUCCESS);

        rewardService.rewardForAddingTransaction(user.getUserId());
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

        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        transactions.forEach( (transaction) -> {
            transaction.setUserId(getCurrentUser.obtainUser().getUserId());
            transactionRepository.save(transaction);
            notificationService.createNotification("NOTIF VIA IMAGE CREATED!", NotificationType.TRANSACTION_ADDED, NotificationLevel.SUCCESS);
        } );

        return transactions;
    }

    public Transaction findTransactionById(String transactionId){
        return transactionRepository.findByTransactionId(transactionId);
    }

    public Transaction updateTransaction(TransactionUpdate updatedTransaction, String transactionId){

        Transaction existingTransaction = transactionRepository.findById(transactionId).orElseThrow( () -> new IllegalArgumentException("Transaction not found") );

        existingTransaction.setTransactionId(transactionId);
        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setDate(updatedTransaction.getDate());
        existingTransaction.setDescription(updatedTransaction.getDescription());
        existingTransaction.setGoalId(updatedTransaction.getGoalId());
        existingTransaction.setBudgetId(updatedTransaction.getBudgetId());
        existingTransaction.setMerchant(updatedTransaction.getMerchant());
        existingTransaction.setStatus(updatedTransaction.getPaymentStatus());
        existingTransaction.setType(updatedTransaction.getType());
        
        return transactionRepository.save(existingTransaction);
    }

    public List<Transaction> getTransactionsByBudgetId(String budgetId){
        return transactionRepository.findByBudgetId(budgetId);
    }

    public List<Transaction> getTransactionsByGoalId(String goalId){
        return transactionRepository.findByGoalId(goalId);
    }
}
