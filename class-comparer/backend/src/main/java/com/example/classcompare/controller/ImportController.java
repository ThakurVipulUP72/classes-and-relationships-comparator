package com.example.classcompare.controller;

import com.example.classcompare.model.ComparisonResponse;
import com.example.classcompare.service.ComparisonService;
import com.example.classcompare.util.CsvReader;
import com.example.classcompare.util.PlantUmlParser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = "http://localhost:3000")
public class ImportController {

    private final CsvReader csvReader;
    private final PlantUmlParser plantUmlParser;
    private final ComparisonService comparisonService;

    public ImportController(CsvReader csvReader, PlantUmlParser plantUmlParser, ComparisonService comparisonService) {
        this.csvReader = csvReader;
        this.plantUmlParser = plantUmlParser;
        this.comparisonService = comparisonService;
    }

    @PostMapping("/csv")
    public ComparisonResponse importCsv(
            @RequestParam("manual") MultipartFile manualFile,
            @RequestParam("llm") MultipartFile llmFile) throws Exception {
        
        Map<String, Map<String, Object>> manualData = csvReader.readCsvData(manualFile.getInputStream());
        Map<String, Map<String, Object>> llmData = csvReader.readCsvData(llmFile.getInputStream());
        
        return comparisonService.compare(manualData, llmData);
    }

    @PostMapping("/plantuml")
    public ComparisonResponse importPlantUml(
            @RequestParam("manual") MultipartFile manualFile,
            @RequestParam("llm") MultipartFile llmFile) throws Exception {
        
        Map<String, Map<String, Object>> manualData = plantUmlParser.parsePlantUml(manualFile.getInputStream());
        Map<String, Map<String, Object>> llmData = plantUmlParser.parsePlantUml(llmFile.getInputStream());
        
        return comparisonService.compare(manualData, llmData);
    }
}
