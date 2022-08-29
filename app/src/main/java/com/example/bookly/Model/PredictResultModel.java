package com.example.bookly.Model;

public class PredictResultModel {
    private String label;
    private double score;

    public PredictResultModel(String label, double score) {
        this.label = label;
        this.score = score;
    }

    public PredictResultModel(String label) {
        this.label = label;
    }

    public PredictResultModel(double score) {
        this.score = score;
    }

    public PredictResultModel() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
