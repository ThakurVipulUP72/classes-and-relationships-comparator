package com.example.classcompare.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "baseline")
public class BaselineEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "comparison_id", nullable = false, unique = true)
    private Long comparisonId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "precision_score", nullable = false)
    private Double precision;
    
    @Column(name = "recall_score", nullable = false)
    private Double recall;
    
    @Column(name = "f1_score", nullable = false)
    private Double f1Score;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    private String userId;
    
    public BaselineEntity() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getComparisonId() {
        return comparisonId;
    }
    
    public void setComparisonId(Long comparisonId) {
        this.comparisonId = comparisonId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
