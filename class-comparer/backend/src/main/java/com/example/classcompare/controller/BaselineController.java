package com.example.classcompare.controller;

import com.example.classcompare.entity.BaselineEntity;
import com.example.classcompare.service.BaselineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/baseline")
@CrossOrigin(origins = "http://localhost:3000")
public class BaselineController {

    private final BaselineService baselineService;

    public BaselineController(BaselineService baselineService) {
        this.baselineService = baselineService;
    }

    @PostMapping
    public ResponseEntity<BaselineEntity> setBaseline(
            @RequestParam Long comparisonId,
            @RequestParam(defaultValue = "default-user") String userId) {
        
        BaselineEntity baseline = baselineService.setBaseline(comparisonId, userId);
        return ResponseEntity.ok(baseline);
    }

    @GetMapping
    public ResponseEntity<BaselineEntity> getBaseline(
            @RequestParam(defaultValue = "default-user") String userId) {
        
        Optional<BaselineEntity> baseline = baselineService.getBaseline(userId);
        return baseline.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/compare/{comparisonId}")
    public ResponseEntity<Map<String, Object>> compareToBaseline(
            @PathVariable Long comparisonId,
            @RequestParam(defaultValue = "default-user") String userId) {
        
        try {
            Map<String, Object> comparison = baselineService.compareToBaseline(comparisonId, userId);
            return ResponseEntity.ok(comparison);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBaseline(
            @RequestParam(defaultValue = "default-user") String userId) {
        
        baselineService.deleteBaseline(userId);
        return ResponseEntity.ok().build();
    }
}
