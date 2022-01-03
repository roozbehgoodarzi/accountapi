package com.codefactory.accountapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
    @JsonProperty
    private String iban;
    @JsonProperty
    private Long balance;
    @JsonProperty
    private Instant time;
}
