package com.codefactory.accountapi.model;

public class AccountBuilder implements Builder{
    private AccountTypeEnum accountType;
    private Boolean withdrawable;
    private Boolean depositable;
    private Long balance;
    private Boolean lock;
    private Account referenceAccount;

    @Override
    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
    }

    @Override
    public void setWithdraw(Boolean withdraw) {
        this.withdrawable = withdraw;
    }

    @Override
    public void setDeposit(Boolean deposit) {
        this.depositable = deposit;
    }

    @Override
    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public void setReferenceAccount(Account referenceAccount) {
        this.referenceAccount = referenceAccount;
    }

    @Override
    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public Account getResult(){
        return new Account(accountType, balance, withdrawable, depositable, referenceAccount, lock);
    }
}
