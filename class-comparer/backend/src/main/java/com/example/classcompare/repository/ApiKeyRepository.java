package com.example.classcompare.repository;

import com.example.classcompare.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
    
    List<ApiKeyEntity> findByUserIdAndIsActive(String userId, Boolean isActive);
    
    Optional<ApiKeyEntity> findByUserIdAndProviderAndIsActive(String userId, String provider, Boolean isActive);
    
    List<ApiKeyEntity> findByProvider(String provider);
}
