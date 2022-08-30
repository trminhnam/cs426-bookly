package com.example.bookly.Model;

import androidx.annotation.NonNull;

import java.util.List;

public class SentimentModel {
    private List<PredictResultModel> results;
    private String label;
    private double score;

    public SentimentModel(List<PredictResultModel> results) {
        setResults(results);
    }

    public SentimentModel(){
        this.label = "POSITIVE";
        this.score = 1.0;
    }

    public List<PredictResultModel> getResults() {
        return results;
    }

    public void setResults(List<PredictResultModel> results) {
        this.results = results;
        // get highest score
        double highestScore = 0;
        for (PredictResultModel result : results) {
            if (result.getScore() > highestScore) {
                highestScore = result.getScore();
                label = result.getLabel();
                score = highestScore;
            }
        }
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

    @NonNull
    public String toString() {
        return "SentimentModel [label=" + getLabel() + ", score=" + getScore() + "]";
    }
}
