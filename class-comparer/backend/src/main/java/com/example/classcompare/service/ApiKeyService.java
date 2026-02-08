package com.example.classcompare.service;

import com.example.classcompare.entity.ApiKeyEntity;
import com.example.classcompare.repository.ApiKeyRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Stores an API key securely by hashing it
     */
    public ApiKeyEntity storeApiKey(String provider, String apiKey, String userId, LocalDateTime expiresAt) {
        // Hash the API key
        String keyHash = passwordEncoder.encode(apiKey);
        
        // Check if key already exists for this provider and user
        Optional<ApiKeyEntity> existing = apiKeyRepository.findByUserIdAndProviderAndIsActive(userId, provider, true);
        if (existing.isPresent()) {
            // Update existing key
            ApiKeyEntity entity = existing.get();
            entity.setKeyHash(keyHash);
            entity.setExpiresAt(expiresAt);
            entity.setCreatedAt(LocalDateTime.now());
            return apiKeyRepository.save(entity);
        }
        
        // Create new key entity
        ApiKeyEntity entity = new ApiKeyEntity();
        entity.setProvider(provider.toUpperCase());
        entity.setKeyHash(keyHash);
        entity.setUserId(userId);
        entity.setExpiresAt(expiresAt);
        entity.setIsActive(true);
        
        return apiKeyRepository.save(entity);
    }

    /**
     * Validates an API key against the stored hash
     */
    public boolean validateApiKey(String provider, String apiKey, String userId) {
        Optional<ApiKeyEntity> entity = apiKeyRepository.findByUserIdAndProviderAndIsActive(userId, provider, true);
        
        if (entity.isEmpty()) {
            return false;
        }
        
        ApiKeyEntity keyEntity = entity.get();
        
        // Check if expired
        if (keyEntity.getExpiresAt() != null && keyEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        
        // Validate against hash
        return passwordEncoder.matches(apiKey, keyEntity.getKeyHash());
    }

    /**
     * Lists all active API key providers for a user (without exposing the keys)
     */
    public List<ApiKeyEntity> listApiKeys(String userId) {
        return apiKeyRepository.findByUserIdAndIsActive(userId, true);
    }

    /**
     * Deactivates an API key
     */
    public void deactivateApiKey(Long keyId, String userId) {
        Optional<ApiKeyEntity> entity = apiKeyRepository.findById(keyId);
        
        if (entity.isPresent() && entity.get().getUserId().equals(userId)) {
            ApiKeyEntity keyEntity = entity.get();
            keyEntity.setIsActive(false);
            apiKeyRepository.save(keyEntity);
        }
    }

    /**
     * Deletes an API key
     */
    public void deleteApiKey(Long keyId, String userId) {
        Optional<ApiKeyEntity> entity = apiKeyRepository.findById(keyId);
        
        if (entity.isPresent() && entity.get().getUserId().equals(userId)) {
            apiKeyRepository.delete(entity.get());
        }
    }

    /**
     * Gets the active API key entity for a provider (returns null to avoid exposing the actual key)
     */
    public Optional<String> getProviderForUser(String userId, String provider) {
        Optional<ApiKeyEntity> entity = apiKeyRepository.findByUserIdAndProviderAndIsActive(userId, provider, true);
        return entity.map(ApiKeyEntity::getProvider);
    }
}
