package com.codefactory.accountapi.service;

import com.codefactory.accountapi.model.Transaction;
import com.codefactory.accountapi.repository.AccountRepository;
import com.codefactory.accountapi.repository.TransactionRepository;
import com.codefactory.accountapi.service.dto.TransactionDTO;
import com.codefactory.accountapi.service.dto.mapper.TransactionMapper;
import com.codefactory.accountapi.service.exception.AccountLockException;
import com.codefactory.accountapi.service.exception.AccountNotFoundException;
import com.codefactory.accountapi.service.exception.TransferValidationException;
import com.codefactory.accountapi.service.exception.WithdrawValidationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.codefactory.accountapi.service.TransactionValidationUtil.validateTransfer;
import static com.codefactory.accountapi.service.TransactionValidationUtil.validateWithdraw;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * transfers amount between 2 accounts
     * @param from
     * @param to
     * @param amount
     * @return
     * @throws Exception
     */
    public TransactionDTO transfer(String from, String to, Long amount) throws TransferValidationException, AccountLockException, AccountNotFoundException {
        if(from.equals(to)){
            throw new TransferValidationException("source and destination account must be different");
        }
        var fromAccount = accountRepository.findAccountByIban(from).orElseThrow(AccountNotFoundException::new);
        var toAccount = accountRepository.findAccountByIban(to).orElseThrow(AccountNotFoundException::new);
        validateTransfer(fromAccount, toAccount, amount);
        Transaction transaction = new Transaction()
                .withTransferTo(toAccount)
                .withTransferFrom(fromAccount)
                .withAmount(amount)
                .withDateTime(Instant.now());
        transactionRepository.save(transaction);

        fromAccount.getWithdraws().add(transaction);
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.getDeposits().add(transaction);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return transactionMapper.toDto(transaction);
    }

    /**
     * Deposits amount into an account
     * @param iban
     * @param amount
     * @return
     */
    public TransactionDTO deposit(String iban, Long amount) throws AccountNotFoundException {
        var toAccount = accountRepository.findAccountByIban(iban).orElseThrow(AccountNotFoundException::new);
        Transaction transaction = new Transaction()
                .withTransferTo(toAccount)
                .withDateTime(Instant.now())
                .withAmount(amount);
        transactionRepository.save(transaction);

        toAccount.setBalance(toAccount.getBalance() + amount);
        toAccount.getDeposits().add(transaction);
        accountRepository.save(toAccount);
        return transactionMapper.toDto(transaction);
    }

    /**
     * withdraws amount from an account
     * @param iban
     * @param amount
     * @return
     * @throws Exception
     */
    public TransactionDTO withdraw(String iban, Long amount) throws WithdrawValidationException, TransferValidationException, AccountLockException, AccountNotFoundException {
        var fromAccount = accountRepository.findAccountByIban(iban).orElseThrow(AccountNotFoundException::new);
        validateWithdraw(fromAccount);
        validateTransfer(fromAccount, null, amount);
        Transaction transaction = new Transaction()
                .withAmount(amount)
                .withTransferFrom(fromAccount)
                .withDateTime(Instant.now());
        transactionRepository.save(transaction);

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        fromAccount.getWithdraws().add(transaction);
        accountRepository.save(fromAccount);
        return transactionMapper.toDto(transaction);
    }

}
