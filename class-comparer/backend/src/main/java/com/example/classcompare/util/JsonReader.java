package com.example.classcompare.util;

import com.example.classcompare.model.DatasetData;
import com.example.classcompare.model.Relationship;
import com.example.classcompare.model.StoryData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
public class JsonReader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads JSON input and returns a map of story IDs to their classes and relationships
     * The returned map contains:
     * - Key: story ID as String
     * - Value: Map with "classes" -> Set<String> and "relationships" -> Set<Relationship>
     */
    public Map<String, Map<String, Object>> readJsonData(InputStream is) throws Exception {
        Map<String, Map<String, Object>> result = new HashMap<>();

        // Parse the root JSON object which contains multiple datasets
        @SuppressWarnings("unchecked")
        Map<String, DatasetData> rootMap = objectMapper.readValue(is, 
            objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, DatasetData.class));

        // Process each dataset (e.g., "Camperplus_sampled", "Fishchips_sampled", etc.)
        for (Map.Entry<String, DatasetData> datasetEntry : rootMap.entrySet()) {
            String datasetName = datasetEntry.getKey();
            DatasetData dataset = datasetEntry.getValue();

            if (dataset.getStories() != null) {
                for (StoryData story : dataset.getStories()) {
                    // Create a key combining dataset name and story ID
                    String storyKey = datasetName + "_Story_" + story.getStory_id();

                    Map<String, Object> storyData = new HashMap<>();
                    
                    // Add classes
                    Set<String> classes = new HashSet<>(story.getClasses() != null ? story.getClasses() : Collections.emptyList());
                    storyData.put("classes", classes);
                    
                    // Add relationships
                    Set<Relationship> relationships = new HashSet<>(story.getRelationships() != null ? story.getRelationships() : Collections.emptyList());
                    storyData.put("relationships", relationships);

                    result.put(storyKey, storyData);
                }
            }
        }

        return result;
    }
}
