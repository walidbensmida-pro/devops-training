package com.harington.devops_training.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.harington.devops_training.kafka.model.ContractDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContractEligibilityService {

    public boolean isContractEligible(ContractDto contract) {
        return contract.getEndDate() != null
                && contract.getEndDate().isAfter(LocalDate.now())
                && contract.getAmount() != null
                && contract.getAmount().compareTo(BigDecimal.valueOf(10_000_000L)) > 0;
    }

    public boolean isBatchEligible(List<ContractDto> batch) {
        return batch.stream().anyMatch(this::isContractEligible);
    }

    public BatchSimpleStats computeBatchStats(List<ContractDto> contractDtos) {
        int eligibleCount = 0;
        int ineligibleCount = 0;
        BigDecimal eligibleSum = BigDecimal.ZERO;
        BigDecimal ineligibleSum = BigDecimal.ZERO;
        for (ContractDto contract : contractDtos) {
            boolean isEligible = isContractEligible(contract);
            if (isEligible) {
                eligibleCount++;
                eligibleSum = eligibleSum.add(contract.getAmount());
            } else {
                ineligibleCount++;
                ineligibleSum = ineligibleSum.add(contract.getAmount());
            }
        }
        return new BatchSimpleStats(eligibleCount, ineligibleCount, eligibleSum, ineligibleSum);
    }

    public static class BatchSimpleStats {
        public int eligibleCount;
        public int ineligibleCount;
        public BigDecimal eligibleSum;
        public BigDecimal ineligibleSum;

        public BatchSimpleStats(int eligibleCount, int ineligibleCount, BigDecimal eligibleSum,
                BigDecimal ineligibleSum) {
            this.eligibleCount = eligibleCount;
            this.ineligibleCount = ineligibleCount;
            this.eligibleSum = eligibleSum;
            this.ineligibleSum = ineligibleSum;
        }
    }
}