package com.bank.bank.infrastructure.controller;

import com.bank.bank.domain.model.Account;
import com.bank.bank.domain.service.AccountService;
import com.bank.bank.infrastructure.dto.request.CreateAccountRequest;
import com.bank.bank.infrastructure.dto.request.UpdateAccountRequest;
import com.bank.bank.infrastructure.dto.response.AccountResponse;
import com.bank.bank.infrastructure.dto.response.MessageResponse;
import com.bank.bank.infrastructure.dto.response.WrapperResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account Controller", description = "Controller to manage the accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @Operation(summary = "Create Account", description = "This method creates a new account")
    public ResponseEntity<WrapperResponse<AccountResponse>> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(new WrapperResponse<>(accountService.createAccount(request), null));
    }

    @PatchMapping
    @Operation(summary = "Update Account", description = "This method updates an account")
    public ResponseEntity<WrapperResponse<AccountResponse>> updateAccount(@Valid @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(new WrapperResponse<>(accountService.updateAccount(request), null));
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Get Account", description = "This method returns an account by id")
    public ResponseEntity<WrapperResponse<AccountResponse>> getAccount(@PathVariable Long accountNumber) {
        return ResponseEntity.ok(new WrapperResponse<>(accountService.getAccount(accountNumber), null));
    }

    @GetMapping
    @Operation(summary = "Get All Accounts", description = "This method returns all the accounts")
    public ResponseEntity<WrapperResponse<List<AccountResponse>>> getAllAccounts() {
        return ResponseEntity.ok(new WrapperResponse<>(accountService.getAllAccounts(), null));
    }

    @DeleteMapping("/{accountNumber}")
    @Operation(summary = "Delete Account", description = "This method deletes an account")
    public ResponseEntity<WrapperResponse<MessageResponse>> deleteAccount(@PathVariable Long accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.ok(new WrapperResponse<>(new MessageResponse("Account deleted successfully"), null));
    }


}
