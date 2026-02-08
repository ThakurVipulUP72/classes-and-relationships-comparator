package com.example.classcompare.util;

import com.example.classcompare.model.Relationship;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Component
public class CsvReader {

    /**
     * Reads CSV input and returns a map of story IDs to their classes and relationships
     * Expected CSV format:
     * - For classes: Story, Class
     * - For relationships: Story, Source, Relation, Target
     */
    public Map<String, Map<String, Object>> readCsvData(InputStream is) throws Exception {
        Map<String, Map<String, Object>> result = new HashMap<>();

        Reader reader = new InputStreamReader(is);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());

        // Determine CSV format by checking headers
        Map<String, Integer> headerMap = csvParser.getHeaderMap();
        boolean isRelationshipFormat = headerMap.containsKey("Source") && headerMap.containsKey("Relation") && headerMap.containsKey("Target");
        boolean isClassFormat = headerMap.containsKey("Class") || headerMap.containsKey("ClassName");

        for (CSVRecord record : csvParser) {
            String story = record.get("Story");
            if (story == null || story.trim().isEmpty()) {
                continue;
            }

            story = story.trim();
            Map<String, Object> storyData = result.computeIfAbsent(story, k -> {
                Map<String, Object> data = new HashMap<>();
                data.put("classes", new HashSet<String>());
                data.put("relationships", new HashSet<Relationship>());
                return data;
            });

            if (isRelationshipFormat) {
                // Parse relationship data
                String source = record.get("Source");
                String relation = record.get("Relation");
                String target = record.get("Target");

                if (source != null && relation != null && target != null &&
                    !source.trim().isEmpty() && !relation.trim().isEmpty() && !target.trim().isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Set<Relationship> relationships = (Set<Relationship>) storyData.get("relationships");
                    relationships.add(new Relationship(source.trim(), relation.trim(), target.trim()));
                }
            } else if (isClassFormat) {
                // Parse class data
                String className = record.get(headerMap.containsKey("Class") ? "Class" : "ClassName");

                if (className != null && !className.trim().isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Set<String> classes = (Set<String>) storyData.get("classes");
                    classes.add(className.trim());
                }
            }
        }

        csvParser.close();
        return result;
    }

    /**
     * Reads CSV with both classes and relationships combined
     * Expected format: Story, Type (Class/Relationship), Value1, Value2, Value3
     * For Class: Type=Class, Value1=ClassName
     * For Relationship: Type=Relationship, Value1=Source, Value2=Relation, Value3=Target
     */
    public Map<String, Map<String, Object>> readCombinedCsvData(InputStream is) throws Exception {
        Map<String, Map<String, Object>> result = new HashMap<>();

        Reader reader = new InputStreamReader(is);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());

        // Column indices
        final int STORY_COLUMN = 0;
        final int TYPE_COLUMN = 1;
        final int VALUE1_COLUMN = 2;
        final int VALUE2_COLUMN = 3;
        final int VALUE3_COLUMN = 4;

        for (CSVRecord record : csvParser) {
            String story = record.get(STORY_COLUMN); // First column is story
            if (story == null || story.trim().isEmpty()) {
                continue;
            }

            story = story.trim();
            Map<String, Object> storyData = result.computeIfAbsent(story, k -> {
                Map<String, Object> data = new HashMap<>();
                data.put("classes", new HashSet<String>());
                data.put("relationships", new HashSet<Relationship>());
                return data;
            });

            String type = record.get(TYPE_COLUMN); // Second column indicates type
            if ("Class".equalsIgnoreCase(type)) {
                String className = record.get(VALUE1_COLUMN);
                if (className != null && !className.trim().isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Set<String> classes = (Set<String>) storyData.get("classes");
                    classes.add(className.trim());
                }
            } else if ("Relationship".equalsIgnoreCase(type)) {
                String source = record.get(VALUE1_COLUMN);
                String relation = record.get(VALUE2_COLUMN);
                String target = record.get(VALUE3_COLUMN);

                if (source != null && relation != null && target != null &&
                    !source.trim().isEmpty() && !relation.trim().isEmpty() && !target.trim().isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Set<Relationship> relationships = (Set<Relationship>) storyData.get("relationships");
                    relationships.add(new Relationship(source.trim(), relation.trim(), target.trim()));
                }
            }
        }

        csvParser.close();
        return result;
    }
}
