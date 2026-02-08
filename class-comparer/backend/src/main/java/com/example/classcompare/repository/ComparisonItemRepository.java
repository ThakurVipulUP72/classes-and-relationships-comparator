package com.example.classcompare.repository;

import com.example.classcompare.entity.ComparisonItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComparisonItemRepository extends JpaRepository<ComparisonItemEntity, Long> {
    
    List<ComparisonItemEntity> findByComparisonId(Long comparisonId);
    
    List<ComparisonItemEntity> findByComparisonIdAndItemType(Long comparisonId, String itemType);
    
    List<ComparisonItemEntity> findByComparisonIdAndMatchType(Long comparisonId, String matchType);
}
