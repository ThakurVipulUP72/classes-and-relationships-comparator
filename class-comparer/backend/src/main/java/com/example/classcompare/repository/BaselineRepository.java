package com.example.classcompare.repository;

import com.example.classcompare.entity.BaselineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaselineRepository extends JpaRepository<BaselineEntity, Long> {
    
    Optional<BaselineEntity> findByUserId(String userId);
    
    Optional<BaselineEntity> findByComparisonId(Long comparisonId);
}
