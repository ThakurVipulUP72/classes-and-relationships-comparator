package com.example.classcompare.util;

import com.example.classcompare.model.ComparisonResponse;
import com.example.classcompare.model.Relationship;
import com.example.classcompare.model.StoryComparisonResult;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PlantUmlGenerator {

    /**
     * Generates PlantUML class diagram from comparison results
     * Includes all classes and relationships with color coding for match status
     */
    public String generatePlantUml(ComparisonResponse comparison) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        sb.append("' Generated Class Diagram from Comparison Results\n\n");

        Set<String> allClasses = new HashSet<>();
        Set<Relationship> allRelationships = new HashSet<>();

        // Collect all unique classes and relationships
        for (StoryComparisonResult story : comparison.getStories()) {
            allClasses.addAll(story.getCommon());
            allClasses.addAll(story.getOnlyManual());
            allClasses.addAll(story.getOnlyLLM());
            
            allRelationships.addAll(story.getCommonRelationships());
            allRelationships.addAll(story.getOnlyManualRelationships());
            allRelationships.addAll(story.getOnlyLLMRelationships());
        }

        // Define classes
        for (String className : allClasses) {
            String color = getClassColor(className, comparison);
            sb.append("class ").append(className);
            if (color != null) {
                sb.append(" ").append(color);
            }
            sb.append("\n");
        }

        sb.append("\n");

        // Define relationships
        for (Relationship rel : allRelationships) {
            String arrow = convertToPlantUmlArrow(rel.getRelation());
            sb.append(rel.getSource())
              .append(" ")
              .append(arrow)
              .append(" ")
              .append(rel.getTarget());
            
            // Add label if not just "Association"
            if (!rel.getRelation().equalsIgnoreCase("Association")) {
                sb.append(" : ").append(rel.getRelation());
            }
            
            sb.append("\n");
        }

        sb.append("\n@enduml\n");
        return sb.toString();
    }

    /**
     * Determines the color for a class based on its match status
     */
    private String getClassColor(String className, ComparisonResponse comparison) {
        for (StoryComparisonResult story : comparison.getStories()) {
            if (story.getCommon().contains(className)) {
                return "#LightGreen";
            }
            if (story.getOnlyManual().contains(className)) {
                return "#LightCoral";
            }
            if (story.getOnlyLLM().contains(className)) {
                return "#LightBlue";
            }
        }
        return null;
    }

    /**
     * Converts relationship type to PlantUML arrow notation
     */
    private String convertToPlantUmlArrow(String relationType) {
        String lower = relationType.toLowerCase();
        if (lower.contains("inherit") || lower.contains("extends")) {
            return "<|--";
        } else if (lower.contains("compos")) {
            return "*--";
        } else if (lower.contains("aggreg")) {
            return "o--";
        } else if (lower.contains("realiz") || lower.contains("implement")) {
            return "..|>";
        } else if (lower.contains("depend")) {
            return "..>";
        } else {
            return "-->";
        }
    }
}
