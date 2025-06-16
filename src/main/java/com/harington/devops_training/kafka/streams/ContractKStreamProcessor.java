package com.harington.devops_training.kafka.streams;

import java.util.List;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.devops_training.kafka.model.ContractDto;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafkaStreams
@Slf4j
public class ContractKStreamProcessor {
    @Autowired
    private ContractEligibilityService eligibilityService;

    @Bean(name = "processorContractKStream")
    public KStream<String, String> processorContractKStream(StreamsBuilder builder) {
        KStream<String, String> stream = builder.stream("devops-training-topic");
        KStream<String, String> resultStream = stream.mapValues(this::processBatchAndLog);
        resultStream.to("devops-training-result");

        // Routage pédagogique : topic eligible/ineligible
        resultStream.split()
                .branch((key, value) -> isBatchEligible(value),
                        Branched.withConsumer(ks -> ks.to("contracts-eligible")))
                .branch((key, value) -> !isBatchEligible(value),
                        Branched.withConsumer(ks -> ks.to("contracts-ineligible")));

        // Statistiques pédagogiques
        stream.foreach((key, value) -> logBatchStats(value));
        return stream;
    }

    private boolean isBatchEligible(String value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ContractDto> batch = objectMapper.readValue(value, new TypeReference<List<ContractDto>>() {
            });
            return eligibilityService.isBatchEligible(batch);
        } catch (Exception e) {
            log.error("[KStream][Split] Erreur de parsing batch pour routage", e);
            return false;
        }
    }

    private void logBatchStats(String value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ContractDto> batch = objectMapper.readValue(value, new TypeReference<List<ContractDto>>() {
            });
            log.info("[KStream][Stats] Batch {} contrats", batch.size());
        } catch (Exception e) {
            log.error("[KStream][Stats] Erreur de parsing batch", e);
        }
    }

    private String processBatchAndLog(String value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            var contractList = objectMapper.readValue(value, new TypeReference<List<ContractDto>>() {
            });
            var stats = eligibilityService.computeBatchStats(contractList);
            log.info(stats.summary);
            return objectMapper.writeValueAsString(stats.results);
        } catch (Exception e) {
            log.error("[KStream] Erreur lors du traitement du batch: {}", e.getMessage(), e);
            return null;
        }
    }
}