package com.harington.devops_training.streams;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.harington.devops_training.model.ContractDto;
import com.harington.devops_training.model.ContractResultDto;

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

    public BatchStats computeBatchStats(List<ContractDto> contractDtos) {
        List<ContractResultDto> contractResultDtos = new ArrayList<>();
        int eligibleCount = 0;
        int ineligibleCount = 0;
        BigDecimal eligibleSum = BigDecimal.ZERO;
        BigDecimal ineligibleSum = BigDecimal.ZERO;
        StringBuilder batchSummary = new StringBuilder();
        batchSummary.append("[KStream][Batch] Contrats traités: ");

        // Itérer sur chaque contrat pour déterminer son éligibilité
        for (ContractDto contract : contractDtos) {
            boolean isEligible = isContractEligible(contract);
            if (isEligible) {
                eligibleCount++;
                eligibleSum = eligibleSum.add(contract.getAmount());
            } else {
                ineligibleCount++;
                ineligibleSum = ineligibleSum.add(contract.getAmount());
            }
            batchSummary.append(contract.getId())
                    .append(":")
                    .append(isEligible ? "✔" : "✘")
                    .append(", ");
            contractResultDtos.add(new ContractResultDto(contract.getId(), isEligible, contract.getAmount()));
        }
        // Retirer la dernière virgule et espace
        if (!contractDtos.isEmpty()) {
            batchSummary.setLength(batchSummary.length() - 2);
        }
        // Log des statistiques du batch
        batchSummary.append(" | Eligibles: ")
                .append(eligibleCount)
                .append(" (Total: ")
                .append(eligibleSum)
                .append(")");
        batchSummary.append(" | Ineligibles: ")
                .append(ineligibleCount)
                .append(" (Total: ")
                .append(ineligibleSum)
                .append(")");
        log.info(batchSummary.toString());

        // Retourner les résultats et le résumé du batch
        return new BatchStats(contractResultDtos, batchSummary.toString());
    }

    public static class BatchStats {
        public final List<ContractResultDto> results;
        public final String summary;

        public BatchStats(List<ContractResultDto> results, String summary) {
            this.results = results;
            this.summary = summary;
        }
    }
}
