package com.example.classcompare.model;

import java.util.List;

public class DatasetData {
    private List<StoryData> stories;

    public DatasetData() {}

    public DatasetData(List<StoryData> stories) {
        this.stories = stories;
    }

    public List<StoryData> getStories() {
        return stories;
    }

    public void setStories(List<StoryData> stories) {
        this.stories = stories;
    }
}
