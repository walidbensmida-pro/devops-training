package com.harington.devops_training.kafka.streams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import static com.harington.devops_training.kafka.constants.KafkaConstants.DEVOPS_TRAINING_STREAMING_TOPIC;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
@Slf4j
public class KstreamLogs {
    /**
     * Exemple 1 : Log simple de tous les messages reçus
     * <p>
     * Objectif pédagogique :
     * - Comprendre comment consommer un topic Kafka simplement
     * - Utiliser foreach pour afficher chaque message
     */
    // @Bean
    public KStream<String, String> logAllMessages(StreamsBuilder builder) {
        KStream<String, String> stream = builder.stream(DEVOPS_TRAINING_STREAMING_TOPIC);

        // Affiche chaque message reçu
        stream.foreach((key, value) -> log.info("[KStream][1] Message reçu - key: {}, value: {}", key, value));

        // Filtre et affiche uniquement les messages contenant "important"
        stream.filter((key, value) -> value != null && value.contains("important"))
                .foreach((key, value) -> log.info("[KStream][1][Filtré] Message IMPORTANT - key: {}, value: {}", key,
                        value));
        return stream;

    }
}