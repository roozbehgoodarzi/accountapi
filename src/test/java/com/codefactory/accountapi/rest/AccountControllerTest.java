package com.codefactory.accountapi.rest;

import com.codefactory.accountapi.model.AccountTypeEnum;
import com.codefactory.accountapi.model.Transaction;
import com.codefactory.accountapi.repository.AccountRepository;
import com.codefactory.accountapi.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void createAccount() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .queryParam("accountType", AccountTypeEnum.CHECKING.name())
                        .queryParam("countryCode", "DE"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.iban").isNotEmpty())
                .andExpect(jsonPath("$.type").value(AccountTypeEnum.CHECKING.name()))
                .andExpect(jsonPath("$.lock").value(false))
                .andExpect(jsonPath("$.withdrawable").value(true))
                .andExpect(jsonPath("$.depositable").value(true));
    }

    @Test
    void shouldFailForSavinggAccountWithInvalidReference() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .queryParam("accountType", AccountTypeEnum.SAVING.name())
                        .queryParam("countryCode", "DE"))
                .andExpect(status().isCreated());
        var savingAccount = accountRepository.findAll().get(0);
        mockMvc.perform(post("/api/accounts")
                        .queryParam("accountType", AccountTypeEnum.SAVING.name())
                        .queryParam("countryCode", "DE")
                        .queryParam("referenceAccountIban", savingAccount.getIban()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void blockAndUnBlockAccount() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .queryParam("accountType", AccountTypeEnum.SAVING.name())
                        .queryParam("countryCode", "DE"))
                .andExpect(status().isCreated());
        var savingAccount = accountRepository.findAll().get(0);
        mockMvc.perform(put("/api/block")
                        .queryParam("iban", savingAccount.getIban()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lock").value(true));
        mockMvc.perform(put("/api/unblock")
                        .queryParam("iban", savingAccount.getIban()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lock").value(false));
    }

    @Test
    void getBalance() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .queryParam("accountType", AccountTypeEnum.CHECKING.name())
                        .queryParam("countryCode", "DE"))
                .andExpect(status().isCreated());
        var account = accountRepository.findAll().get(0);
        account.setBalance(100L);
        accountRepository.save(account);

        mockMvc.perform(get("/api/balance")
                .queryParam("iban", account.getIban()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100));

    }

    @Test
    void getAccountListByType() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .queryParam("accountType", AccountTypeEnum.CHECKING.name())
                        .queryParam("countryCode", "DE"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/accounts")
                        .queryParam("accountType", AccountTypeEnum.CHECKING.name())
                        .queryParam("countryCode", "DE"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/filtered-account-list")
                .queryParam("accountType", AccountTypeEnum.CHECKING.name()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[*]").isArray());
    }

    @Test
    void getTransactionHistory() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .queryParam("accountType", AccountTypeEnum.CHECKING.name())
                        .queryParam("countryCode", "DE")).andReturn();
        var account = accountRepository.findAll().get(0);
        Transaction transaction1 = new Transaction();
        transaction1.setTransferFrom(account);
        Transaction transaction2 = new Transaction();
        transaction2.setTransferTo(account);
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        account.setWithdraws(new ArrayList<>());
        account.getWithdraws().add(transaction1);
        account.setDeposits(new ArrayList<>());
        account.getDeposits().add(transaction2);
        accountRepository.save(account);
        mockMvc.perform(get("/api/transactions").queryParam("iban", account.getIban()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].transactionNumber").value(transaction2.getId()))
                .andExpect(jsonPath("$.[0].transactionNumber").value(transaction1.getId()));
    }
}