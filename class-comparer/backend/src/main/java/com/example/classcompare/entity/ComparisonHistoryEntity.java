package com.example.classcompare.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comparison_history")
public class ComparisonHistoryEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "precision_score", nullable = false)
    private Double precision;
    
    @Column(name = "recall_score", nullable = false)
    private Double recall;
    
    @Column(name = "f1_score", nullable = false)
    private Double f1Score;
    
    private String userId;
    private String datasetName;
    
    @Column(name = "comparison_id")
    private Long comparisonId;
    
    public ComparisonHistoryEntity() {
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Double getPrecision() {
        return precision;
    }
    
    public void setPrecision(Double precision) {
        this.precision = precision;
    }
    
    public Double getRecall() {
        return recall;
    }
    
    public void setRecall(Double recall) {
        this.recall = recall;
    }
    
    public Double getF1Score() {
        return f1Score;
    }
    
    public void setF1Score(Double f1Score) {
        this.f1Score = f1Score;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getDatasetName() {
        return datasetName;
    }
    
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }
    
    public Long getComparisonId() {
        return comparisonId;
    }
    
    public void setComparisonId(Long comparisonId) {
        this.comparisonId = comparisonId;
    }
}
