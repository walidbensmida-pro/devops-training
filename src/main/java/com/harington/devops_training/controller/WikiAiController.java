package com.harington.devops_training.controller;

import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.harington.devops_training.service.WikiService;

@RestController
@RequestMapping("/api")
public class WikiAiController {

    @Autowired
    private OpenAiChatClient chatClient;

    @Autowired
    private WikiService wikiService;

    @PostMapping("/ask-wiki")
    public Map<String, String> askWiki(@RequestBody Map<String, String> body) throws IOException {
        String question = body.get("question");
        String wikiContent = wikiService.searchWikiForQuestion("wiki", question);

        String prompt = "Voici un extrait de la documentation de mon projet :\n" + wikiContent +
                "\nRéponds à la question suivante uniquement à partir de cette documentation (sois concis) :\n" + question;
        String answer = chatClient.call(prompt);
        return Map.of("answer", answer);
    }
 
}
