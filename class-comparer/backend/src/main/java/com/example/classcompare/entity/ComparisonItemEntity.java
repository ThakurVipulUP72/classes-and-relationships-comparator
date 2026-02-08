package com.example.classcompare.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comparison_item")
public class ComparisonItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comparison_id", nullable = false)
    private ComparisonEntity comparison;
    
    @Column(nullable = false)
    private String story;
    
    @Column(name = "class_name")
    private String className;
    
    @Column(name = "relationship_text")
    private String relationshipText;
    
    @Column(name = "match_type", nullable = false)
    private String matchType; // COMMON, ONLY_MANUAL, ONLY_LLM
    
    @Column(name = "similarity_score")
    private Double similarityScore;
    
    @Column(name = "confidence_score")
    private Double confidenceScore;
    
    @Column(name = "item_type", nullable = false)
    private String itemType; // CLASS, RELATIONSHIP
    
    public ComparisonItemEntity() {
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ComparisonEntity getComparison() {
        return comparison;
    }
    
    public void setComparison(ComparisonEntity comparison) {
        this.comparison = comparison;
    }
    
    public String getStory() {
        return story;
    }
    
    public void setStory(String story) {
        this.story = story;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getRelationshipText() {
        return relationshipText;
    }
    
    public void setRelationshipText(String relationshipText) {
        this.relationshipText = relationshipText;
    }
    
    public String getMatchType() {
        return matchType;
    }
    
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }
    
    public Double getSimilarityScore() {
        return similarityScore;
    }
    
    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }
    
    public Double getConfidenceScore() {
        return confidenceScore;
    }
    
    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
    
    public String getItemType() {
        return itemType;
    }
    
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
