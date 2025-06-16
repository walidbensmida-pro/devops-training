package com.harington.devops_training.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractResultDto {
    private String contractId;
    private boolean eligible;
    private BigDecimal amount;

}