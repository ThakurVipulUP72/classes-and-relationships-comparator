package com.example.classcompare.repository;

import com.example.classcompare.entity.ComparisonHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComparisonHistoryRepository extends JpaRepository<ComparisonHistoryEntity, Long> {
    
    List<ComparisonHistoryEntity> findByUserIdOrderByTimestampDesc(String userId);
    
    List<ComparisonHistoryEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    List<ComparisonHistoryEntity> findByDatasetName(String datasetName);
}
