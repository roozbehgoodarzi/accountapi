package com.codefactory.accountapi.rest;

import com.codefactory.accountapi.model.Account;
import com.codefactory.accountapi.model.AccountTypeEnum;
import com.codefactory.accountapi.repository.AccountRepository;
import com.codefactory.accountapi.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    Account account = new Account();

    void createAccount(){
        account.setLock(false);
        account.setWithdrawable(true);
        account.setWithdrawable(true);
        account.setBalance(100L);
        account.setType(AccountTypeEnum.CHECKING);
    }

    @Test
    void transferShouldFailForSameSourceAndDestinationAccount() throws Exception {
        createAccount();
        account.setIban("iban1");
        accountRepository.save(account);
        mockMvc.perform(post("/api/transfer")
                .queryParam("fromAccountIban", "iban1")
                .queryParam("toAccountIban", "iban1")
                .queryParam("amount", "50"))
               .andExpect(status().is4xxClientError());
    }
    @Test
    void transfer() throws Exception {
        createAccount();
        account.setIban("iban2");
        accountRepository.save(account);
        Account toAccount = new Account();
        toAccount.setIban("iban3");
        toAccount.setLock(false);
        toAccount.setWithdrawable(true);
        toAccount.setWithdrawable(true);
        toAccount.setBalance(100L);
        accountRepository.save(toAccount);
       mockMvc.perform(post("/api/transfer")
                .queryParam("fromAccountIban", "iban2")
                .queryParam("toAccountIban", "iban3")
                .queryParam("amount", "50"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.transferFrom").value("iban2"))
               .andExpect(jsonPath("$.transferTo").value("iban3"))
               .andExpect(jsonPath("$.amount").value("50"));
    }

    @Test
    void deposit() throws Exception {
        createAccount();
        account.setIban("iban4");
        accountRepository.save(account);
        mockMvc.perform(post("/api/deposit")
                .queryParam("iban", "iban4")
                .queryParam("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transferTo").value("iban4"))
                .andExpect(jsonPath("$.amount").value("100"));
    }

    @Test
    void withdraw() throws Exception {
        createAccount();
        account.setIban("iban5");
        accountRepository.save(account);
        mockMvc.perform(post("/api/withdraw")
                        .queryParam("iban", "iban5")
                        .queryParam("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transferFrom").value("iban5"))
                .andExpect(jsonPath("$.amount").value("100"));
    }
}