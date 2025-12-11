package com.example.classcompare.service;

import com.example.classcompare.model.ComparisonResponse;
import com.example.classcompare.model.Relationship;
import com.example.classcompare.model.StoryComparisonResult;
import com.example.classcompare.model.Metrics;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ComparisonService {

    @SuppressWarnings("unchecked")
    public ComparisonResponse compare(Map<String, Map<String, Object>> manual, Map<String, Map<String, Object>> llm) {
        List<StoryComparisonResult> results = new ArrayList<>();
        
        Set<String> allStories = new HashSet<>();
        allStories.addAll(manual.keySet());
        allStories.addAll(llm.keySet());

        for (String story : allStories) {
            Map<String, Object> manualStoryData = manual.getOrDefault(story, Collections.emptyMap());
            Map<String, Object> llmStoryData = llm.getOrDefault(story, Collections.emptyMap());
            
            // Extract classes
            Set<String> manualClasses = (Set<String>) manualStoryData.getOrDefault("classes", Collections.emptySet());
            Set<String> llmClasses = (Set<String>) llmStoryData.getOrDefault("classes", Collections.emptySet());

            // Compare classes
            Set<String> commonClasses = new HashSet<>(manualClasses);
            commonClasses.retainAll(llmClasses);

            Set<String> onlyManualClasses = new HashSet<>(manualClasses);
            onlyManualClasses.removeAll(llmClasses);

            Set<String> onlyLLMClasses = new HashSet<>(llmClasses);
            onlyLLMClasses.removeAll(manualClasses);

            // Calculate metrics for classes
            Metrics classMetrics = calculateMetrics(commonClasses.size(), onlyManualClasses.size(), onlyLLMClasses.size());

            // Extract relationships
            Set<Relationship> manualRelationships = (Set<Relationship>) manualStoryData.getOrDefault("relationships", Collections.emptySet());
            Set<Relationship> llmRelationships = (Set<Relationship>) llmStoryData.getOrDefault("relationships", Collections.emptySet());

            // Compare relationships
            Set<Relationship> commonRelationships = new HashSet<>(manualRelationships);
            commonRelationships.retainAll(llmRelationships);

            Set<Relationship> onlyManualRelationships = new HashSet<>(manualRelationships);
            onlyManualRelationships.removeAll(llmRelationships);

            Set<Relationship> onlyLLMRelationships = new HashSet<>(llmRelationships);
            onlyLLMRelationships.removeAll(manualRelationships);

            // Calculate metrics for relationships
            Metrics relationshipMetrics = calculateMetrics(commonRelationships.size(), onlyManualRelationships.size(), onlyLLMRelationships.size());

            results.add(new StoryComparisonResult(story, commonClasses, onlyManualClasses, onlyLLMClasses,
                    commonRelationships, onlyManualRelationships, onlyLLMRelationships, classMetrics, relationshipMetrics));
        }
        
        // Sort by story name for consistent display
        results.sort(Comparator.comparing(StoryComparisonResult::getStoryName));

        return new ComparisonResponse(results);
    }

    /**
     * Calculate Precision, Recall, and F1 Score
     * Precision = TP / (TP + FP) = common / llmTotal
     * Recall = TP / (TP + FN) = common / manualTotal
     * F1 = 2 * (Precision * Recall) / (Precision + Recall)
     */
    private Metrics calculateMetrics(int common, int onlyManual, int onlyLLM) {
        int manualTotal = common + onlyManual;
        int llmTotal = common + onlyLLM;
        
        double precision = llmTotal > 0 ? (double) common / llmTotal : 0.0;
        double recall = manualTotal > 0 ? (double) common / manualTotal : 0.0;
        double f1Score = (precision + recall) > 0 ? 2 * (precision * recall) / (precision + recall) : 0.0;
        
        return new Metrics(precision, recall, f1Score);
    }
}
