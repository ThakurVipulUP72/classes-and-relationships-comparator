package com.example.classcompare.controller;

import com.example.classcompare.model.ComparisonResponse;
import com.example.classcompare.service.PdfExportService;
import com.example.classcompare.util.PlantUmlGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visualize")
@CrossOrigin(origins = "http://localhost:3000")
public class VisualizationController {

    private final PdfExportService pdfExportService;
    private final PlantUmlGenerator plantUmlGenerator;

    public VisualizationController(PdfExportService pdfExportService, PlantUmlGenerator plantUmlGenerator) {
        this.pdfExportService = pdfExportService;
        this.plantUmlGenerator = plantUmlGenerator;
    }

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> exportPdf(@RequestBody ComparisonResponse comparison) throws Exception {
        byte[] pdfBytes = pdfExportService.generateComparisonReport(comparison);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "comparison_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @PostMapping("/plantuml")
    public ResponseEntity<String> exportPlantUml(@RequestBody ComparisonResponse comparison) {
        String plantUml = plantUmlGenerator.generatePlantUml(comparison);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "class_diagram.puml");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(plantUml);
    }

    @PostMapping("/diagram")
    public ResponseEntity<Object> getDiagramData(@RequestBody ComparisonResponse comparison) {
        // Returns data suitable for vis.js or D3.js visualization
        // Format: { nodes: [], edges: [] }
        return ResponseEntity.ok(convertToGraphData(comparison));
    }

    private Object convertToGraphData(ComparisonResponse comparison) {
        // Simplified implementation - returns structure for frontend visualization
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        java.util.List<java.util.Map<String, Object>> nodes = new java.util.ArrayList<>();
        java.util.List<java.util.Map<String, Object>> edges = new java.util.ArrayList<>();

        java.util.Set<String> allClasses = new java.util.HashSet<>();
        
        for (var story : comparison.getStories()) {
            // Collect all classes
            for (String className : story.getCommon()) {
                if (allClasses.add(className)) {
                    java.util.Map<String, Object> node = new java.util.HashMap<>();
                    node.put("id", className);
                    node.put("label", className);
                    node.put("color", "#90EE90"); // Green for matched
                    node.put("matchType", "COMMON");
                    nodes.add(node);
                }
            }
            
            for (String className : story.getOnlyManual()) {
                if (allClasses.add(className)) {
                    java.util.Map<String, Object> node = new java.util.HashMap<>();
                    node.put("id", className);
                    node.put("label", className);
                    node.put("color", "#FFB6C1"); // Red for manual only
                    node.put("matchType", "ONLY_MANUAL");
                    nodes.add(node);
                }
            }
            
            for (String className : story.getOnlyLLM()) {
                if (allClasses.add(className)) {
                    java.util.Map<String, Object> node = new java.util.HashMap<>();
                    node.put("id", className);
                    node.put("label", className);
                    node.put("color", "#ADD8E6"); // Blue for LLM only
                    node.put("matchType", "ONLY_LLM");
                    nodes.add(node);
                }
            }

            // Collect all relationships
            int edgeId = 0;
            for (var rel : story.getCommonRelationships()) {
                java.util.Map<String, Object> edge = new java.util.HashMap<>();
                edge.put("id", "edge" + (edgeId++));
                edge.put("from", rel.getSource());
                edge.put("to", rel.getTarget());
                edge.put("label", rel.getRelation());
                edge.put("color", "#90EE90");
                edges.add(edge);
            }
            
            for (var rel : story.getOnlyManualRelationships()) {
                java.util.Map<String, Object> edge = new java.util.HashMap<>();
                edge.put("id", "edge" + (edgeId++));
                edge.put("from", rel.getSource());
                edge.put("to", rel.getTarget());
                edge.put("label", rel.getRelation());
                edge.put("color", "#FFB6C1");
                edges.add(edge);
            }
            
            for (var rel : story.getOnlyLLMRelationships()) {
                java.util.Map<String, Object> edge = new java.util.HashMap<>();
                edge.put("id", "edge" + (edgeId++));
                edge.put("from", rel.getSource());
                edge.put("to", rel.getTarget());
                edge.put("label", rel.getRelation());
                edge.put("color", "#ADD8E6");
                edges.add(edge);
            }
        }

        result.put("nodes", nodes);
        result.put("edges", edges);
        return result;
    }
}
