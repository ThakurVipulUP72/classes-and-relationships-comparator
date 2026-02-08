package com.example.classcompare.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SuggestionEngine {

    private final SemanticSimilarityService similarityService;

    public SuggestionEngine(SemanticSimilarityService similarityService) {
        this.similarityService = similarityService;
    }

    /**
     * Finds potential matches for unmatched items using fuzzy matching
     */
    public List<Map<String, Object>> findPotentialMatches(
            Collection<String> unmatchedManual,
            Collection<String> unmatchedLlm,
            double threshold) {
        
        List<Map<String, Object>> suggestions = new ArrayList<>();
        
        for (String manualItem : unmatchedManual) {
            List<Map<String, Object>> matches = new ArrayList<>();
            
            for (String llmItem : unmatchedLlm) {
                double score = similarityService.calculateCombinedScore(manualItem, llmItem);
                
                if (score >= threshold) {
                    Map<String, Object> match = new HashMap<>();
                    match.put("llmItem", llmItem);
                    match.put("score", score);
                    matches.add(match);
                }
            }
            
            // Sort by score descending
            matches.sort((a, b) -> Double.compare((Double) b.get("score"), (Double) a.get("score")));
            
            if (!matches.isEmpty()) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("manualItem", manualItem);
                suggestion.put("topMatches", matches.subList(0, Math.min(3, matches.size())));
                suggestions.add(suggestion);
            }
        }
        
        return suggestions;
    }

    /**
     * Ranks suggestions by similarity score and context
     */
    public List<Map<String, Object>> rankSuggestions(List<Map<String, Object>> suggestions) {
        // Already sorted by score in findPotentialMatches
        return suggestions;
    }

    /**
     * Identifies items that might be duplicates based on high similarity
     */
    public List<Map<String, Object>> suggestMerges(Collection<String> items, double threshold) {
        List<Map<String, Object>> mergeSuggestions = new ArrayList<>();
        List<String> itemList = new ArrayList<>(items);
        
        for (int i = 0; i < itemList.size(); i++) {
            for (int j = i + 1; j < itemList.size(); j++) {
                String item1 = itemList.get(i);
                String item2 = itemList.get(j);
                
                double score = similarityService.calculateCombinedScore(item1, item2);
                
                if (score >= threshold) {
                    Map<String, Object> merge = new HashMap<>();
                    merge.put("item1", item1);
                    merge.put("item2", item2);
                    merge.put("score", score);
                    merge.put("reason", "High similarity detected");
                    mergeSuggestions.add(merge);
                }
            }
        }
        
        // Sort by score descending
        mergeSuggestions.sort((a, b) -> Double.compare((Double) b.get("score"), (Double) a.get("score")));
        
        return mergeSuggestions;
    }

    /**
     * Generates confidence-based suggestions for all unmatched items
     */
    public Map<String, Object> generateSuggestions(
            Collection<String> unmatchedManual,
            Collection<String> unmatchedLlm,
            double matchThreshold,
            double mergeThreshold) {
        
        Map<String, Object> result = new HashMap<>();
        
        // Find potential matches between manual and LLM items
        List<Map<String, Object>> matches = findPotentialMatches(unmatchedManual, unmatchedLlm, matchThreshold);
        result.put("potentialMatches", matches);
        
        // Find potential duplicate merges within manual items
        List<Map<String, Object>> manualMerges = suggestMerges(unmatchedManual, mergeThreshold);
        result.put("manualDuplicates", manualMerges);
        
        // Find potential duplicate merges within LLM items
        List<Map<String, Object>> llmMerges = suggestMerges(unmatchedLlm, mergeThreshold);
        result.put("llmDuplicates", llmMerges);
        
        return result;
    }
}
