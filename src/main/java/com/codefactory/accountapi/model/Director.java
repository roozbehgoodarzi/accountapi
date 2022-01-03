package com.codefactory.accountapi.model;

public class Director {
    public void constructCheckingAccount(Builder builder){
        builder.setAccountType(AccountTypeEnum.CHECKING);
        builder.setDeposit(true);
        builder.setWithdraw(true);
        builder.setBalance(0L);
        builder.setLock(false);
    }

    public void constructSavingAccount(Builder builder, Account referenceAccount){
        builder.setAccountType(AccountTypeEnum.SAVING);
        builder.setWithdraw(false);
        builder.setDeposit(true);
        builder.setBalance(0L);
        builder.setLock(false);
        builder.setReferenceAccount(referenceAccount);
    }

    public void constructPrivateLoanAccount(Builder builder){
        builder.setAccountType(AccountTypeEnum.PRIVATE_LOAN);
        builder.setWithdraw(false);
        builder.setDeposit(true);
        builder.setBalance(0L);
        builder.setLock(false);
    }
}
