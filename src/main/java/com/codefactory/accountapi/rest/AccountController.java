package com.codefactory.accountapi.rest;

import com.codefactory.accountapi.model.AccountTypeEnum;
import com.codefactory.accountapi.service.AccountService;
import com.codefactory.accountapi.service.dto.AccountDTO;
import com.codefactory.accountapi.service.dto.BalanceDTO;
import com.codefactory.accountapi.service.dto.TransactionDTO;
import com.codefactory.accountapi.service.exception.AccountNotFoundException;
import com.codefactory.accountapi.service.exception.InvalidAccountTypeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.iban4j.CountryCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "open a new account")
    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> createAccount(@Parameter(description = "Type of account:[CHECKING, SAVING, PRIVATE LOAN") @RequestParam AccountTypeEnum accountType,
                                                    @Parameter(description = "Country Code to generate IBAN", example = "DE") @RequestParam(defaultValue = "DE") CountryCode countryCode,
                                                    @Parameter(description = "For Saving Account you can assign a reference account") @RequestParam(required = false) String referenceAccountIban) {
        try {
            AccountDTO result = accountService.createAccount(accountType, countryCode, referenceAccountIban);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (InvalidAccountTypeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "assign an existing Checking Account as a reference to an existing Saving Account")
    @PutMapping("/accounts/reference")
    public ResponseEntity<AccountDTO> assignReferenceAccount(@RequestParam String savingAccountIban, @RequestParam String checkingAccountIban) {
        try {
            AccountDTO result = accountService.assignReferenceAccount(savingAccountIban, checkingAccountIban);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidAccountTypeException | AccountNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "block an account")
    @PutMapping("/block")
    public ResponseEntity<AccountDTO> blockAccount(@RequestParam String iban) {
        try {
            AccountDTO result = accountService.blockAccount(iban);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "unblock an account")
    @PutMapping("/unblock")
    public ResponseEntity<AccountDTO> unblockAccount(@RequestParam String iban) {
        try {
            var result = accountService.unblockAccount(iban);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "get the account balance")
    @GetMapping("/balance")
    public ResponseEntity<BalanceDTO> getAccountListByType(@RequestParam String iban) {
        try {
            var result = accountService.getBalance(iban);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "get accounts by account type")
    @GetMapping("/filtered-account-list")
    public ResponseEntity<List<AccountDTO>> getAccountListByType(@RequestParam AccountTypeEnum accountType) {
        var result = accountService.getAccountListByType(accountType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "get the transaction history of an account")
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionHistory(@RequestParam String iban) throws AccountNotFoundException {
        var result = accountService.getTransactionHistory(iban);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
