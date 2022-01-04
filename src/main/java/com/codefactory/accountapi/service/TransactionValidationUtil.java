package com.codefactory.accountapi.service;

import com.codefactory.accountapi.model.Account;
import com.codefactory.accountapi.model.AccountTypeEnum;
import com.codefactory.accountapi.service.exception.AccountLockException;
import com.codefactory.accountapi.service.exception.TransferValidationException;
import com.codefactory.accountapi.service.exception.WithdrawValidationException;

import java.util.Objects;

public class TransactionValidationUtil {

    //to hide constructor
    private TransactionValidationUtil() {
    }

    /**
     * validates if the amount can be transferred between 2 accounts
     * @param fromAccount
     * @param toAccount
     * @param amount
     * @throws Exception
     */
    public static void validateTransfer(Account fromAccount, Account toAccount, Long amount) throws AccountLockException, TransferValidationException {
        if(fromAccount.getLock()){
            throw new AccountLockException(String.format("Account: %s is locked", fromAccount.getIban()));
        }
        if(Boolean.TRUE.equals(!fromAccount.getWithdrawable()) && Objects.nonNull(fromAccount.getReferenceAccount())) {
            if (!fromAccount.getReferenceAccount().getIban().equals(toAccount.getIban())) {
                throw new TransferValidationException(String.format("Can not withdraw from the source Account: %s", fromAccount.getIban()));
            }
        }else if (Boolean.FALSE.equals(fromAccount.getWithdrawable())){
            throw new TransferValidationException(String.format("Unable to withdraw from account: %s", fromAccount.getIban()));
        }
        if(fromAccount.getBalance()< amount){
            throw new TransferValidationException(String.format("Account balance is not enough: %s", fromAccount.getIban()));
        }
    }

    /**
     * validates if the amount could be withdrawn from an account
     * @param account
     * @throws Exception
     */
    public static void validateWithdraw(Account account) throws WithdrawValidationException {
        if(!account.getType().equals(AccountTypeEnum.CHECKING)){
            throw new WithdrawValidationException("Withdrawing money is only possible for Checking accounts");
        }
    }
}
