package com.example.classcompare.service;

import com.example.classcompare.model.ComparisonResponse;
import com.example.classcompare.model.Relationship;
import com.example.classcompare.model.StoryComparisonResult;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

@Service
public class PdfExportService {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");

    /**
     * Generates a comprehensive PDF report from comparison results
     */
    public byte[] generateComparisonReport(ComparisonResponse comparison) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Title
        Paragraph title = new Paragraph("Class Comparison Report")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(title);

        // Executive Summary
        addExecutiveSummary(document, comparison);

        // Overall Metrics
        addOverallMetrics(document, comparison);

        // Per-Story Details
        addPerStoryDetails(document, comparison);

        document.close();
        return baos.toByteArray();
    }

    private void addExecutiveSummary(Document document, ComparisonResponse comparison) {
        Paragraph header = new Paragraph("Executive Summary")
                .setFontSize(18)
                .setBold()
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(header);

        int totalStories = comparison.getStories().size();
        int totalCommonClasses = 0;
        int totalManualOnlyClasses = 0;
        int totalLlmOnlyClasses = 0;
        int totalCommonRels = 0;
        int totalManualOnlyRels = 0;
        int totalLlmOnlyRels = 0;

        for (StoryComparisonResult story : comparison.getStories()) {
            totalCommonClasses += story.getCommon().size();
            totalManualOnlyClasses += story.getOnlyManual().size();
            totalLlmOnlyClasses += story.getOnlyLLM().size();
            totalCommonRels += story.getCommonRelationships().size();
            totalManualOnlyRels += story.getOnlyManualRelationships().size();
            totalLlmOnlyRels += story.getOnlyLLMRelationships().size();
        }

        Paragraph summary = new Paragraph()
                .add("Total Stories Analyzed: " + totalStories + "\n")
                .add("Total Matched Classes: " + totalCommonClasses + "\n")
                .add("Total Manual-Only Classes: " + totalManualOnlyClasses + "\n")
                .add("Total LLM-Only Classes: " + totalLlmOnlyClasses + "\n")
                .add("Total Matched Relationships: " + totalCommonRels + "\n")
                .add("Total Manual-Only Relationships: " + totalManualOnlyRels + "\n")
                .add("Total LLM-Only Relationships: " + totalLlmOnlyRels + "\n")
                .setMarginBottom(15);
        document.add(summary);
    }

    private void addOverallMetrics(Document document, ComparisonResponse comparison) {
        Paragraph header = new Paragraph("Overall Metrics")
                .setFontSize(18)
                .setBold()
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(header);

        // Calculate overall metrics
        int tpClass = 0, fpClass = 0, fnClass = 0;
        int tpRel = 0, fpRel = 0, fnRel = 0;

        for (StoryComparisonResult story : comparison.getStories()) {
            tpClass += story.getCommon().size();
            fnClass += story.getOnlyManual().size();
            fpClass += story.getOnlyLLM().size();
            tpRel += story.getCommonRelationships().size();
            fnRel += story.getOnlyManualRelationships().size();
            fpRel += story.getOnlyLLMRelationships().size();
        }

        int totalTP = tpClass + tpRel;
        int totalFP = fpClass + fpRel;
        int totalFN = fnClass + fnRel;

        double precision = totalTP + totalFP > 0 ? (double) totalTP / (totalTP + totalFP) : 0.0;
        double recall = totalTP + totalFN > 0 ? (double) totalTP / (totalTP + totalFN) : 0.0;
        double f1 = precision + recall > 0 ? 2 * (precision * recall) / (precision + recall) : 0.0;

        Table metricsTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(60))
                .setMarginBottom(15);

        metricsTable.addCell(createHeaderCell("Metric"));
        metricsTable.addCell(createHeaderCell("Value"));
        metricsTable.addCell(new Cell().add(new Paragraph("Precision")));
        metricsTable.addCell(new Cell().add(new Paragraph(DECIMAL_FORMAT.format(precision * 100) + "%")));
        metricsTable.addCell(new Cell().add(new Paragraph("Recall")));
        metricsTable.addCell(new Cell().add(new Paragraph(DECIMAL_FORMAT.format(recall * 100) + "%")));
        metricsTable.addCell(new Cell().add(new Paragraph("F1 Score")));
        metricsTable.addCell(new Cell().add(new Paragraph(DECIMAL_FORMAT.format(f1 * 100) + "%")));

        document.add(metricsTable);
    }

    private void addPerStoryDetails(Document document, ComparisonResponse comparison) {
        Paragraph header = new Paragraph("Per-Story Details")
                .setFontSize(18)
                .setBold()
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(header);

        for (StoryComparisonResult story : comparison.getStories()) {
            // Story header
            Paragraph storyHeader = new Paragraph(story.getStoryName())
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(15)
                    .setMarginBottom(8);
            document.add(storyHeader);

            // Classes table
            if (!story.getCommon().isEmpty() || !story.getOnlyManual().isEmpty() || !story.getOnlyLLM().isEmpty()) {
                Paragraph classesHeader = new Paragraph("Classes")
                        .setFontSize(12)
                        .setBold()
                        .setMarginTop(8)
                        .setMarginBottom(5);
                document.add(classesHeader);

                Table classesTable = new Table(UnitValue.createPercentArray(new float[]{1, 3}))
                        .setWidth(UnitValue.createPercentValue(80))
                        .setMarginBottom(10);

                classesTable.addCell(createHeaderCell("Type"));
                classesTable.addCell(createHeaderCell("Classes"));

                if (!story.getCommon().isEmpty()) {
                    classesTable.addCell(createColoredCell("Matched", new DeviceRgb(144, 238, 144)));
                    classesTable.addCell(new Cell().add(new Paragraph(String.join(", ", story.getCommon()))));
                }
                if (!story.getOnlyManual().isEmpty()) {
                    classesTable.addCell(createColoredCell("Manual Only", new DeviceRgb(255, 182, 193)));
                    classesTable.addCell(new Cell().add(new Paragraph(String.join(", ", story.getOnlyManual()))));
                }
                if (!story.getOnlyLLM().isEmpty()) {
                    classesTable.addCell(createColoredCell("LLM Only", new DeviceRgb(173, 216, 230)));
                    classesTable.addCell(new Cell().add(new Paragraph(String.join(", ", story.getOnlyLLM()))));
                }

                document.add(classesTable);
            }

            // Relationships table
            if (!story.getCommonRelationships().isEmpty() || 
                !story.getOnlyManualRelationships().isEmpty() || 
                !story.getOnlyLLMRelationships().isEmpty()) {
                
                Paragraph relsHeader = new Paragraph("Relationships")
                        .setFontSize(12)
                        .setBold()
                        .setMarginTop(8)
                        .setMarginBottom(5);
                document.add(relsHeader);

                Table relsTable = new Table(UnitValue.createPercentArray(new float[]{1, 4}))
                        .setWidth(UnitValue.createPercentValue(80))
                        .setMarginBottom(10);

                relsTable.addCell(createHeaderCell("Type"));
                relsTable.addCell(createHeaderCell("Relationships"));

                if (!story.getCommonRelationships().isEmpty()) {
                    relsTable.addCell(createColoredCell("Matched", new DeviceRgb(144, 238, 144)));
                    relsTable.addCell(new Cell().add(new Paragraph(formatRelationships(story.getCommonRelationships()))));
                }
                if (!story.getOnlyManualRelationships().isEmpty()) {
                    relsTable.addCell(createColoredCell("Manual Only", new DeviceRgb(255, 182, 193)));
                    relsTable.addCell(new Cell().add(new Paragraph(formatRelationships(story.getOnlyManualRelationships()))));
                }
                if (!story.getOnlyLLMRelationships().isEmpty()) {
                    relsTable.addCell(createColoredCell("LLM Only", new DeviceRgb(173, 216, 230)));
                    relsTable.addCell(new Cell().add(new Paragraph(formatRelationships(story.getOnlyLLMRelationships()))));
                }

                document.add(relsTable);
            }

            // Story metrics
            Paragraph metricsHeader = new Paragraph("Metrics")
                    .setFontSize(12)
                    .setBold()
                    .setMarginTop(8)
                    .setMarginBottom(5);
            document.add(metricsHeader);

            Table metricsTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                    .setWidth(UnitValue.createPercentValue(80))
                    .setMarginBottom(15);

            metricsTable.addCell(createHeaderCell("Category"));
            metricsTable.addCell(createHeaderCell("Precision"));
            metricsTable.addCell(createHeaderCell("Recall"));
            metricsTable.addCell(createHeaderCell("F1 Score"));

            metricsTable.addCell(new Cell().add(new Paragraph("Classes")));
            metricsTable.addCell(new Cell().add(new Paragraph(DECIMAL_FORMAT.format(story.getClassMetrics().getPrecision() * 100) + "%")));
            metricsTable.addCell(new Cell().add(new Paragraph(DECIMAL_FORMAT.format(story.getClassMetrics().getRecall() * 100) + "%")));
            metricsTable.addCell(new Cell().add(new Paragraph(DECIMAL_FORMAT.format(story.getClassMetrics().getF1Score() * 100) + "%")));

            metricsTable.addCell(new Cell().add(new Paragraph("Relationships")));
            metricsTable.addCell(new Cell().add(new Paragraph(DECIMAL_FORMAT.format(story.getRelationshipMetrics().getPrecision() * 100) + "%")));
            metricsTable.addCell(new Cell().add(new Paragraph(DECIMAL_FORMAT.format(story.getRelationshipMetrics().getRecall() * 100) + "%")));
            metricsTable.addCell(new Cell().add(new Paragraph(DECIMAL_FORMAT.format(story.getRelationshipMetrics().getF1Score() * 100) + "%")));

            document.add(metricsTable);
        }
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(new DeviceRgb(200, 200, 200))
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell createColoredCell(String text, DeviceRgb color) {
        return new Cell()
                .add(new Paragraph(text))
                .setBackgroundColor(color);
    }

    private String formatRelationships(java.util.Set<Relationship> relationships) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Relationship rel : relationships) {
            if (count > 0) sb.append(", ");
            sb.append(rel.getSource())
              .append(" → ")
              .append(rel.getRelation())
              .append(" → ")
              .append(rel.getTarget());
            count++;
        }
        return sb.toString();
    }
}
