package com.example.classcompare.repository;

import com.example.classcompare.entity.ComparisonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComparisonRepository extends JpaRepository<ComparisonEntity, Long> {
    
    List<ComparisonEntity> findByUserIdOrderByTimestampDesc(String userId);
    
    List<ComparisonEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    List<ComparisonEntity> findByDatasetName(String datasetName);
}
