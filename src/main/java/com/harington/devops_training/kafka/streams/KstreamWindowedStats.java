package com.harington.devops_training.kafka.streams;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.devops_training.kafka.model.ContractDto;
import com.harington.devops_training.kafka.serde.ContractListSerde;
import com.harington.devops_training.service.ContractEligibilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;
import java.util.List;

import static com.harington.devops_training.kafka.constants.KafkaConstants.DEVOPS_TRAINING_STREAMING_TOPIC;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
@Slf4j
public class KstreamWindowedStats {
    private final ObjectMapper objectMapper;
    private final ContractEligibilityService eligibilityService;

    /**
     * Exemple 3 : Agrégation par fenêtre temporelle
     *
     * Objectif pédagogique :
     * - Comprendre les agrégations temporelles (fenêtres)
     * - Compter les événements sur une période donnée
     */
    // @Bean
    public KStream<String, String> contractsWindowedStats(StreamsBuilder builder) {
        KStream<String, String> stream = builder.stream(DEVOPS_TRAINING_STREAMING_TOPIC);

        stream.flatMapValues(value -> {
                    try {
                        return objectMapper.readValue(value, new TypeReference<List<ContractDto>>() {
                        });
                    } catch (Exception e) {
                        log.error("[KStream][3][Window] Erreur de parsing batch", e);
                        return List.of();
                    }
                })
                .filter((key, contract) -> eligibilityService.isContractEligible(contract))
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
                .count()
                .toStream()
                .foreach((windowedKey, count) -> log.info("[KStream][3][Window] {} contrats éligibles entre {} et {}",
                        count, windowedKey.window().startTime(), windowedKey.window().endTime()));

        return stream;
    }
}
