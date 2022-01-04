package com.codefactory.accountapi.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO implements Comparable<TransactionDTO> {
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

    @Override
    public int compareTo(TransactionDTO other) {
        return this.dateTime.compareTo(other.dateTime);
    }
}
