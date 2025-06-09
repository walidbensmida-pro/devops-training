package com.harington.devops_training.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.devops_training.model.ContractDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContractService {

    private final KafkaProducerService kafkaProducerService;

    public void sendBatches(List<List<ContractDto>> batches) {
        log.info("Sending {} batches to Kafka", batches.size());
        ObjectMapper objectMapper = new ObjectMapper();

        IntStream.range(0, batches.size())
                .forEach(i -> {
                    List<ContractDto> batch = batches.get(i);
                    String batchId = "batch-" + (i + 1);
                    try {
                        var batchAsString = objectMapper.writeValueAsString(batch);
                        kafkaProducerService.sendMessage("devops-training-topic", batchId, batchAsString);
                    } catch (JsonProcessingException e) {
                        log.error("Failed to serialize batch {}: {}", batchId, e.getMessage(), e);
                    }
                });
    }
}
