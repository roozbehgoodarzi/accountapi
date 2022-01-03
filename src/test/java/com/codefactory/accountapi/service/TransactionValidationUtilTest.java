package com.codefactory.accountapi.service;

import com.codefactory.accountapi.model.Account;
import com.codefactory.accountapi.model.AccountTypeEnum;
import com.codefactory.accountapi.service.exception.AccountLockException;
import com.codefactory.accountapi.service.exception.TransferValidationException;
import com.codefactory.accountapi.service.exception.WithdrawValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class TransactionValidationUtilTest {

    @Test
    void shouldThrowAccountLockException() {
        Account fromAccount = new Account();
        fromAccount.setIban("iban");
        fromAccount.setLock(true);
        Account toAccount = new Account();
        Assertions.assertThrows(AccountLockException.class, () -> TransactionValidationUtil.validateTransfer(fromAccount, toAccount, null), "Account: iban is locked");
    }

    @Test
    void shouldThrowTransferValidationException() {
        Account fromAccount = new Account();
        fromAccount.setIban("iban");
        fromAccount.setLock(false);
        fromAccount.setWithdrawable(true);
        fromAccount.setBalance(100L);
        Account toAccount = new Account();
        Assertions.assertThrows(TransferValidationException.class, () -> TransactionValidationUtil.validateTransfer(fromAccount, toAccount, 1000L), "Can not withdraw from the source Account");
    }

    @Test
    void shouldThrowTransferValidationExceptionForReferenceAccount() {
        Account fromAccount = new Account();
        fromAccount.setIban("iban");
        fromAccount.setLock(false);
        fromAccount.setWithdrawable(false);
        fromAccount.setReferenceAccount(new Account().withIban("iban2"));
        fromAccount.setBalance(100L);
        Account toAccount = new Account();
        Assertions.assertThrows(TransferValidationException.class, () -> TransactionValidationUtil.validateTransfer(fromAccount, toAccount, 1000L), "Account: iban is locked");
    }

    @Test
    void shouldThrowTransferValidationExceptionForFalseWithdrawableAccount() {
        Account fromAccount = new Account();
        fromAccount.setIban("iban");
        fromAccount.setLock(false);
        fromAccount.setWithdrawable(false);
        fromAccount.setBalance(100L);
        Account toAccount = new Account();
        Assertions.assertThrows(TransferValidationException.class, () -> TransactionValidationUtil.validateTransfer(fromAccount, toAccount, 1000L), "Account: iban is locked");
    }

    @Test
    void shouldThrowWithdrawValidationException() {
        Account fromAccount = new Account();
        fromAccount.setType(AccountTypeEnum.SAVING);
        Assertions.assertThrows(WithdrawValidationException.class, () -> TransactionValidationUtil.validateWithdraw(fromAccount), "ithdrawing money is only possible for Checking accounts");
    }

}