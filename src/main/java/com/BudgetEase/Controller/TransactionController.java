package com.BudgetEase.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.BudgetEase.BudgetEaseService.BudgetService;
import com.BudgetEase.BudgetEaseService.GeminiVisionService;
import com.BudgetEase.BudgetEaseService.TransactionService;
import com.BudgetEase.BudgetEaseService.UserService;
import com.BudgetEase.Models.Transaction;
import com.BudgetEase.Models.User;
import com.BudgetEase.dtos.TransactionUpdate;
import com.BudgetEase.utils.ApiResponse;
import com.BudgetEase.utils.GetCurrentUser;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    // private final repository.UserRepository userRepository;

    private final UserService userService;
    private TransactionService transactionService;
    private GeminiVisionService geminiVisionService;
    // private BudgetService budgetService;

    public TransactionController(TransactionService transactionService, GeminiVisionService geminiVisionService, UserService userService, BudgetService budgetService){
        this.transactionService=transactionService;
        this.geminiVisionService=geminiVisionService;
        this.userService = userService;
        // this.budgetService=budgetService;
    }

    @PostMapping("/create-transaction-gemini")
    public ResponseEntity<List<Transaction>> createFromImage(@RequestParam("file") MultipartFile file) {
        List<Transaction> transactions = geminiVisionService.extractTransactionsFromImage(file);
        transactionService.createTransactionsFromList(transactions);
        return ResponseEntity.ok(transactions);
    }

   @PostMapping("/create-normal-transaction")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody Transaction request) {
        // Get the current user
        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);
        User currentUser = getCurrentUser.obtainUser();

        if (currentUser == null) {
            return ResponseEntity.status(401).body(new ApiResponse("User not authenticated!"));
        }

        // Set the user ID in the transaction
        request.setUserId(currentUser.getUserId());

        // Save the transaction
        transactionService.createTransaction(request);

        // Return a success response
        return ResponseEntity.ok(new ApiResponse("Transaction created successfully!"));
    }

    @PostMapping("/addTransactionToBudget/{budgetId}")
    public ResponseEntity<?> addTransactionToBudget(@Valid @RequestBody Transaction request, @PathVariable String budgetId){
       request.setBudgetId(budgetId);

       Transaction transaction = transactionService.addTransactionToBudget(request);

       if(transaction == null){
        return ResponseEntity.status(500).body(new ApiResponse("Error in adding transaction to budget"));
       }

       return ResponseEntity.ok(new ApiResponse("Transaction added to budget"));
    }
    
    @PostMapping("/addTransactionToGoal/{goalId}")
    public ResponseEntity<?> addTransactionToGoal(@Valid @RequestBody Transaction request, @PathVariable String goalId){
       request.setBudgetId(goalId);

       Transaction transaction = transactionService.addTransactionToGoal(request);

       if(transaction == null){
        return ResponseEntity.status(500).body(new ApiResponse("Error in adding transaction to budget"));
       }

       return ResponseEntity.ok(new ApiResponse("Transaction added to goal"));
    }
    
    @PostMapping("/updateTransaction/{transactionId}")
    public ResponseEntity<?> updateTransaction(@Valid @RequestBody TransactionUpdate request, @PathVariable String transactionId){
        Transaction transaction = transactionService.findTransactionById(transactionId);

        if(transaction == null){
            return ResponseEntity.status(404).body(new ApiResponse("Transaction with this id does not exist"));
        }

        Transaction updatedTransaction = transactionService.updateTransaction(request, transactionId);

        if(updatedTransaction == null){
            return ResponseEntity.status(500).body(new ApiResponse("Transaction cannot be updated"));
        }

        return ResponseEntity.ok(new ApiResponse("Transaction updated"));
    }

    @GetMapping("/getTransactions")
    public ResponseEntity<?> getTransactions(){

        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        String userId = getCurrentUser.obtainUser().getUserId();

        return ResponseEntity.ok(transactionService.getTransactions(userId));
    }

    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<?> deleteTransaction(@PathVariable String transactionId){
        Transaction deletedTransaction = transactionService.deleteTransaction(transactionId);

        if(deletedTransaction == null){
            return ResponseEntity.status(500).body(new ApiResponse("transaction deletion failed"));
        }

        return ResponseEntity.ok().body(new ApiResponse("Deleted transaction "+deletedTransaction));

    }
}
