package com.example.classcompare.controller;

import com.example.classcompare.entity.ApiKeyEntity;
import com.example.classcompare.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/keys")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    // TODO: Replace with proper authentication/authorization
    // Currently using default user ID for simplicity - NOT PRODUCTION READY
    @PostMapping
    public ResponseEntity<Map<String, Object>> addApiKey(@RequestBody Map<String, String> request) {
        String provider = request.get("provider");
        String apiKey = request.get("apiKey");
        String userId = request.getOrDefault("userId", "default-user");
        String expiresAtStr = request.get("expiresAt");
        
        LocalDateTime expiresAt = expiresAtStr != null ? LocalDateTime.parse(expiresAtStr) : null;
        
        ApiKeyEntity entity = apiKeyService.storeApiKey(provider, apiKey, userId, expiresAt);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("provider", entity.getProvider());
        response.put("createdAt", entity.getCreatedAt());
        
        return ResponseEntity.ok(response);
    }

    // TODO: Replace with proper authentication/authorization
    // Currently using default user ID for simplicity - NOT PRODUCTION READY
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listApiKeys(
            @RequestParam(defaultValue = "default-user") String userId) {
        
        List<ApiKeyEntity> keys = apiKeyService.listApiKeys(userId);
        
        // Return without exposing the actual keys
        List<Map<String, Object>> response = keys.stream()
                .map(key -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", key.getId());
                    item.put("provider", key.getProvider());
                    item.put("createdAt", key.getCreatedAt());
                    item.put("expiresAt", key.getExpiresAt());
                    item.put("isActive", key.getIsActive());
                    return item;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    // TODO: Replace with proper authentication/authorization
    // Currently using default user ID for simplicity - NOT PRODUCTION READY
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteApiKey(
            @PathVariable Long id,
            @RequestParam(defaultValue = "default-user") String userId) {
        
        apiKeyService.deleteApiKey(id, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "API key deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}
