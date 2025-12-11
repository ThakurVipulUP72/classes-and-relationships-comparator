package com.example.classcompare.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
public class ExcelReader {

    public Map<String, Set<String>> readClasses(InputStream is) throws Exception {
        Map<String, Set<String>> result = new HashMap<>();

        Workbook workbook = new XSSFWorkbook(is);
        // Assuming data is in the first sheet
        Sheet sheet = workbook.getSheetAt(0);

        // Iterate rows, skipping header if necessary (simple heuristic: check if first row looks like header)
        boolean isFirstRow = true;
        
        for (Row row : sheet) {
            if (isFirstRow) {
                isFirstRow = false;
                // Optional: Check headers "Story" and "Class"
                // For now, assume Col 0 = Story, Col 1 = Class
                continue; 
            }

            Cell storyCell = row.getCell(0);
            Cell classCell = row.getCell(1);

            if (storyCell != null && classCell != null) {
                String story = getCellValueAsString(storyCell).trim();
                String className = getCellValueAsString(classCell).trim();

                if (!story.isEmpty() && !className.isEmpty()) {
                    result.computeIfAbsent(story, k -> new HashSet<>()).add(className);
                }
            }
        }

        workbook.close();
        return result;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return cell.toString();
        }
    }
}
