package com.example.classcompare.model;



import java.util.List;

public class ComparisonResponse {
    private List<StoryComparisonResult> stories;

    public ComparisonResponse() {}

    public ComparisonResponse(List<StoryComparisonResult> stories) {
        this.stories = stories;
    }

    public List<StoryComparisonResult> getStories() {
        return stories;
    }

    public void setStories(List<StoryComparisonResult> stories) {
        this.stories = stories;
    }
}
