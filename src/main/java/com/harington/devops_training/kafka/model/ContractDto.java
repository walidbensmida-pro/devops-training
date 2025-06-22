package com.harington.devops_training.kafka.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto {

    private String id;
    private String label;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amount;
    private Boolean isEligible;

    public static List<ContractDto> generateMocks(int count) {
        LocalDate today = LocalDate.now();
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> {
                    LocalDate start = today.minusDays(i * 10L);
                    LocalDate end = today.plusDays(i % 3 == 0 ? -5 : 10 + i); // certains déjà expirés
                    BigDecimal amount = BigDecimal.valueOf(5_000_000L + (i * 2_000_000L));
                    boolean eligible = end.isAfter(today) && amount.compareTo(BigDecimal.valueOf(10_000_000L)) > 0;
                    return ContractDto.builder()
                            .id(String.valueOf(i))
                            .label("Contrat " + i)
                            .description("Description du contrat " + i)
                            .startDate(start)
                            .endDate(end)
                            .amount(amount)
                            .isEligible(eligible)
                            .build();
                })
                .toList();
    }

    public static List<List<ContractDto>> partition(List<ContractDto> list, int batchSize) {
        int totalSize = list.size();
        int numBatches = (int) Math.ceil((double) totalSize / batchSize);

        return IntStream.range(0, numBatches)
                .mapToObj(i -> list.subList(i * batchSize, Math.min((i + 1) * batchSize, totalSize)))
                .toList();
    }
}
