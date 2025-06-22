package com.harington.devops_training.kafka.consumer;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.harington.devops_training.kafka.model.ContractResultDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.devops_training.kafka.config.KafkaDemoProperties;
import com.harington.devops_training.kafka.model.ContractDto;
import com.harington.devops_training.kafka.producer.KafkaProducerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumerService {
    private final KafkaDemoProperties kafkaDemoProperties;
    private final KafkaProducerService kafkaProducerService;

    private final List<String> messages = Collections.synchronizedList(new ArrayList<>());
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.key-deserializer:org.apache.kafka.common.serialization.StringDeserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer:org.apache.kafka.common.serialization.StringDeserializer}")
    private String valueDeserializer;

    @Value("${spring.kafka.consumer.auto-offset-reset:earliest}")
    private String autoOffsetReset;

    private static final String CONSUMER_GROUP = "devops-training-group";

    public KafkaConsumerService(KafkaDemoProperties kafkaDemoProperties, KafkaProducerService kafkaProducerService,
            ObjectMapper objectMapper) {
        this.kafkaDemoProperties = kafkaDemoProperties;
        this.kafkaProducerService = kafkaProducerService;
        this.objectMapper = objectMapper;
    }

    /**
     * Méthode appelée pour écouter les messages du topic Kafka.
     * Elle traite les messages en lot et publie le résultat sur un autre topic.
     *
     * @param record le message reçu du topic Kafka
     */
    @KafkaListener(topics = "#{@kafkaDemoProperties.topic}", groupId = CONSUMER_GROUP)
    public void listen(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();
        int partition = record.partition();
        // Ajout pédagogique : traitement batch JSON
        try {
            calculateBatchSummaryAndSendResult(value, partition);
        } catch (Exception e) {
            log.error("Erreur lors du traitement du batch Kafka : {}", e.getMessage(), e);
            messages.add("[Erreur][partition=" + partition + "] Batch non traité : " + e.getMessage());
        }
        // ...affichage classique pour la démo...
        if (key != null) {
            messages.add("[clé=" + key + ", partition=" + partition + "] " + value);
        } else {
            messages.add("[partition=" + partition + "] " + value);
        }
    }

    /**
     * Retourne la liste des messages traités par le consommateur.
     *
     * @return la liste des messages
     */
    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    /**
     * Poll les messages depuis Kafka.
     *
     * @return la liste des messages
     */
    public List<String> pollMessagesFromKafka() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                kafkaDemoProperties.getPollingGroupPrefix() + "-" + UUID.randomUUID());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        List<String> result = new ArrayList<>();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(kafkaDemoProperties.getTopic()));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
            log.info("[KafkaDemo] Polling direct : {} messages lus depuis le topic {}", records.count(),
                    kafkaDemoProperties.getTopic());
            for (ConsumerRecord<String, String> record : records) {
                String key = record.key();
                String value = record.value();
                int partition = record.partition();
                if (key != null) {
                    result.add("[clé=" + key + ", partition=" + partition + "] " + value);
                } else {
                    result.add("[partition=" + partition + "] " + value);
                }
            }
        }
        return result;
    }

    /**
     * Retourne le nom du topic utilisé pour la démo.
     *
     * @return le nom du topic
     */
    public String getDemoTopic() {
        return kafkaDemoProperties.getTopic();
    }

    /**
     * Traite le batch, logge, ajoute le résumé à messages, et publie le résultat
     * sur le topic résultat.
     *
     * @return le JSON envoyé sur le topic résultat
     */
    private void calculateBatchSummaryAndSendResult(String value, int partition)
            throws JsonProcessingException, JsonMappingException {
        List<ContractDto> batch = objectMapper.readValue(value, new TypeReference<List<ContractDto>>() {
        });
        StringBuilder batchSummary = new StringBuilder();
        batchSummary.append("[Batch][partition=").append(partition).append("] ");
        batchSummary.append("Contrats traités: ");
        List<ContractResultDto> results = new ArrayList<>();
        for (ContractDto contract : batch) {
            boolean isEligible = false;
            try {
                int idInt = Integer.parseInt(contract.getId());
                isEligible = (idInt % 2 == 0);
            } catch (NumberFormatException e) {
                // Si l'id n'est pas un nombre, on considère non éligible
            }
            log.info("[Batch][partition={}] Contrat id={} label='{}' => isEligible={}", partition, contract.getId(),
                    contract.getLabel(), isEligible);
            batchSummary.append(contract.getId()).append(":").append(isEligible ? "✔" : "✘").append(", ");
            results.add(new ContractResultDto(contract.getId(), isEligible, new BigDecimal(10)));
        }
        // Retirer la dernière virgule
        if (batch.size() > 0) {
            batchSummary.setLength(batchSummary.length() - 2);
        }
        messages.add(batchSummary.toString());
        // Publier le résultat sur le topic résultat
        String resultJson = objectMapper.writeValueAsString(results);
        kafkaProducerService.sendMessage("devops-training-result", "result-batch", resultJson);
    }

}