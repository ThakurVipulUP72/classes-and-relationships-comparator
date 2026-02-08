package com.example.classcompare.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comparison")
public class ComparisonEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "precision_score")
    private Double precision;
    
    @Column(name = "recall_score")
    private Double recall;
    
    @Column(name = "f1_score")
    private Double f1Score;
    
    @OneToMany(mappedBy = "comparison", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComparisonItemEntity> items = new ArrayList<>();
    
    private String userId;
    private String datasetName;
    
    public ComparisonEntity() {
        this.timestamp = LocalDateTime.now();
        this.status = "COMPLETED";
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
    
    public List<ComparisonItemEntity> getItems() {
        return items;
    }
    
    public void setItems(List<ComparisonItemEntity> items) {
        this.items = items;
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
}
