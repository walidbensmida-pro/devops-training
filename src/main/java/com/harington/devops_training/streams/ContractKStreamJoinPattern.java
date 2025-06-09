package com.harington.devops_training.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.apache.kafka.streams.StreamsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContractKStreamJoinPattern {
    private static final Logger logger = LoggerFactory.getLogger(ContractKStreamJoinPattern.class);

    /**
     * Exemple pédagogique : Jointure avec un autre topic (enrichissement)
     */
    @Bean
    public KStream<String, String> contractKStreamWithJoin(StreamsBuilder builder) {
        KStream<String, String> contracts = builder.stream("devops-training-topic");
        KStream<String, String> clients = builder.stream("clients-topic");
        contracts.join(
                clients,
                (contractJson, clientJson) -> contractJson + " | Client: " + clientJson,
                JoinWindows.ofTimeDifferenceWithNoGrace(java.time.Duration.ofMinutes(5)),
                StreamJoined.with(
                        Serdes.String(),
                        Serdes.String(),
                        Serdes.String()))
                .foreach((key, value) -> logger.info("[KStream][Join] Contrat enrichi: {}", value));
        return contracts;
    }
}
