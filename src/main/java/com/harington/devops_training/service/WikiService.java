package com.harington.devops_training.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class WikiService {
    public String searchWikiForQuestion(String wikiDir, String question) throws IOException {
        // Extraction des mots-clés (mots de plus de 3 lettres)
        Set<String> keywords = extractKeywords(question);
        StringBuilder sb = new StringBuilder();
        Files.walk(Paths.get(wikiDir))
                .filter(p -> p.toString().endsWith(".md"))
                .forEach(p -> {
                    try {
                        String content = new String(Files.readAllBytes(p));
                        // Recherche par mot-clé, ligne par ligne
                        List<String> foundLines = new ArrayList<>();
                        for (String line : content.split("\n")) {
                            String normLine = normalize(line);
                            for (String kw : keywords) {
                                if (normLine.contains(kw)) {
                                    foundLines.add(line);
                                    break;
                                }
                            }
                        }
                        if (!foundLines.isEmpty()) {
                            sb.append("\n---\n").append(p.getFileName()).append(":\n");
                            foundLines.forEach(l -> sb.append(l).append("\n"));
                        }
                    } catch (IOException e) {
                        /* ignore */ }
                });
        // Si rien trouvé, fallback sur le premier fichier
        if (sb.isEmpty()) {
            Optional<Path> first = Files.walk(Paths.get(wikiDir))
                    .filter(p -> p.toString().endsWith(".md")).findFirst();
            if (first.isPresent()) {
                sb.append(new String(Files.readAllBytes(first.get())));
            }
        }
        return sb.toString();
    }

    private Set<String> extractKeywords(String question) {
        String normalized = normalize(question);
        String[] words = normalized.split("\\W+");
        Set<String> keywords = new HashSet<>();
        for (String w : words) {
            if (w.length() > 3)
                keywords.add(w);
        }
        return keywords;
    }

    private String normalize(String s) {
        String norm = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "").toLowerCase();
        return norm;
    }

    public String readAllMarkdownFiles(String wikiDir) throws IOException {
        StringBuilder sb = new StringBuilder();
        Files.walk(Paths.get(wikiDir))
                .filter(p -> p.toString().endsWith(".md"))
                .forEach(p -> {
                    try {
                        sb.append("\n---\n").append(new String(Files.readAllBytes(p)));
                    } catch (IOException e) {
                        /* ignore */ }
                });
        return sb.toString();
    }
}
