package com.harington.devops_training.kafka.producer;

import com.harington.devops_training.kafka.model.ContractDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final KafkaTemplate<String, List<ContractDto>> contractListKafkaTemplate;

//    @Value("${spring.kafka.producer.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Value("${spring.kafka.properties.schema.registry.url}")
//    private String schemaRegistryUrl;

    public void sendMessage(String topic, String message) {
        stringKafkaTemplate.send(topic, message);
    }

    public void sendMessage(String topic, String key, String message) {
        stringKafkaTemplate.send(topic, key, message);
    }

    public void sendContracts(String topic, String key, List<ContractDto> contracts) {
        try {
            contractListKafkaTemplate.send(topic, key, contracts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void sendContractsAvro(String topic, String key, List<ContractDto>
    // contracts) {
    // Properties props = new Properties();
    // props.put("bootstrap.servers", bootstrapServers);
    // props.put("key.serializer",
    // "org.apache.kafka.common.serialization.StringSerializer");
    // props.put("value.serializer",
    // "io.confluent.kafka.serializers.KafkaAvroSerializer");
    // props.put("schema.registry.url", schemaRegistryUrl);

    // try (KafkaProducer<String, ContractAvro> producer = new
    // KafkaProducer<>(props)) {
    // producer.send(new ProducerRecord<>(topic, key, contract));
    // }
    // }
}
