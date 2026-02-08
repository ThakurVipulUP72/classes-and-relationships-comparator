package com.example.classcompare.controller;

import com.example.classcompare.model.ComparisonResponse;
import com.example.classcompare.model.StoryComparisonResult;
import com.example.classcompare.service.SuggestionEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/suggestions")
@CrossOrigin(origins = "http://localhost:3000")
public class SuggestionController {

    private final SuggestionEngine suggestionEngine;

    public SuggestionController(SuggestionEngine suggestionEngine) {
        this.suggestionEngine = suggestionEngine;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> getSuggestions(
            @RequestBody ComparisonResponse comparison,
            @RequestParam(defaultValue = "0.6") double matchThreshold,
            @RequestParam(defaultValue = "0.85") double mergeThreshold) {
        
        Map<String, Object> allSuggestions = new HashMap<>();
        
        // Collect all unmatched items across stories
        Set<String> allUnmatchedManual = new HashSet<>();
        Set<String> allUnmatchedLlm = new HashSet<>();
        
        for (StoryComparisonResult story : comparison.getStories()) {
            allUnmatchedManual.addAll(story.getOnlyManual());
            allUnmatchedLlm.addAll(story.getOnlyLLM());
        }
        
        // Generate suggestions
        Map<String, Object> suggestions = suggestionEngine.generateSuggestions(
                allUnmatchedManual, allUnmatchedLlm, matchThreshold, mergeThreshold);
        
        allSuggestions.putAll(suggestions);
        allSuggestions.put("matchThreshold", matchThreshold);
        allSuggestions.put("mergeThreshold", mergeThreshold);
        
        return ResponseEntity.ok(allSuggestions);
    }

    @PostMapping("/story/{storyIndex}")
    public ResponseEntity<Map<String, Object>> getStorySuggestions(
            @PathVariable int storyIndex,
            @RequestBody ComparisonResponse comparison,
            @RequestParam(defaultValue = "0.6") double matchThreshold,
            @RequestParam(defaultValue = "0.85") double mergeThreshold) {
        
        if (storyIndex < 0 || storyIndex >= comparison.getStories().size()) {
            return ResponseEntity.badRequest().build();
        }
        
        StoryComparisonResult story = comparison.getStories().get(storyIndex);
        
        Map<String, Object> suggestions = suggestionEngine.generateSuggestions(
                story.getOnlyManual(),
                story.getOnlyLLM(),
                matchThreshold,
                mergeThreshold);
        
        suggestions.put("storyName", story.getStoryName());
        suggestions.put("matchThreshold", matchThreshold);
        suggestions.put("mergeThreshold", mergeThreshold);
        
        return ResponseEntity.ok(suggestions);
    }
}
