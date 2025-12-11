package com.example.classcompare.model;

public class Metrics {
    private double precision;
    private double recall;
    private double f1Score;

    public Metrics() {}

    public Metrics(double precision, double recall, double f1Score) {
        this.precision = precision;
        this.recall = recall;
        this.f1Score = f1Score;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getF1Score() {
        return f1Score;
    }

    public void setF1Score(double f1Score) {
        this.f1Score = f1Score;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "precision=" + precision +
                ", recall=" + recall +
                ", f1Score=" + f1Score +
                '}';
    }
}
