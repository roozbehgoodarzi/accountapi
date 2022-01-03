package com.codefactory.accountapi.service.dto;

import com.codefactory.accountapi.model.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {
    @JsonProperty
    private Long transactionNumber;
    @JsonProperty
    private Instant dateTime;
    @JsonProperty
    private String transferFrom;
    @JsonProperty
    private String transferTo;
    @JsonProperty
    private Long amount;
    @JsonProperty
    private TransactionTypeEnum transactionType;
}
