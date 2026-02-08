package com.example.classcompare.controller;

import com.example.classcompare.entity.ComparisonHistoryEntity;
import com.example.classcompare.repository.ComparisonHistoryRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "http://localhost:3000")
public class MetricsController {

    private final ComparisonHistoryRepository historyRepository;

    public MetricsController(ComparisonHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @GetMapping("/history")
    public ResponseEntity<List<ComparisonHistoryEntity>> getHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(historyRepository.findByTimestampBetween(startDate, endDate));
        } else {
            return ResponseEntity.ok(historyRepository.findAll());
        }
    }

    @GetMapping("/trend")
    public ResponseEntity<Map<String, Object>> getTrend(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(defaultValue = "daily") String granularity) {
        
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        
        List<ComparisonHistoryEntity> history = historyRepository.findByTimestampBetween(startDate, endDate);
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> trends = new ArrayList<>();
        
        // Group by date based on granularity
        Map<String, List<ComparisonHistoryEntity>> grouped = groupByGranularity(history, granularity);
        
        for (Map.Entry<String, List<ComparisonHistoryEntity>> entry : grouped.entrySet()) {
            Map<String, Object> point = new HashMap<>();
            point.put("date", entry.getKey());
            
            List<ComparisonHistoryEntity> items = entry.getValue();
            double avgPrecision = items.stream().mapToDouble(ComparisonHistoryEntity::getPrecision).average().orElse(0.0);
            double avgRecall = items.stream().mapToDouble(ComparisonHistoryEntity::getRecall).average().orElse(0.0);
            double avgF1 = items.stream().mapToDouble(ComparisonHistoryEntity::getF1Score).average().orElse(0.0);
            
            point.put("avgPrecision", avgPrecision);
            point.put("avgRecall", avgRecall);
            point.put("avgF1Score", avgF1);
            point.put("count", items.size());
            
            trends.add(point);
        }
        
        result.put("trends", trends);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/heatmap")
    public ResponseEntity<Map<String, Object>> getHeatmap() {
        List<ComparisonHistoryEntity> history = historyRepository.findAll();
        
        Map<String, Object> result = new HashMap<>();
        List<List<Double>> matrix = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        // Group by dataset
        Map<String, List<ComparisonHistoryEntity>> byDataset = new HashMap<>();
        for (ComparisonHistoryEntity item : history) {
            String dataset = item.getDatasetName() != null ? item.getDatasetName() : "Unknown";
            byDataset.computeIfAbsent(dataset, k -> new ArrayList<>()).add(item);
        }
        
        // Create similarity matrix
        for (Map.Entry<String, List<ComparisonHistoryEntity>> entry : byDataset.entrySet()) {
            labels.add(entry.getKey());
            List<Double> row = new ArrayList<>();
            
            // Calculate average F1 score for this dataset
            double avgF1 = entry.getValue().stream()
                    .mapToDouble(ComparisonHistoryEntity::getF1Score)
                    .average()
                    .orElse(0.0);
            
            // For now, create a simple row with the avg F1 score
            for (int i = 0; i < byDataset.size(); i++) {
                row.add(avgF1);
            }
            matrix.add(row);
        }
        
        result.put("matrix", matrix);
        result.put("labels", labels);
        
        return ResponseEntity.ok(result);
    }

    private Map<String, List<ComparisonHistoryEntity>> groupByGranularity(
            List<ComparisonHistoryEntity> history, String granularity) {
        
        Map<String, List<ComparisonHistoryEntity>> grouped = new LinkedHashMap<>();
        
        for (ComparisonHistoryEntity item : history) {
            String key;
            LocalDateTime timestamp = item.getTimestamp();
            
            switch (granularity.toLowerCase()) {
                case "monthly":
                    key = String.format("%04d-%02d", timestamp.getYear(), timestamp.getMonthValue());
                    break;
                case "weekly":
                    // Simplified - use ISO week
                    key = String.format("%04d-W%02d", timestamp.getYear(), 
                            (timestamp.getDayOfYear() / 7) + 1);
                    break;
                case "daily":
                default:
                    key = String.format("%04d-%02d-%02d", 
                            timestamp.getYear(), 
                            timestamp.getMonthValue(), 
                            timestamp.getDayOfMonth());
                    break;
            }
            
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        
        return grouped;
    }
}
