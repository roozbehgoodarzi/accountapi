package com.codefactory.accountapi.service.dto.mapper;

import com.codefactory.accountapi.model.Transaction;
import com.codefactory.accountapi.service.dto.TransactionDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(value = TransactionTypeDecorator.class)
public interface TransactionMapper {
    @Mapping(target = "transactionType", ignore = true)
    @Mapping(target = "transactionNumber", source = "id")
    @Mapping(target = "transferFrom", source = "transferFrom.iban")
    @Mapping(target = "transferTo", source = "transferTo.iban")
    TransactionDTO toDto(Transaction transaction);

}
