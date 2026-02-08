package com.example.classcompare.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SemanticSimilarityService {

    /**
     * Calculates Levenshtein distance-based similarity (0 to 1)
     */
    public double calculateLevenshteinSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;
        
        int distance = levenshteinDistance(s1.toLowerCase(), s2.toLowerCase());
        int maxLength = Math.max(s1.length(), s2.length());
        
        if (maxLength == 0) return 1.0;
        return 1.0 - ((double) distance / maxLength);
    }

    /**
     * Calculates Jaccard similarity for token-based matching (0 to 1)
     */
    public double calculateJaccardSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;

        Set<String> tokens1 = tokenize(s1);
        Set<String> tokens2 = tokenize(s2);

        if (tokens1.isEmpty() && tokens2.isEmpty()) return 1.0;
        if (tokens1.isEmpty() || tokens2.isEmpty()) return 0.0;

        Set<String> intersection = new HashSet<>(tokens1);
        intersection.retainAll(tokens2);

        Set<String> union = new HashSet<>(tokens1);
        union.addAll(tokens2);

        return (double) intersection.size() / union.size();
    }

    /**
     * Calculates cosine similarity using simple character-based vectors
     * For a more sophisticated implementation, word embeddings would be used
     */
    public double calculateCosineSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;

        Map<String, Integer> vector1 = createCharacterVector(s1.toLowerCase());
        Map<String, Integer> vector2 = createCharacterVector(s2.toLowerCase());

        return cosineSimilarity(vector1, vector2);
    }

    /**
     * Calculates combined similarity score using weighted average
     * Default weights: Levenshtein 40%, Jaccard 30%, Cosine 30%
     */
    public double calculateCombinedScore(String s1, String s2) {
        double levenshtein = calculateLevenshteinSimilarity(s1, s2);
        double jaccard = calculateJaccardSimilarity(s1, s2);
        double cosine = calculateCosineSimilarity(s1, s2);

        return (levenshtein * 0.4) + (jaccard * 0.3) + (cosine * 0.3);
    }

    /**
     * Calculates combined similarity score with custom weights
     */
    public double calculateCombinedScore(String s1, String s2, 
                                        double levenshteinWeight, 
                                        double jaccardWeight, 
                                        double cosineWeight) {
        double levenshtein = calculateLevenshteinSimilarity(s1, s2);
        double jaccard = calculateJaccardSimilarity(s1, s2);
        double cosine = calculateCosineSimilarity(s1, s2);

        return (levenshtein * levenshteinWeight) + 
               (jaccard * jaccardWeight) + 
               (cosine * cosineWeight);
    }

    /**
     * Finds best match for a given string from a list of candidates
     * Returns the best match and its similarity score
     */
    public Map.Entry<String, Double> findBestMatch(String target, Collection<String> candidates, double threshold) {
        String bestMatch = null;
        double bestScore = 0.0;

        for (String candidate : candidates) {
            double score = calculateCombinedScore(target, candidate);
            if (score > bestScore && score >= threshold) {
                bestScore = score;
                bestMatch = candidate;
            }
        }

        return bestMatch != null ? Map.entry(bestMatch, bestScore) : null;
    }

    // Helper methods

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(
                    dp[i - 1][j] + 1,      // deletion
                    dp[i][j - 1] + 1),     // insertion
                    dp[i - 1][j - 1] + cost); // substitution
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private Set<String> tokenize(String s) {
        Set<String> tokens = new HashSet<>();
        // Split on camelCase, spaces, and underscores
        String[] parts = s.split("(?<=[a-z])(?=[A-Z])|[\\s_]+");
        for (String part : parts) {
            if (!part.isEmpty()) {
                tokens.add(part.toLowerCase());
            }
        }
        return tokens;
    }

    private Map<String, Integer> createCharacterVector(String s) {
        Map<String, Integer> vector = new HashMap<>();
        for (String token : tokenize(s)) {
            vector.put(token, vector.getOrDefault(token, 0) + 1);
        }
        return vector;
    }

    private double cosineSimilarity(Map<String, Integer> v1, Map<String, Integer> v2) {
        if (v1.isEmpty() || v2.isEmpty()) return 0.0;

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        Set<String> allKeys = new HashSet<>(v1.keySet());
        allKeys.addAll(v2.keySet());

        for (String key : allKeys) {
            int val1 = v1.getOrDefault(key, 0);
            int val2 = v2.getOrDefault(key, 0);
            
            dotProduct += val1 * val2;
            norm1 += val1 * val1;
            norm2 += val2 * val2;
        }

        if (norm1 == 0.0 || norm2 == 0.0) return 0.0;
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
