// -----------------------------------------------------------------------------
// Point d'entrée pédagogique Kafka Streams :
// Cette classe montre comment brancher et activer les différents patterns Kafka Streams
// (flux principal, agrégation par fenêtre, jointure) définis dans les autres classes.
//
// Pour chaque pattern, il suffit d'injecter le bean correspondant et de l'activer ici.
// -----------------------------------------------------------------------------
package com.harington.devops_training.streams;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
@Slf4j
public class SimpleKafkaStreamsConfig {

    // Injection des patterns pédagogiques par constructeur (Lombok)
    private final ContractKStreamProcessor contractKStreamProcessor;
    private final ContractKStreamWindowPattern contractKStreamWindowPattern;
    private final ContractKStreamJoinPattern contractKStreamJoinPattern;

    /**
     * Exemple 1 : Flux Kafka Streams simple (log de tous les messages)
     */
    // @Bean
    // public KStream<String, String> kStream(StreamsBuilder builder) {
    // KStream<String, String> stream = builder.stream("devops-training-topic");
    // stream.foreach((key, value) -> log.info("[KStream] Message reçu - key: {}
    // value: {}", key, value));
    // // Pour tester le filtrage simple :
    // stream.filter((key, value) -> value != null && value.contains("important"))
    // .foreach((key, value) -> log.info("[KStream][Filtré] Message IMPORTANT - key:
    // {} value: {}", key,
    // value));
    // return stream;
    // }

    /**
     * Exemple 2 : Activer le pattern principal (traitement batch, routage, stats)
     * Décommente pour activer ce flux pédagogique !
     */
    @Bean
    public KStream<String, String> contractKStream(StreamsBuilder builder) {
        return contractKStreamProcessor.contractKStream(builder);
    }

    /**
     * Exemple 3 : Activer le pattern d'agrégation par fenêtre temporelle
     * Décommente pour activer ce flux pédagogique !
     */
    // @Bean
    // public KStream<String, String> contractKStreamWithWindow(StreamsBuilder
    // builder) {
    // return contractKStreamWindowPattern.contractKStreamWithWindow(builder);
    // }

    /**
     * Exemple 4 : Activer le pattern de jointure (enrichissement)
     * Décommente pour activer ce flux pédagogique !
     */
    // @Bean
    // public KStream<String, String> contractKStreamWithJoin(StreamsBuilder
    // builder) {
    // return contractKStreamJoinPattern.contractKStreamWithJoin(builder);
    // }
}
