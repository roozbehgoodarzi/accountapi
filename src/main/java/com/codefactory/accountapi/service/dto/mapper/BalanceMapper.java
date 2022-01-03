package com.codefactory.accountapi.service.dto.mapper;

import com.codefactory.accountapi.model.Account;
import com.codefactory.accountapi.service.dto.BalanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceMapper {
    @Mapping(target = "time", ignore = true)
    BalanceDTO map(Account account);
}
