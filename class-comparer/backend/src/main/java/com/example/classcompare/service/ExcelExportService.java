package com.example.classcompare.service;

import com.example.classcompare.model.ComparisonResponse;
import com.example.classcompare.model.Relationship;
import com.example.classcompare.model.StoryComparisonResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Set;

@Service
public class ExcelExportService {

    public byte[] generateExcel(ComparisonResponse comparisonResponse) throws Exception {
        Workbook workbook = new XSSFWorkbook();

        // Create cell styles for color coding
        CellStyle greenStyle = createCellStyle(workbook, IndexedColors.LIGHT_GREEN);
        CellStyle redStyle = createCellStyle(workbook, IndexedColors.LIGHT_ORANGE);
        CellStyle yellowStyle = createCellStyle(workbook, IndexedColors.LIGHT_YELLOW);
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle defaultStyle = createCellStyle(workbook, IndexedColors.WHITE);

        // Create sheets
        Sheet classesSheet = workbook.createSheet("Classes Comparison");
        Sheet relationshipsSheet = workbook.createSheet("Relationships Comparison");

        // Populate classes
        populateAllClassesSheet(classesSheet, comparisonResponse, greenStyle, redStyle, yellowStyle, defaultStyle,
                headerStyle);

        // Populate relationships
        populateAllRelationshipsSheet(relationshipsSheet, comparisonResponse, greenStyle, redStyle, yellowStyle,
                defaultStyle, headerStyle);

        autoSizeColumns(classesSheet, 4);
        autoSizeColumns(relationshipsSheet, 4);

        // Write to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private void populateAllClassesSheet(Sheet sheet, ComparisonResponse response,
            CellStyle greenStyle, CellStyle redStyle, CellStyle yellowStyle,
            CellStyle defaultStyle, CellStyle headerStyle) {
        // Create header
        Row headerRow = sheet.createRow(0);
        String[] headers = new String[] { "Story", "Class", "Source", "Notes" };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (StoryComparisonResult story : response.getStories()) {
            // Add matched classes
            for (String className : story.getCommon()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, story.getStoryName(), defaultStyle);
                createCell(row, 1, className, defaultStyle);
                createCell(row, 2, "Both", greenStyle);
                createCell(row, 3, "Found in both Manual and LLM", defaultStyle);
            }

            // Add unmatched classes from manual
            for (String className : story.getOnlyManual()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, story.getStoryName(), defaultStyle);
                createCell(row, 1, className, defaultStyle);
                createCell(row, 2, "Manual Only", yellowStyle);
                createCell(row, 3, "Not found in LLM output", defaultStyle);
            }

            // Add unmatched classes from LLM
            for (String className : story.getOnlyLLM()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, story.getStoryName(), defaultStyle);
                createCell(row, 1, className, defaultStyle);
                createCell(row, 2, "LLM Only", redStyle);
                createCell(row, 3, "Not found in manual extraction", defaultStyle);
            }
        }
    }

    private void populateAllRelationshipsSheet(Sheet sheet, ComparisonResponse response,
            CellStyle greenStyle, CellStyle redStyle, CellStyle yellowStyle,
            CellStyle defaultStyle, CellStyle headerStyle) {
        // Create header
        Row headerRow = sheet.createRow(0);
        String[] headers = new String[] { "Story", "Relationship", "Source", "Notes" };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (StoryComparisonResult story : response.getStories()) {
            // Add matched relationships
            for (Relationship rel : story.getCommonRelationships()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, story.getStoryName(), defaultStyle);
                createCell(row, 1, rel.toString(), defaultStyle);
                createCell(row, 2, "Both", greenStyle);
                createCell(row, 3, "Found in both Manual and LLM", defaultStyle);
            }

            // Add unmatched relationships from manual
            for (Relationship rel : story.getOnlyManualRelationships()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, story.getStoryName(), defaultStyle);
                createCell(row, 1, rel.toString(), defaultStyle);
                createCell(row, 2, "Manual Only", yellowStyle);
                createCell(row, 3, "Not found in LLM output", defaultStyle);
            }

            // Add unmatched relationships from LLM
            for (Relationship rel : story.getOnlyLLMRelationships()) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, story.getStoryName(), defaultStyle);
                createCell(row, 1, rel.toString(), defaultStyle);
                createCell(row, 2, "LLM Only", redStyle);
                createCell(row, 3, "Not found in manual extraction", defaultStyle);
            }
        }
    }

    private CellStyle createCellStyle(Workbook workbook, IndexedColors color) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
