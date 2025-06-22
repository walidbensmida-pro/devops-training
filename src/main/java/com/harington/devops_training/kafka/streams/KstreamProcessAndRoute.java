package com.harington.devops_training.kafka.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.devops_training.kafka.model.ContractDto;
import com.harington.devops_training.kafka.serde.ContractListSerde;
import com.harington.devops_training.service.ContractEligibilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.List;

import static com.harington.devops_training.kafka.constants.KafkaConstants.DEVOPS_TRAINING_STREAMING_TOPIC;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
@Slf4j
public class KstreamProcessAndRoute {
    private final ObjectMapper objectMapper;
    private final ContractEligibilityService eligibilityService;
    private final ContractListSerde contractListSerde;

    /**
     * Imagine que tu as une boîte aux lettres (topic Kafka) où plein de gens
     * peuvent déposer des enveloppes (messages).
     * Ton application, c'est le facteur qui va ouvrir chaque enveloppe, regarder ce
     * qu'il y a dedans, et faire des actions.
     *
     * Ici, chaque enveloppe contient une "liste de contrats" (comme une liste de
     * fiches de personnes).
     *
     * On va :
     * 1. Ouvrir chaque enveloppe et compter combien il y a de fiches dedans.
     * 2. Faire des calculs (combien de fiches sont "éligibles", combien ne le sont
     * pas, etc.)
     * 3. Ranger les résultats dans des boîtes différentes selon la couleur de la
     * fiche (éligible ou pas).
     * 4. Garder une trace de tout ce qu'on fait (c'est la topologie, le plan de
     * travail du facteur !)
     */
    @Bean
    public KStream<String, List<ContractDto>> processAndRouteContracts(StreamsBuilder builder) {
        // 1. Le facteur ouvre la boîte aux lettres et prend chaque enveloppe
        KStream<String, List<ContractDto>> contractListStream = builder.stream(
                DEVOPS_TRAINING_STREAMING_TOPIC,
                Consumed.with(Serdes.String(), contractListSerde));

        // 2. Il compte combien il y a de fiches dans chaque enveloppe
        contractListStream
                .foreach((key, list) -> log.info("[KStream][2] Contrats reçus - key: {}, nb: {}", key, list.size()));

        // 3. Il fait des calculs sur toutes les fiches de l'enveloppe (combien sont
        // éligibles, etc.)
        KStream<String, String> resultStream = contractListStream.mapValues(list -> {
            var stats = eligibilityService.computeBatchStats(list);
            try {
                return objectMapper.writeValueAsString(stats); // Il écrit le résultat sur une feuille
            } catch (Exception e) {
                log.error("[KStream] Erreur de sérialisation stats: {}", e.getMessage(), e);
                return null;
            }
        });

        // 4. Il met la feuille de résultats dans une boîte spéciale "résultats"
        resultStream.to("devops-training-streams-result", Produced.with(Serdes.String(), Serdes.String()));

        // 5. Il prend chaque fiche de la liste et les regarde une par une
        KStream<String, ContractDto> individualContracts = contractListStream.flatMapValues(contracts -> contracts);

        // 6. Il sépare les fiches vertes (éligibles) et rouges (ineligibles)
        KStream<String, ContractDto> eligibleContracts = individualContracts
                .filter((key, contract) -> eligibilityService.isContractEligible(contract));
        KStream<String, ContractDto> ineligibleContracts = individualContracts
                .filter((key, contract) -> !eligibilityService.isContractEligible(contract));

        // 7. Il met les fiches vertes dans la boîte "éligibles"
        eligibleContracts
                .mapValues(this::serializeContract)
                .to("contracts-eligible", Produced.with(Serdes.String(), Serdes.String()));

        // 8. Il met les fiches rouges dans la boîte "ineligibles"
        ineligibleContracts
                .mapValues(this::serializeContract)
                .to("contracts-ineligible", Produced.with(Serdes.String(), Serdes.String()));

        // 9. Il affiche le plan de travail du facteur (la topologie)
        org.apache.kafka.streams.Topology topology = builder.build();
        log.info("[KStream][TOPOLOGIE] {}", topology.describe());

        return contractListStream;
    }

    /**
     * Méthode utilitaire pour sérialiser un contrat en JSON
     */
    private String serializeContract(ContractDto contract) {
        try {
            return objectMapper.writeValueAsString(contract);
        } catch (Exception e) {
            log.error("[KStream] Erreur de sérialisation contrat: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Exemple pédagogique : Utilisation d'un state store (magasin d'état)
     *
     * Imagine que le facteur veut aussi garder un carnet où il note combien de
     * fiches vertes (éligibles)
     * il a vu pour chaque client (clé). Ce carnet, c'est le "state store" !
     *
     * À chaque fois qu'il voit une fiche verte, il ajoute +1 dans le carnet pour ce
     * client.
     * Si l'application s'arrête, le carnet est sauvegardé dans une boîte spéciale
     * (le changelog Kafka),
     * et il pourra le retrouver à la reprise !
     */
    @Bean
    public KTable<String, Long> eligibleContractsCount(StreamsBuilder builder) {
        // Le facteur ouvre la boîte aux lettres et prend chaque enveloppe
        KStream<String, List<ContractDto>> contractListStream = builder.stream(
                DEVOPS_TRAINING_STREAMING_TOPIC,
                Consumed.with(Serdes.String(), contractListSerde));

        // Il prend chaque fiche de la liste et les regarde une par une
        KStream<String, ContractDto> individualContracts = contractListStream.flatMapValues(contracts -> contracts);

        // Il ne garde que les fiches vertes (éligibles)
        KStream<String, ContractDto> eligibleContracts = individualContracts
                .filter((key, contract) -> eligibilityService.isContractEligible(contract));

        // Il compte combien de fiches vertes il a vu pour chaque client (clé)
        KTable<String, Long> eligibleCountByClient = eligibleContracts
                .groupByKey()
                .count(Materialized.as("eligible-contracts-count-store")); // Ici, on crée le state store !

        // Il affiche le contenu du carnet à chaque mise à jour
        eligibleCountByClient.toStream().foreach(
                (key, count) -> log.info("[KTable][StateStore] Client: {} a {} contrats éligibles", key, count));

        return eligibleCountByClient;
    }
}
