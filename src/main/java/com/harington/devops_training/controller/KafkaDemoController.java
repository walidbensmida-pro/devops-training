package com.harington.devops_training.controller;

import java.util.List;

import com.harington.devops_training.kafka.model.ContractDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.harington.devops_training.service.ContractService;
import com.harington.devops_training.kafka.consumer.KafkaConsumerService;
import com.harington.devops_training.kafka.producer.KafkaProducerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/kafka-demo")
@RequiredArgsConstructor
public class KafkaDemoController {
    private final KafkaProducerService producerService;
    private final KafkaConsumerService consumerService;
    private final ContractService contractService;
    private static final Logger logger = LoggerFactory.getLogger(KafkaDemoController.class);

    @GetMapping
    public String kafkaDemoPage(Model model) {
        model.addAttribute("messages", consumerService.getMessages());
        model.addAttribute("topic", consumerService.getDemoTopic());
        model.addAttribute("polledMessages", consumerService.pollMessagesFromKafka());
        return "kafka-demo";
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestParam(required = false) String key, @RequestParam String message,
            RedirectAttributes redirectAttributes) {
        if (key != null && !key.isEmpty()) {
            producerService.sendMessage("devops-training-topic", key, message);
            redirectAttributes.addFlashAttribute("info",
                    "Message envoyé avec clé : '" + key + "' et valeur : '" + message + "'");
        } else {
            producerService.sendMessage("devops-training-topic", message);
            redirectAttributes.addFlashAttribute("info", "Message envoyé sans clé : '" + message + "'");
        }
        return "redirect:/kafka-demo";
    }

    @PostMapping("/publish-test")
    public String publishTestMessage(RedirectAttributes redirectAttributes) {
        String key = "test-key";
        String value = "message de test (" + System.currentTimeMillis() + ")";
        producerService.sendMessage(consumerService.getDemoTopic(), key, value);
        redirectAttributes.addFlashAttribute("info",
                "Message de test publié sur le topic : '" + consumerService.getDemoTopic() + "'");
        return "redirect:/kafka-demo";
    }

    @GetMapping("/messages")
    public List<String> getMessages() {
        return consumerService.getMessages();
    }

    @GetMapping("/messages-poll")
    @ResponseBody
    public List<String> getPolledMessages() {
        return consumerService.pollMessagesFromKafka();
    }

    @GetMapping("/test-kstream")
    public String testKStream() {
        // Génère 10 contrats mock, batch de 5
        var contracts = ContractDto.generateMocks(10);
        var batches = ContractDto.partition(contracts, 5);
        contractService.sendBatches(batches);
        return "redirect:/kafka-demo";
    }
}
