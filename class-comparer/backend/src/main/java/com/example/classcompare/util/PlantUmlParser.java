package com.example.classcompare.util;

import com.example.classcompare.model.Relationship;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PlantUmlParser {

    // Patterns for parsing PlantUML class diagram syntax
    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([A-Za-z0-9_]+)");
    private static final Pattern RELATIONSHIP_PATTERN = Pattern.compile("([A-Za-z0-9_]+)\\s+(-->|<--|\\*--|--\\*|o--|--o|<\\|--|--|\\.\\.>|<\\.\\.)\\.+([A-Za-z0-9_]+)");
    private static final Pattern RELATIONSHIP_WITH_LABEL_PATTERN = Pattern.compile("([A-Za-z0-9_]+)\\s+(-->|<--|\\*--|--\\*|o--|--o|<\\|--|--|\\.\\.>|<\\.\\.)\\.+\"([^\"]+)\"\\s+([A-Za-z0-9_]+)");

    /**
     * Parses PlantUML content and extracts classes and relationships
     * Groups everything into a single "PlantUML Diagram" story
     */
    public Map<String, Map<String, Object>> parsePlantUml(InputStream is) throws Exception {
        Map<String, Map<String, Object>> result = new HashMap<>();
        Map<String, Object> storyData = new HashMap<>();
        Set<String> classes = new HashSet<>();
        Set<Relationship> relationships = new HashSet<>();

        storyData.put("classes", classes);
        storyData.put("relationships", relationships);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // Skip empty lines and comments
            if (line.isEmpty() || line.startsWith("'") || line.startsWith("@startuml") || line.startsWith("@enduml")) {
                continue;
            }

            // Parse class declarations
            Matcher classMatcher = CLASS_PATTERN.matcher(line);
            if (classMatcher.find()) {
                classes.add(classMatcher.group(1));
                continue;
            }

            // Parse relationships with labels
            Matcher relWithLabelMatcher = RELATIONSHIP_WITH_LABEL_PATTERN.matcher(line);
            if (relWithLabelMatcher.find()) {
                String source = relWithLabelMatcher.group(1);
                String relationType = convertPlantUmlRelation(relWithLabelMatcher.group(2));
                String label = relWithLabelMatcher.group(3);
                String target = relWithLabelMatcher.group(4);
                
                // Ensure classes exist
                classes.add(source);
                classes.add(target);
                relationships.add(new Relationship(source, relationType + " (" + label + ")", target));
                continue;
            }

            // Parse relationships without labels
            Matcher relMatcher = RELATIONSHIP_PATTERN.matcher(line);
            if (relMatcher.find()) {
                String source = relMatcher.group(1);
                String relationType = convertPlantUmlRelation(relMatcher.group(2));
                String target = relMatcher.group(3);
                
                // Ensure classes exist
                classes.add(source);
                classes.add(target);
                relationships.add(new Relationship(source, relationType, target));
            }
        }

        result.put("PlantUML_Diagram", storyData);
        reader.close();
        return result;
    }

    /**
     * Converts PlantUML relationship notation to readable format
     */
    private String convertPlantUmlRelation(String plantUmlRelation) {
        return switch (plantUmlRelation) {
            case "-->" -> "Association";
            case "<--" -> "Association";
            case "*--" -> "Composition";
            case "--*" -> "Composition";
            case "o--" -> "Aggregation";
            case "--o" -> "Aggregation";
            case "<|--" -> "Inheritance";
            case "..|>" -> "Realization";
            case "<.." -> "Dependency";
            default -> "Association";
        };
    }
}
