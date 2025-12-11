package com.example.classcompare.model;

import java.util.List;

public class StoryData {
    private int story_id;
    private List<String> classes;
    private List<Relationship> relationships;

    public StoryData() {}

    public StoryData(int story_id, List<String> classes, List<Relationship> relationships) {
        this.story_id = story_id;
        this.classes = classes;
        this.relationships = relationships;
    }

    public int getStory_id() {
        return story_id;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }
}
