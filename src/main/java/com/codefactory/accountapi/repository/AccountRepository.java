package com.codefactory.accountapi.repository;

import com.codefactory.accountapi.model.Account;
import com.codefactory.accountapi.model.AccountTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findAccountByIban(String iban);
    List<Account> findAccountsByType(AccountTypeEnum type);
}
