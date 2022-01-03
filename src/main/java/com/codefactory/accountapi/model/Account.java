package com.codefactory.accountapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "iban", unique = true)
    private String iban;

    private AccountTypeEnum type;

    private Long balance;

    private Boolean lock;

    @OneToMany
    private List<Transaction> deposits = new ArrayList<>();

    @OneToMany
    private List<Transaction> withdraws = new ArrayList<>();

    @OneToOne
    private Account referenceAccount;

    private Boolean withdrawable;

    private Boolean depositable;

    public Account(AccountTypeEnum type, Long balance, Boolean withdrawable, Boolean depositable, Account referenceAccount, Boolean lock) {
        this.type = type;
        this.balance = balance;
        this.withdrawable = withdrawable;
        this.depositable = depositable;
        this.referenceAccount = referenceAccount;
        this.lock = lock;
    }
}
