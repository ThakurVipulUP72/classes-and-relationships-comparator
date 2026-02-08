package com.example.classcompare.service;

import com.example.classcompare.entity.BaselineEntity;
import com.example.classcompare.entity.ComparisonEntity;
import com.example.classcompare.repository.BaselineRepository;
import com.example.classcompare.repository.ComparisonRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BaselineService {

    private final BaselineRepository baselineRepository;
    private final ComparisonRepository comparisonRepository;

    public BaselineService(BaselineRepository baselineRepository, ComparisonRepository comparisonRepository) {
        this.baselineRepository = baselineRepository;
        this.comparisonRepository = comparisonRepository;
    }

    /**
     * Sets a comparison as the baseline for a user
     */
    public BaselineEntity setBaseline(Long comparisonId, String userId) {
        Optional<ComparisonEntity> comparison = comparisonRepository.findById(comparisonId);
        
        if (comparison.isEmpty()) {
            throw new IllegalArgumentException("Comparison not found: " + comparisonId);
        }
        
        ComparisonEntity comp = comparison.get();
        
        // Delete existing baseline for this user
        Optional<BaselineEntity> existing = baselineRepository.findByUserId(userId);
        existing.ifPresent(baselineRepository::delete);
        
        // Create new baseline
        BaselineEntity baseline = new BaselineEntity();
        baseline.setComparisonId(comparisonId);
        baseline.setName(comp.getName());
        baseline.setPrecision(comp.getPrecision());
        baseline.setRecall(comp.getRecall());
        baseline.setF1Score(comp.getF1Score());
        baseline.setUserId(userId);
        
        return baselineRepository.save(baseline);
    }

    /**
     * Gets the current baseline for a user
     */
    public Optional<BaselineEntity> getBaseline(String userId) {
        return baselineRepository.findByUserId(userId);
    }

    /**
     * Compares a comparison against the baseline
     */
    public Map<String, Object> compareToBaseline(Long comparisonId, String userId) {
        Optional<BaselineEntity> baseline = baselineRepository.findByUserId(userId);
        
        if (baseline.isEmpty()) {
            throw new IllegalArgumentException("No baseline set for user: " + userId);
        }
        
        Optional<ComparisonEntity> comparison = comparisonRepository.findById(comparisonId);
        
        if (comparison.isEmpty()) {
            throw new IllegalArgumentException("Comparison not found: " + comparisonId);
        }
        
        BaselineEntity base = baseline.get();
        ComparisonEntity comp = comparison.get();
        
        Map<String, Object> result = new HashMap<>();
        result.put("baseline", base);
        result.put("comparison", comp);
        
        // Calculate deltas
        Map<String, Object> deltas = new HashMap<>();
        deltas.put("precisionDelta", comp.getPrecision() - base.getPrecision());
        deltas.put("recallDelta", comp.getRecall() - base.getRecall());
        deltas.put("f1ScoreDelta", comp.getF1Score() - base.getF1Score());
        
        // Calculate percentage changes
        Map<String, Object> percentages = new HashMap<>();
        percentages.put("precisionChange", calculatePercentageChange(base.getPrecision(), comp.getPrecision()));
        percentages.put("recallChange", calculatePercentageChange(base.getRecall(), comp.getRecall()));
        percentages.put("f1ScoreChange", calculatePercentageChange(base.getF1Score(), comp.getF1Score()));
        
        result.put("deltas", deltas);
        result.put("percentages", percentages);
        
        // Determine if it's an improvement
        result.put("isImprovement", comp.getF1Score() > base.getF1Score());
        
        return result;
    }

    /**
     * Deletes the baseline for a user
     */
    public void deleteBaseline(String userId) {
        Optional<BaselineEntity> baseline = baselineRepository.findByUserId(userId);
        baseline.ifPresent(baselineRepository::delete);
    }

    /**
     * Calculates percentage change, handling edge cases
     * Returns null for undefined cases (old value is zero, new value is non-zero)
     */
    private Double calculatePercentageChange(double oldValue, double newValue) {
        if (oldValue == 0.0) {
            // Cannot calculate percentage change from zero
            // Return null to indicate undefined
            return newValue == 0.0 ? 0.0 : null;
        }
        return ((newValue - oldValue) / oldValue) * 100.0;
    }
}
