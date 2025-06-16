package com.harington.devops_training.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ContractResultDto {
    private String contractId;
    private boolean eligible;
    private java.math.BigDecimal amount;

    public ContractResultDto(String contractId, boolean eligible, BigDecimal amount) {
        this.contractId = contractId;
        this.eligible = eligible;
        this.amount = amount;
    }

}
