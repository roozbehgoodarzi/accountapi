package com.codefactory.accountapi.rest;

import com.codefactory.accountapi.model.AccountTypeEnum;
import com.codefactory.accountapi.service.AccountService;
import com.codefactory.accountapi.service.dto.AccountDTO;
import com.codefactory.accountapi.service.dto.BalanceDTO;
import com.codefactory.accountapi.service.dto.TransactionDTO;
import com.codefactory.accountapi.service.exception.AccountNotFoundException;
import com.codefactory.accountapi.service.exception.InvalidAccountTypeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> createAccount(@RequestParam AccountTypeEnum accountType,
                                                    @RequestParam CountryCode countryCode,
                                                    @RequestParam(required = false) String referenceAccountIban) {
        try {
            AccountDTO result = accountService.createAccount(accountType, countryCode, referenceAccountIban);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (InvalidAccountTypeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/block")
    public ResponseEntity<AccountDTO> blockAccount(@RequestParam String iban) {
        try {
            AccountDTO result = accountService.blockAccount(iban);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/unblock")
    public ResponseEntity<AccountDTO> unblockAccount(@RequestParam String iban) {
        try {
            var result = accountService.unblockAccount(iban);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceDTO> getAccountListByType(@RequestParam String iban) {
        try {
            var result = accountService.getBalance(iban);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/filtered-account-list")
    public ResponseEntity<List<AccountDTO>> getAccountListByType(@RequestParam AccountTypeEnum accountType) {
        var result = accountService.getAccountListByType(accountType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionHistory(@RequestParam String iban) throws AccountNotFoundException {
        var result = accountService.getTransactionHistory(iban);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
