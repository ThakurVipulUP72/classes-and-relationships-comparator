package com.example.classcompare.model;

import java.util.Set;

public class StoryComparisonResult {
    private String storyName;
    private Set<String> common;
    private Set<String> onlyManual;
    private Set<String> onlyLLM;
    
    // Relationship comparison fields
    private Set<Relationship> commonRelationships;
    private Set<Relationship> onlyManualRelationships;
    private Set<Relationship> onlyLLMRelationships;
    
    // Metrics for classes and relationships
    private Metrics classMetrics;
    private Metrics relationshipMetrics;

    public StoryComparisonResult() {}

    public StoryComparisonResult(String storyName, Set<String> common, Set<String> onlyManual, Set<String> onlyLLM,
                                  Set<Relationship> commonRelationships, Set<Relationship> onlyManualRelationships, 
                                  Set<Relationship> onlyLLMRelationships) {
        this.storyName = storyName;
        this.common = common;
        this.onlyManual = onlyManual;
        this.onlyLLM = onlyLLM;
        this.commonRelationships = commonRelationships;
        this.onlyManualRelationships = onlyManualRelationships;
        this.onlyLLMRelationships = onlyLLMRelationships;
    }

    public StoryComparisonResult(String storyName, Set<String> common, Set<String> onlyManual, Set<String> onlyLLM,
                                  Set<Relationship> commonRelationships, Set<Relationship> onlyManualRelationships, 
                                  Set<Relationship> onlyLLMRelationships, Metrics classMetrics, Metrics relationshipMetrics) {
        this.storyName = storyName;
        this.common = common;
        this.onlyManual = onlyManual;
        this.onlyLLM = onlyLLM;
        this.commonRelationships = commonRelationships;
        this.onlyManualRelationships = onlyManualRelationships;
        this.onlyLLMRelationships = onlyLLMRelationships;
        this.classMetrics = classMetrics;
        this.relationshipMetrics = relationshipMetrics;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public Set<String> getCommon() {
        return common;
    }

    public void setCommon(Set<String> common) {
        this.common = common;
    }

    public Set<String> getOnlyManual() {
        return onlyManual;
    }

    public void setOnlyManual(Set<String> onlyManual) {
        this.onlyManual = onlyManual;
    }

    public Set<String> getOnlyLLM() {
        return onlyLLM;
    }

    public void setOnlyLLM(Set<String> onlyLLM) {
        this.onlyLLM = onlyLLM;
    }

    public Set<Relationship> getCommonRelationships() {
        return commonRelationships;
    }

    public void setCommonRelationships(Set<Relationship> commonRelationships) {
        this.commonRelationships = commonRelationships;
    }

    public Set<Relationship> getOnlyManualRelationships() {
        return onlyManualRelationships;
    }

    public void setOnlyManualRelationships(Set<Relationship> onlyManualRelationships) {
        this.onlyManualRelationships = onlyManualRelationships;
    }

    public Set<Relationship> getOnlyLLMRelationships() {
        return onlyLLMRelationships;
    }

    public void setOnlyLLMRelationships(Set<Relationship> onlyLLMRelationships) {
        this.onlyLLMRelationships = onlyLLMRelationships;
    }

    public Metrics getClassMetrics() {
        return classMetrics;
    }

    public void setClassMetrics(Metrics classMetrics) {
        this.classMetrics = classMetrics;
    }

    public Metrics getRelationshipMetrics() {
        return relationshipMetrics;
    }

    public void setRelationshipMetrics(Metrics relationshipMetrics) {
        this.relationshipMetrics = relationshipMetrics;
    }
}
