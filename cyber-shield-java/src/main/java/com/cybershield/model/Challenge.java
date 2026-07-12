package com.cybershield.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a cybersecurity awareness scenario or challenge.
 */
public class Challenge {
    private String title;
    private String description;
    private String scenarioText;
    private List<String> options;
    private int correctOptionIndex;
    private String explanation;
    private int rewardPoints;

    public Challenge(String title, String description, String scenarioText, List<String> options, int correctOptionIndex, String explanation, int rewardPoints) {
        this.title = title;
        this.description = description;
        this.scenarioText = scenarioText;
        this.options = new ArrayList<>(options);
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
        this.rewardPoints = rewardPoints;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getScenarioText() {
        return scenarioText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public boolean checkAnswer(int selectedIndex) {
        return selectedIndex == correctOptionIndex;
    }
}
