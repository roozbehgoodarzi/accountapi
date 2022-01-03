package com.codefactory.accountapi.model;



/**
 * Builder interface defines all possible ways to configure an account.
 */
public interface Builder {
    void setAccountType(AccountTypeEnum accountType);
    void setWithdraw(Boolean withdraw);
    void setDeposit(Boolean deposit);
    void setBalance(Long balance);
    void setLock(Boolean lock);
    void setReferenceAccount(Account account);
}
