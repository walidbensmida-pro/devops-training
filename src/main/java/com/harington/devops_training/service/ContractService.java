package com.harington.devops_training.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.devops_training.kafka.constants.KafkaConstants;
import com.harington.devops_training.kafka.model.ContractDto;
import com.harington.devops_training.kafka.producer.KafkaProducerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContractService {

    private final KafkaProducerService kafkaProducerService;

    public void sendBatches(List<List<ContractDto>> batches) {
        log.info("Sending {} batches to Kafka", batches.size());
        IntStream.range(0, batches.size())
                .forEach(i -> {
                    List<ContractDto> contractBatch = batches.get(i);
                    String batchId = "batch-" + (i + 1);
                    contractBatch.forEach(contract -> {
                        kafkaProducerService.sendContracts(KafkaConstants.DEVOPS_TRAINING_STREAMING_TOPIC, batchId,
                                contractBatch);
                        // kafkaProducerService.sendMessage(KafkaConstants.DEVOPS_TRAINING_STREAMING_TOPIC,
                        // contract.getId(),
                        // contractAsString);
                    });
                });
    }
}