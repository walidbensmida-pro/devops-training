package com.harington.devops_training.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.demo")
public class KafkaDemoProperties {
    private String topic;
    private String pollingGroupPrefix;
}
