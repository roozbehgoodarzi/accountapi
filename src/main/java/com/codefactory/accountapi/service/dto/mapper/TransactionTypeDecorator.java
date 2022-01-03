package com.codefactory.accountapi.service.dto.mapper;

import com.codefactory.accountapi.model.Transaction;
import com.codefactory.accountapi.service.dto.TransactionDTO;
import com.codefactory.accountapi.service.dto.TransactionTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Objects;

public class TransactionTypeDecorator implements TransactionMapper{

    @Autowired
    @Qualifier("delegate")
    TransactionMapper delegate;

    @Override
    public TransactionDTO toDto(Transaction transaction) {
        final TransactionDTO transactionDTO= delegate.toDto(transaction);
        if(Objects.nonNull(transaction.getTransferFrom()) && Objects.nonNull(transaction.getTransferTo())){
            transactionDTO.setTransactionType(TransactionTypeEnum.TRANSFER);
        }else if(Objects.nonNull(transaction.getTransferFrom())){
            transactionDTO.setTransactionType(TransactionTypeEnum.WITHDRAW);
        }else if(Objects.nonNull(transaction.getTransferTo())){
            transactionDTO.setTransactionType(TransactionTypeEnum.DEPOSIT);
        }
        return transactionDTO;
    }
}
