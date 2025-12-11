package com.example.classcompare.model;

import java.util.Objects;

public class Relationship {
    private String source;
    private String relation;
    private String target;

    public Relationship() {}

    public Relationship(String source, String relation, String target) {
        this.source = source;
        this.relation = relation;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relationship that = (Relationship) o;
        return Objects.equals(source, that.source) &&
               Objects.equals(relation, that.relation) &&
               Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, relation, target);
    }

    @Override
    public String toString() {
        return source + " " + relation + " " + target;
    }
}
