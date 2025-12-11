package com.example.classcompare.controller;

import com.example.classcompare.model.ComparisonResponse;
import com.example.classcompare.service.ComparisonService;
import com.example.classcompare.util.JsonReader;
import com.example.classcompare.service.ExcelExportService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FileUploadController {

    private final JsonReader jsonReader;
    private final ComparisonService comparisonService;
    private final ExcelExportService excelExportService;

    public FileUploadController(JsonReader jsonReader, ComparisonService comparisonService,
            ExcelExportService excelExportService) {
        this.jsonReader = jsonReader;
        this.comparisonService = comparisonService;
        this.excelExportService = excelExportService;
    }

    // Stateful storage for the session (Note: Not production ready for multiple
    // users, but fits the requirement)
    private Map<String, Map<String, Object>> manualData;
    private Map<String, Map<String, Object>> llmData;
    private ComparisonResponse lastComparisonResult;

    @PostMapping("/upload")
    public ComparisonResponse upload(
            @RequestParam("manual") MultipartFile manualFile,
            @RequestParam("llm") MultipartFile llmFile) throws Exception {

        manualData = jsonReader.readJsonData(manualFile.getInputStream());
        llmData = jsonReader.readJsonData(llmFile.getInputStream());

        lastComparisonResult = comparisonService.compare(manualData, llmData);
        return lastComparisonResult;
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel() throws Exception {
        if (lastComparisonResult == null) {
            return ResponseEntity.badRequest().build();
        }

        byte[] excelBytes = excelExportService.generateExcel(lastComparisonResult);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "comparison_results.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

    @PostMapping("/export/modified")
    public ResponseEntity<byte[]> exportModifiedExcel(@RequestBody ComparisonResponse modifiedResponse)
            throws Exception {
        byte[] excelBytes = excelExportService.generateExcel(modifiedResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "modified_comparison_results.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }
}
