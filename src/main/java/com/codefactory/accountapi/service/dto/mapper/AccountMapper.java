package com.codefactory.accountapi.service.dto.mapper;

import com.codefactory.accountapi.model.Account;
import com.codefactory.accountapi.service.dto.AccountDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDto(Account account);
}
