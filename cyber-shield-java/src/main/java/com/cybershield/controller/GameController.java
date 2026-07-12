package com.cybershield.controller;

import com.cybershield.database.DatabaseConnection;
import com.cybershield.model.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class managing the Cyber Shield game state, progress, and database synchronization.
 */
public class GameController {
    private static GameController instance;
    private Player currentPlayer;
    private int currentLevelIndex = 0;
    private boolean isDbConnected = false;

    private GameController() {
        // Attempt to pre-initialize the schema if server is online
        try {
            DatabaseConnection.initializeDatabaseSchema();
            isDbConnected = true;
        } catch (Exception e) {
            System.err.println("Database initialization failed at startup: " + e.getMessage());
            isDbConnected = false;
        }
    }

    public static synchronized GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public void incrementLevel() {
        currentLevelIndex++;
    }

    public void resetGame() {
        currentLevelIndex = 0;
        if (currentPlayer != null) {
            currentPlayer.setCurrentScore(0);
        }
    }

    /**
     * Registers or logs in a player. Saves them in the MySQL database if available.
     * @param username The name typed by the user.
     * @return True if successful or falls back gracefully.
     */
    public boolean loginPlayer(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        String cleanName = username.trim();
        this.currentPlayer = new Player(cleanName);
        
        // Attempt to insert into MySQL
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO users (username) VALUES (?) ON DUPLICATE KEY UPDATE username=username";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, cleanName);
                pstmt.executeUpdate();
                System.out.println("User '" + cleanName + "' logged in/saved in MySQL successfully.");
                isDbConnected = true;
            }
        } catch (Exception e) {
            System.err.println("MySQL login save failed: " + e.getMessage() + ". Running in offline/fallback mode.");
            isDbConnected = false;
        }
        return true;
    }

    /**
     * Saves the final score and badge of the current player to MySQL.
     * @return True if saved to DB, false otherwise.
     */
    public boolean saveFinalScore() {
        if (currentPlayer == null) {
            return false;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO scores (username, score, badge) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, currentPlayer.getUsername());
                pstmt.setInt(2, currentPlayer.getCurrentScore());
                pstmt.setString(3, currentPlayer.getBadgeLevel());
                pstmt.executeUpdate();
                System.out.println("Saved final score of " + currentPlayer.getCurrentScore() + " to database.");
                isDbConnected = true;
                return true;
            }
        } catch (Exception e) {
            System.err.println("Could not save final score to MySQL: " + e.getMessage());
            isDbConnected = false;
            return false;
        }
    }

    /**
     * Retrieves the top 10 scores from the scores table.
     */
    public List<ScoreRecord> getTopScores() {
        List<ScoreRecord> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT username, score, badge, played_at FROM scores ORDER BY score DESC, played_at DESC LIMIT 10";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new ScoreRecord(
                            rs.getString("username"),
                            rs.getInt("score"),
                            rs.getString("badge"),
                            rs.getTimestamp("played_at").toString()
                    ));
                }
                isDbConnected = true;
            }
        } catch (Exception e) {
            System.err.println("Could not query scoreboard from MySQL: " + e.getMessage());
            isDbConnected = false;
            
            // Generate some mock fallback records to keep the UI beautiful if DB is offline
            list.add(new ScoreRecord("CyberSentinel (Local)", 100, "Cyber Hero", "2026-07-12 12:00:00"));
            list.add(new ScoreRecord("ShieldTracer (Local)", 80, "Intermediate", "2026-07-12 12:10:00"));
            list.add(new ScoreRecord("FirewallAlpha (Local)", 40, "Beginner", "2026-07-12 12:20:00"));
        }
        return list;
    }

    public boolean isDbConnected() {
        return isDbConnected;
    }

    /**
     * Score record helper model.
     */
    public static class ScoreRecord {
        private final String username;
        private final int score;
        private final String badge;
        private final String playedAt;

        public ScoreRecord(String username, int score, String badge, String playedAt) {
            this.username = username;
            this.score = score;
            this.badge = badge;
            this.playedAt = playedAt;
        }

        public String getUsername() {
            return username;
        }

        public int getScore() {
            return score;
        }

        public String getBadge() {
            return badge;
        }

        public String getPlayedAt() {
            return playedAt;
        }
    }
}
