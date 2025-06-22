# Kafka : Monitoring & Supervision (Entretien, Simple & Complet)

## 1. Pourquoi monitorer Kafka ?

- Détecter les pannes, ralentissements, saturations.
- Garantir la disponibilité et la performance des applications.
- Anticiper les problèmes avant qu’ils n’impactent la production.

---

## 2. Quoi monitorer ?

- **Brokers** : état, mémoire, CPU, espace disque.
- **Topics/Partitions** : taille, nombre de messages, lag (retard de consommation).
- **Consumers** : offsets, lag, erreurs de consommation.
- **Producers** : taux d’erreur, temps de latence.
- **Connecteurs** (Kafka Connect, Streams) : statut, erreurs, throughput.

---

## 3. Outils classiques

- **JMX** : Kafka expose des métriques via JMX (Java Management Extensions).
- **Prometheus + Grafana** : le combo le plus courant pour collecter et visualiser les métriques.
- **Kafka Manager / AKHQ / Confluent Control Center** : interfaces web pour surveiller et administrer Kafka.
- **Logs** : surveiller les logs Kafka et des applications consommatrices/productrices.

---

## 4. Exemple de stack Prometheus + Grafana

### a. Exporter les métriques Kafka

- Utiliser le **JMX Exporter** (agent Java) pour exposer les métriques Kafka en HTTP pour Prometheus.

```yaml
# Exemple de déploiement docker-compose
kafka:
  image: bitnami/kafka:latest
  environment:
    KAFKA_OPTS: "-javaagent:/jmx_exporter/jmx_prometheus_javaagent-0.16.1.jar=7071:/jmx_exporter/kafka.yml"
  volumes:
    - ./jmx_exporter:/jmx_exporter
  ports:
    - "9092:9092"
    - "7071:7071" # port JMX exporter
```

### b. Scraper avec Prometheus

```yaml
scrape_configs:
  - job_name: 'kafka'
    static_configs:
      - targets: ['kafka:7071']
```

### c. Visualiser avec Grafana

- Utiliser des dashboards prêts à l’emploi (ex: [Grafana Labs Kafka Dashboard](https://grafana.com/grafana/dashboards/721)).

---

## 5. Monitoring du lag des consumers

- **Lag** = nombre de messages non encore consommés (important pour détecter les retards).
- Outils :
  - **Burrow** (LinkedIn) : monitoring avancé du lag.
  - **AKHQ** : visualisation simple du lag par consumer group.
  - **Prometheus** : exporter les métriques de lag via JMX.

---

## 6. Bonnes pratiques

- **Alerter** sur le lag, l’espace disque, l’indisponibilité d’un broker.
- **Centraliser les logs** (ELK, Loki, etc.) pour corréler les erreurs.
- **Documenter les seuils critiques** (ex: lag > 10000 messages, disque < 10% dispo).
- **Automatiser** le redémarrage ou le scaling si besoin (Kubernetes).

---

## 7. À retenir pour l’entretien

- Kafka expose ses métriques via JMX, souvent collectées par Prometheus.
- Grafana permet de visualiser l’état de Kafka (brokers, topics, consumers, lag).
- Surveiller le lag des consumers est essentiel pour garantir la fraîcheur des traitements.
- Des outils comme AKHQ ou Kafka Manager facilitent l’administration au quotidien.
- Toujours mettre en place des alertes sur les points critiques (lag, disque, erreurs).

---

**Ressources utiles** :
- [JMX Exporter Kafka](https://github.com/prometheus/jmx_exporter)
- [AKHQ](https://akhq.io/)
- [Grafana Kafka Dashboards](https://grafana.com/grafana/dashboards/?search=kafka)
- [Burrow](https://github.com/linkedin/Burrow)
