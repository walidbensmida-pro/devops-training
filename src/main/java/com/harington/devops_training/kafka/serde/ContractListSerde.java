package com.harington.devops_training.kafka.serde;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.devops_training.kafka.model.ContractDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor

public class ContractListSerde implements Serde<List<ContractDto>> {
    private final ObjectMapper objectMapper;

    @Override
    public Serializer<List<ContractDto>> serializer() {
        return (topic, data) -> {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public Deserializer<List<ContractDto>> deserializer() {
        return (topic, bytes) -> {
            try {
                if (bytes == null)
                    return null;
                return objectMapper.readValue(bytes, new TypeReference<List<ContractDto>>() {
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
