package com.codefactory.accountapi.service.dto;

import com.codefactory.accountapi.model.AccountTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountDTO {
    @JsonProperty
    private String iban;
    @JsonProperty
    private AccountTypeEnum type;
    @JsonProperty
    private Long balance;
    @JsonProperty
    private Boolean lock;
    @JsonProperty
    private Boolean withdrawable;
    @JsonProperty
    private Boolean depositable;

}
