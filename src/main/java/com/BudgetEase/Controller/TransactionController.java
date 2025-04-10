package com.BudgetEase.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.BudgetEase.BudgetEaseService.GeminiVisionService;
import com.BudgetEase.BudgetEaseService.TransactionService;
import com.BudgetEase.Models.Transaction;

@RestController
// @RequestMapping("/api/transactions")
public class TransactionController {

    private TransactionService transactionService;
    private GeminiVisionService geminiVisionService;

    public TransactionController(TransactionService transactionService, GeminiVisionService geminiVisionService){
        this.transactionService=transactionService;
        this.geminiVisionService=geminiVisionService;
    }

    @PostMapping("/create-transaction-gemini")
    public ResponseEntity<List<Transaction>> createFromImage(@RequestParam("file") MultipartFile file) {
        List<Transaction> transactions = geminiVisionService.extractTransactionsFromImage(file);
        transactionService.createTransactionsFromList(transactions);
        return ResponseEntity.ok(transactions);
    }

}
