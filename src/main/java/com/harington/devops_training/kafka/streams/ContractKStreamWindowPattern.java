package com.harington.devops_training.kafka.streams;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.devops_training.kafka.model.ContractDto;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.StreamsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ContractKStreamWindowPattern {
    private static final Logger logger = LoggerFactory.getLogger(ContractKStreamWindowPattern.class);
    @Autowired
    private ContractEligibilityService eligibilityService;

    /**
     * Exemple pédagogique : Agrégation par fenêtre temporelle (nombre de contrats
     * éligibles par minute)
     */
    @Bean
    public KStream<String, String> contractKStreamWithWindow(StreamsBuilder builder) {
        KStream<String, String> stream = builder.stream("devops-training-topic");
        stream
                .flatMapValues(value -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(value, new TypeReference<List<ContractDto>>() {
                        });
                    } catch (Exception e) {
                        logger.error("[KStream][Window] Erreur de parsing batch", e);
                        return List.of();
                    }
                })
                .filter((key, contract) -> eligibilityService.isContractEligible(contract))
                .groupByKey()
                .windowedBy(
                        org.apache.kafka.streams.kstream.TimeWindows.ofSizeWithNoGrace(java.time.Duration.ofMinutes(1)))
                .count()
                .toStream()
                .foreach((windowedKey, count) -> logger.info("[KStream][Window] {} contrats éligibles entre {} et {}",
                        count, windowedKey.window().startTime(), windowedKey.window().endTime()));
        return stream;
    }
}