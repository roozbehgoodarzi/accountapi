package com.codefactory.accountapi.rest;

import com.codefactory.accountapi.service.TransactionService;
import com.codefactory.accountapi.service.dto.TransactionDTO;
import com.codefactory.accountapi.service.exception.AccountLockException;
import com.codefactory.accountapi.service.exception.AccountNotFoundException;
import com.codefactory.accountapi.service.exception.TransferValidationException;
import com.codefactory.accountapi.service.exception.WithdrawValidationException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
        public ResponseEntity<TransactionDTO> transfer(@RequestParam String fromAccountIban, @RequestParam String toAccountIban, @RequestParam Long amount)  {
        try {
            var result = transactionService.transfer(fromAccountIban, toAccountIban, amount);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (TransferValidationException | AccountNotFoundException | AccountLockException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDTO> deposit(@RequestParam String iban, @RequestParam Long amount) throws AccountNotFoundException {
        var result = transactionService.deposit(iban, amount);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@RequestParam String iban, @RequestParam Long amount) throws TransferValidationException, AccountLockException, WithdrawValidationException, AccountNotFoundException {
        var result = transactionService.withdraw(iban, amount);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
