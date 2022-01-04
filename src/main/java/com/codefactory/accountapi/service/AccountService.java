package com.codefactory.accountapi.service;

import com.codefactory.accountapi.model.Account;
import com.codefactory.accountapi.model.AccountBuilder;
import com.codefactory.accountapi.model.AccountTypeEnum;
import com.codefactory.accountapi.model.Director;
import com.codefactory.accountapi.repository.AccountRepository;
import com.codefactory.accountapi.service.dto.AccountDTO;
import com.codefactory.accountapi.service.dto.BalanceDTO;
import com.codefactory.accountapi.service.dto.TransactionDTO;
import com.codefactory.accountapi.service.dto.mapper.AccountMapper;
import com.codefactory.accountapi.service.dto.mapper.BalanceMapper;
import com.codefactory.accountapi.service.dto.mapper.TransactionMapper;
import com.codefactory.accountapi.service.exception.AccountNotFoundException;
import com.codefactory.accountapi.service.exception.InvalidAccountTypeException;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;
    private final TransactionMapper transactionMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, BalanceMapper balanceMapper, TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.balanceMapper = balanceMapper;
        this.transactionMapper = transactionMapper;
    }

    public AccountDTO createAccount(AccountTypeEnum accountType, CountryCode countryCode, String referenceAccountIban) throws InvalidAccountTypeException {
        Director director = new Director();
        AccountBuilder builder = new AccountBuilder();
        switch (accountType){
            case SAVING:
                var referenceAccount = accountRepository.findAccountByIban(referenceAccountIban).orElse(null);
                if(Objects.nonNull(referenceAccount) && !referenceAccount.getType().equals(AccountTypeEnum.CHECKING)){
                    throw new InvalidAccountTypeException("Reference account should be a checking account");
                }
                director.constructSavingAccount(builder, referenceAccount);
                break;
            case PRIVATE_LOAN:
                director.constructPrivateLoanAccount(builder);
                break;
            default:
                director.constructCheckingAccount(builder);
                break;
        }

        var account = builder.getResult();
        //just for the sake of not extending the domain too much a random Iban get assigned to account
        Iban iban = Iban.random(countryCode);
        account.setIban(iban.toString());
        return accountMapper.toDto(accountRepository.save(account));
    }

    public AccountDTO blockAccount(String iban) throws AccountNotFoundException {
        var account = accountRepository.findAccountByIban(iban).orElseThrow(AccountNotFoundException::new);
        account.setLock(true);
        return accountMapper.toDto(accountRepository.save(account));
    }
    public AccountDTO unblockAccount(String iban) throws AccountNotFoundException {
        var account = accountRepository.findAccountByIban(iban).orElseThrow(AccountNotFoundException::new);
        account.setLock(false);
        return accountMapper.toDto(accountRepository.save(account));
    }


    public BalanceDTO getBalance(String iban) throws AccountNotFoundException {
        var account = accountRepository.findAccountByIban(iban).orElseThrow(AccountNotFoundException::new);
        return balanceMapper.map(account).withTime(Instant.now());
    }

    public List<AccountDTO> getAccountListByType(AccountTypeEnum accountType) {
        return accountRepository.findAccountsByType(accountType)
                .stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionHistory(String iban) throws AccountNotFoundException {
        var account = accountRepository.findAccountByIban(iban).orElseThrow(AccountNotFoundException::new);
        var transactions = Stream.concat(account.getWithdraws().stream(), account.getDeposits().stream())
                .collect(Collectors.toList());
        return transactions.stream().map(transactionMapper::toDto)
                .sorted()
                .collect(Collectors.toList());
    }

    public AccountDTO assignReferenceAccount(String savingAccountIban, String checkingAccountIban) throws AccountNotFoundException, InvalidAccountTypeException {
        Account checkingAccount = accountRepository.findAccountByIban(checkingAccountIban).orElseThrow(AccountNotFoundException::new);
        if(!checkingAccount.getType().equals(AccountTypeEnum.CHECKING)){
            throw new InvalidAccountTypeException("Reference Account must be of a checking type");
        }
        Account savingAccount = accountRepository.findAccountByIban(savingAccountIban).orElseThrow(AccountNotFoundException::new);
        if(!savingAccount.getType().equals(AccountTypeEnum.SAVING)){
            throw new InvalidAccountTypeException("Account must be of a saving type");
        }
        savingAccount.setReferenceAccount(checkingAccount);
        return accountMapper.toDto(accountRepository.save(savingAccount));
    }
}
