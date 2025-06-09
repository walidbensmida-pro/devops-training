package com.harington.devops_training.model;

public class ContractResultDto {
    private String contractId;
    private boolean eligible;
    private java.math.BigDecimal amount;

    public ContractResultDto(String contractId, boolean eligible, java.math.BigDecimal amount) {
        this.contractId = contractId;
        this.eligible = eligible;
        this.amount = amount;
    }

    public String getContractId() {
        return contractId;
    }

    public boolean isEligible() {
        return eligible;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }
}
