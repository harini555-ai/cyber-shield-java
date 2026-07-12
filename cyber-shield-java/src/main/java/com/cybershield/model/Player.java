package com.cybershield.model;

/**
 * Encapsulates the Player entity in the Cyber Shield game.
 */
public class Player {
    private int id;
    private String username;
    private int currentScore;

    public Player(String username) {
        this.username = username;
        this.currentScore = 0;
    }

    public Player(int id, String username) {
        this.id = id;
        this.username = username;
        this.currentScore = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void addPoints(int points) {
        this.currentScore += points;
    }

    // Determine badge level based on score
    public String getBadgeLevel() {
        if (currentScore >= 81) {
            return "Cyber Hero";
        } else if (currentScore >= 41) {
            return "Intermediate";
        } else {
            return "Beginner";
        }
    }
}
