package com.bank.bank.infrastructure.controller;

import com.bank.bank.domain.model.Transaction;
import com.bank.bank.domain.service.TransactionService;
import com.bank.bank.infrastructure.dto.request.CreateTransactionRequest;
import com.bank.bank.infrastructure.dto.response.MessageResponse;
import com.bank.bank.infrastructure.dto.response.TransactionResponse;
import com.bank.bank.infrastructure.dto.response.WrapperResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transaction Management", description = "Operations pertaining to transaction in Transaction Management System")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Create a new transaction", tags = { "Transaction Management" })
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody CreateTransactionRequest request) {
        TransactionResponse transaction = transactionService.createTransaction(request);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing transaction", description = "Update an existing transaction", tags = { "Transaction Management" })
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody CreateTransactionRequest request) {
        TransactionResponse transaction = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a transaction by id", description = "Get a transaction by id", tags = { "Transaction Management" })
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long id) {
        TransactionResponse transaction = transactionService.getTransaction(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Get all transactions", tags = { "Transaction Management" })
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction", description = "Delete a transaction", tags = { "Transaction Management" })
    public ResponseEntity<WrapperResponse<MessageResponse>> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(new WrapperResponse<>(new MessageResponse("Transaction deleted successfully"), null));
    }


}
