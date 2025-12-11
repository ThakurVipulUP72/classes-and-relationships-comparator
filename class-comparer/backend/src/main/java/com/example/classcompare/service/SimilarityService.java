package com.example.classcompare.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SimilarityService {

    public List<String[]> findSimilar(Map<String, Set<String>> manual, Map<String, Set<String>> llm, int len) {
        List<String[]> matches = new ArrayList<>();
        
        // Flatten all classes for global comparison, or compare per story?
        // The requirement says "between classes of two different files". 
        // This usually implies a global search to find if a class in one story (Manual) 
        // might have been misclassified or named differently in another story (LLM), or just generally similar.
        // Let's do a global comparison of all unique classes found in Manual vs all in LLM.

        Set<String> allManualClasses = new HashSet<>();
        manual.values().forEach(allManualClasses::addAll);

        Set<String> allLlmClasses = new HashSet<>();
        llm.values().forEach(allLlmClasses::addAll);

        for (String a : allManualClasses) {
            for (String b : allLlmClasses) {
                // Avoid exact matches (they are already "common")
                if (a.equalsIgnoreCase(b)) continue;

                if (hasCommonSubstring(a, b, len)) {
                    matches.add(new String[]{a, b});
                }
            }
        }
        return matches;
    }

    private boolean hasCommonSubstring(String a, String b, int len) {
        if (a == null || b == null || len <= 0) return false;
        String A = a.toLowerCase();
        String B = b.toLowerCase();
        
        if (A.length() < len || B.length() < len) return false;

        for (int i = 0; i <= A.length() - len; i++) {
            String sub = A.substring(i, i + len);
            if (B.contains(sub)) {
                return true;
            }
        }
        return false;
    }
}
