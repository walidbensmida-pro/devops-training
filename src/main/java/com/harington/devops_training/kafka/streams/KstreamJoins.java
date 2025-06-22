package com.harington.devops_training.kafka.streams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;

import static com.harington.devops_training.kafka.constants.KafkaConstants.DEVOPS_TRAINING_STREAMING_TOPIC;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
@Slf4j
public class KstreamJoins {
    /**
     * Exemple 4 : Jointure entre deux topics
     * <p>
     * Objectif pédagogique :
     * - Comprendre comment joindre deux flux Kafka
     * - Enrichir un flux avec des données provenant d'un autre flux
     */
    // @Bean
    public KStream<String, String> contractsJoinWithClients(StreamsBuilder builder) {
        KStream<String, String> contracts = builder.stream(DEVOPS_TRAINING_STREAMING_TOPIC);
        KStream<String, String> clients = builder.stream("clients-topic");

        contracts.join(
                        clients,
                        (contractJson, clientJson) -> contractJson + " | Client: " + clientJson,
                        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5)),
                        StreamJoined.with(Serdes.String(), Serdes.String(), Serdes.String()))
                .foreach((key, value) -> log.info("[KStream][4][Join] Contrat enrichi: {}", value));

        return contracts;
    }
}
