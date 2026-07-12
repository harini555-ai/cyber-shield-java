package com.cybershield.view;

import com.cybershield.controller.GameController;
import com.cybershield.model.Challenge;
import com.cybershield.utils.GameData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Main JavaFX Application class representing the Cyber Shield UI and state flow.
 */
public class GameApp extends Application {
    private Stage primaryStage;
    private Scene mainScene;
    private GameController controller;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.controller = GameController.getInstance();

        primaryStage.setTitle("Cyber Shield – Cybersecurity Awareness Game");
        primaryStage.setResizable(false);

        // Start by showing the Login screen
        VBox loginRoot = createLoginRoot();
        mainScene = new Scene(loginRoot, 900, 650);
        
        // Load style.css
        try {
            String cssPath = getClass().getResource("/css/style.css").toExternalForm();
            mainScene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Style sheet not found, proceeding with default styling. Details: " + e.getMessage());
        }

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Updates the main scene root node to transition between screen states.
     */
    private void setSceneRoot(Parent root) {
        mainScene.setRoot(root);
    }

    /**
     * Screen 1: LOGIN SCREEN
     */
    private VBox createLoginRoot() {
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        // Header Title
        Label titleLabel = new Label("CYBER SHIELD");
        titleLabel.getStyleClass().add("title-label");

        Label subtitleLabel = new Label("Secure the Future. One Decision at a Time.");
        subtitleLabel.getStyleClass().add("subtitle-label");

        // Login Card
        VBox loginCard = new VBox(15);
        loginCard.getStyleClass().add("cyber-card-glow");
        loginCard.setMaxWidth(420);
        loginCard.setPadding(new Insets(30));
        loginCard.setAlignment(Pos.CENTER);

        Label cardHeader = new Label("ENTER AGENT CREDENTIALS");
        cardHeader.setStyle("-fx-text-fill: #00f0ff; -fx-font-weight: bold; -fx-font-size: 14px; -fx-font-family: 'Courier New';");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Code Name / Username...");
        usernameField.getStyleClass().add("text-input");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 12px;");

        Button loginButton = new Button("ACCESS SYSTEM");
        loginButton.getStyleClass().add("btn-primary");
        loginButton.setMinWidth(200);

        // DB Status Indicator
        HBox dbStatusBox = new HBox(5);
        dbStatusBox.setAlignment(Pos.CENTER);
        Label dbStatusText = new Label(controller.isDbConnected() ? "● SECURE DATABASE CONNECTED" : "● OFFLINE FALLBACK MODE");
        dbStatusText.getStyleClass().add(controller.isDbConnected() ? "database-badge-connected" : "database-badge-disconnected");
        dbStatusBox.getChildren().add(dbStatusText);

        loginButton.setOnAction(e -> {
            String name = usernameField.getText();
            if (name == null || name.trim().isEmpty()) {
                errorLabel.setText("Agent code name cannot be empty.");
            } else if (name.length() > 25) {
                errorLabel.setText("Username must be under 25 characters.");
            } else {
                boolean success = controller.loginPlayer(name);
                if (success) {
                    showHomeScreen();
                } else {
                    errorLabel.setText("Failed to register credential.");
                }
            }
        });

        loginCard.getChildren().addAll(cardHeader, usernameField, errorLabel, loginButton, dbStatusBox);
        root.getChildren().addAll(titleLabel, subtitleLabel, loginCard);

        return root;
    }

    /**
     * Screen 2: HOME SCREEN
     */
    private void showHomeScreen() {
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        Label titleLabel = new Label("AGENT DASHBOARD");
        titleLabel.getStyleClass().add("title-label");

        String username = controller.getCurrentPlayer() != null ? controller.getCurrentPlayer().getUsername() : "Unknown Agent";
        Label welcomeLabel = new Label("Welcome back, Agent: " + username);
        welcomeLabel.setStyle("-fx-text-fill: #f8fafc; -fx-font-size: 18px; -fx-font-weight: bold;");

        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(350);
        menuBox.setPadding(new Insets(20));
        menuBox.getStyleClass().add("cyber-card");

        Button startBtn = new Button("START CHALLENGE MISSION");
        startBtn.getStyleClass().add("btn-primary");
        startBtn.setMaxWidth(Double.MAX_VALUE);
        startBtn.setOnAction(e -> {
            controller.resetGame();
            showLevelScreen();
        });

        Button scoreBtn = new Button("VIEW HIGH SCORES");
        scoreBtn.getStyleClass().add("btn-secondary");
        scoreBtn.setMaxWidth(Double.MAX_VALUE);
        scoreBtn.setOnAction(e -> showScoreboardScreen());

        Button exitBtn = new Button("TERMINATE SESSION");
        exitBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 10px;");
        exitBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        menuBox.getChildren().addAll(startBtn, scoreBtn, exitBtn);
        root.getChildren().addAll(titleLabel, welcomeLabel, menuBox);

        setSceneRoot(root);
    }

    /**
     * Screen 3: LEVEL/CHALLENGE SCREEN (Levels 1 to 5)
     */
    private void showLevelScreen() {
        int currentLevelIndex = controller.getCurrentLevelIndex();
        Challenge challenge = GameData.getChallenge(currentLevelIndex);

        if (challenge == null) {
            // All levels completed, save score and transition to Result Screen
            controller.saveFinalScore();
            showResultScreen();
            return;
        }

        // Parent layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));

        // Top HUD Header
        HBox hudHeader = new HBox(20);
        hudHeader.setAlignment(Pos.CENTER_LEFT);
        hudHeader.setPadding(new Insets(10, 10, 20, 10));
        hudHeader.setStyle("-fx-border-color: #1f2937; -fx-border-width: 0 0 1 0;");

        Label levelTitle = new Label(challenge.getTitle());
        levelTitle.getStyleClass().add("subtitle-label");
        levelTitle.setStyle("-fx-font-size: 18px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label scoreLabel = new Label("SCORE: " + controller.getCurrentPlayer().getCurrentScore() + " PTS");
        scoreLabel.setStyle("-fx-text-fill: #00f0ff; -fx-font-weight: bold; -fx-font-size: 16px; -fx-font-family: 'Courier New';");

        hudHeader.getChildren().addAll(levelTitle, spacer, scoreLabel);
        root.setTop(hudHeader);

        // Center Content Area (Scenario Description)
        VBox centerContent = new VBox(15);
        centerContent.setPadding(new Insets(20, 10, 10, 10));

        Label challengeDesc = new Label(challenge.getDescription());
        challengeDesc.getStyleClass().add("text-normal");
        challengeDesc.setWrapText(true);
        challengeDesc.setStyle("-fx-font-weight: bold;");

        // Code/Detail Box
        VBox detailCard = new VBox(10);
        detailCard.getStyleClass().add("cyber-card");
        detailCard.setStyle("-fx-background-color: #0d1117;"); // Code-like background
        
        Label scenarioLabel = new Label(challenge.getScenarioText());
        scenarioLabel.getStyleClass().add("text-normal");
        scenarioLabel.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13px;");
        scenarioLabel.setWrapText(true);
        
        detailCard.getChildren().add(scenarioLabel);

        centerContent.getChildren().addAll(challengeDesc, detailCard);
        root.setCenter(centerContent);

        // Right side options panel
        VBox optionsContainer = new VBox(12);
        optionsContainer.setAlignment(Pos.CENTER_LEFT);
        optionsContainer.setPadding(new Insets(20, 0, 10, 25));
        optionsContainer.setPrefWidth(380);

        Label optionsHeader = new Label("CHOOSE YOUR REACTION:");
        optionsHeader.setStyle("-fx-text-fill: #94a3b8; -fx-font-weight: bold; -fx-font-size: 12px;");
        optionsContainer.getChildren().add(optionsHeader);

        List<String> options = challenge.getOptions();
        for (int i = 0; i < options.size(); i++) {
            final int index = i;
            Button optionBtn = new Button((i + 1) + ". " + options.get(i));
            optionBtn.getStyleClass().add("option-button");
            optionBtn.setMaxWidth(Double.MAX_VALUE);
            optionBtn.setWrapText(true);

            optionBtn.setOnAction(e -> handleAnswerSubmission(index, challenge));
            optionsContainer.getChildren().add(optionBtn);
        }

        root.setRight(optionsContainer);
        setSceneRoot(root);
    }

    /**
     * Evaluates selection, displays explanation modal, updates points, and moves forward.
     */
    private void handleAnswerSubmission(int selectedIndex, Challenge challenge) {
        boolean isCorrect = challenge.checkAnswer(selectedIndex);
        int scoreEarned = 0;

        if (isCorrect) {
            scoreEarned = challenge.getRewardPoints();
            controller.getCurrentPlayer().addPoints(scoreEarned);
        }

        // Show a custom explanation screen instead of blocking standard Dialog so it feels extremely polished and styled
        VBox explRoot = new VBox(20);
        explRoot.setAlignment(Pos.CENTER);
        explRoot.setPadding(new Insets(40));

        Label feedbackLabel = new Label(isCorrect ? "MISSION SUCCESSFUL!" : "MISSION COMPROMISED!");
        feedbackLabel.setStyle(isCorrect ? 
                "-fx-text-fill: #10b981; -fx-font-size: 24px; -fx-font-weight: bold;" : 
                "-fx-text-fill: #ef4444; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox explCard = new VBox(15);
        explCard.getStyleClass().add(isCorrect ? "correct-box" : "wrong-box");
        explCard.setMaxWidth(650);
        explCard.setPadding(new Insets(25));

        Label pointsLabel = new Label(isCorrect ? "+" + scoreEarned + " SECURE POINTS AWARDED" : "0 POINTS EARNED");
        pointsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: " + (isCorrect ? "#10b981" : "#ef4444") + ";");

        Label explanationLabel = new Label(challenge.getExplanation());
        explanationLabel.getStyleClass().add("text-normal");
        explanationLabel.setWrapText(true);
        explanationLabel.setStyle("-fx-line-spacing: 4px;");

        explCard.getChildren().addAll(pointsLabel, explanationLabel);

        Button nextBtn = new Button("CONTINUE MISSION");
        nextBtn.getStyleClass().add("btn-primary");
        nextBtn.setMinWidth(180);
        nextBtn.setOnAction(e -> {
            controller.incrementLevel();
            showLevelScreen();
        });

        explRoot.getChildren().addAll(feedbackLabel, explCard, nextBtn);
        setSceneRoot(explRoot);
    }

    /**
     * Screen 4: RESULT SCREEN
     */
    private void showResultScreen() {
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(45));

        Label titleLabel = new Label("MISSION COMPLETION REPORT");
        titleLabel.getStyleClass().add("title-label");
        titleLabel.setStyle("-fx-text-fill: #10b981;");

        // Results Card
        VBox resultCard = new VBox(18);
        resultCard.getStyleClass().add("cyber-card-glow");
        resultCard.setMaxWidth(500);
        resultCard.setPadding(new Insets(35));
        resultCard.setAlignment(Pos.CENTER);

        String username = controller.getCurrentPlayer() != null ? controller.getCurrentPlayer().getUsername() : "Agent";
        int finalScore = controller.getCurrentPlayer() != null ? controller.getCurrentPlayer().getCurrentScore() : 0;
        String badge = controller.getCurrentPlayer() != null ? controller.getCurrentPlayer().getBadgeLevel() : "Beginner";

        Label nameLabel = new Label("OPERATIVE ID: " + username);
        nameLabel.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Courier New';");

        Label scoreLabel = new Label("FINAL SECURITY SCORE: " + finalScore + " / 100 PTS");
        scoreLabel.setStyle("-fx-text-fill: #00f0ff; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Custom styling for badge
        Label badgeLabel = new Label("SECURITY RANK: " + badge.toUpperCase());
        String badgeColor = "#ef4444"; // default red
        if (badge.equals("Cyber Hero")) {
            badgeColor = "#10b981"; // green
        } else if (badge.equals("Intermediate")) {
            badgeColor = "#3b82f6"; // blue
        }
        badgeLabel.setStyle("-fx-text-fill: " + badgeColor + "; -fx-font-size: 22px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, " + badgeColor + "80, 10, 0, 0, 0);");

        Label descriptionLabel = new Label();
        descriptionLabel.getStyleClass().add("text-muted");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setAlignment(Pos.CENTER);
        if (badge.equals("Cyber Hero")) {
            descriptionLabel.setText("Outstanding! You demonstrated an exceptional grasp of network security, physical cyber threats, phishing evasion, and credential integrity.");
        } else if (badge.equals("Intermediate")) {
            descriptionLabel.setText("Good work. You know the fundamentals but remains vulnerable to sophisticated socially engineered attacks. Keep practicing.");
        } else {
            descriptionLabel.setText("Warning: High vulnerability detected. You fell for common hacker baits. Re-evaluate your training checklist immediately.");
        }

        resultCard.getChildren().addAll(nameLabel, scoreLabel, badgeLabel, descriptionLabel);

        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER);

        Button restartBtn = new Button("RUN CHALLENGE AGAIN");
        restartBtn.getStyleClass().add("btn-primary");
        restartBtn.setOnAction(e -> {
            controller.resetGame();
            showLevelScreen();
        });

        Button homeBtn = new Button("RETURN TO DASHBOARD");
        homeBtn.getStyleClass().add("btn-secondary");
        homeBtn.setOnAction(e -> showHomeScreen());

        actionBox.getChildren().addAll(restartBtn, homeBtn);
        root.getChildren().addAll(titleLabel, resultCard, actionBox);

        setSceneRoot(root);
    }

    /**
     * Screen 5: SCOREBOARD / HIGH SCORES SCREEN
     */
    private void showScoreboardScreen() {
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        Label titleLabel = new Label("GLOBAL HIGH SCORE DATABASE");
        titleLabel.getStyleClass().add("title-label");

        VBox tableContainer = new VBox(10);
        tableContainer.getStyleClass().add("cyber-card");
        tableContainer.setMaxWidth(650);
        tableContainer.setPrefHeight(350);

        // Header Row
        GridPane headerRow = new GridPane();
        headerRow.setStyle("-fx-background-color: #1e293b; -fx-padding: 10px; -fx-background-radius: 6px;");
        headerRow.getColumnConstraints().addAll(
                new ColumnConstraints(50),  // Rank
                new ColumnConstraints(180), // Name
                new ColumnConstraints(100), // Score
                new ColumnConstraints(150), // Badge
                new ColumnConstraints(150)  // Date
        );

        Label rankH = new Label("#"); rankH.setStyle("-fx-text-fill: #00f0ff; -fx-font-weight: bold;");
        Label nameH = new Label("OPERATIVE"); nameH.setStyle("-fx-text-fill: #00f0ff; -fx-font-weight: bold;");
        Label scoreH = new Label("SCORE"); scoreH.setStyle("-fx-text-fill: #00f0ff; -fx-font-weight: bold;");
        Label badgeH = new Label("SECURITY RANK"); badgeH.setStyle("-fx-text-fill: #00f0ff; -fx-font-weight: bold;");
        Label dateH = new Label("RECORD TIME"); dateH.setStyle("-fx-text-fill: #00f0ff; -fx-font-weight: bold;");

        headerRow.addRow(0, rankH, nameH, scoreH, badgeH, dateH);
        tableContainer.getChildren().add(headerRow);

        // ScrollPane for records
        VBox recordsBox = new VBox(8);
        ScrollPane scrollPane = new ScrollPane(recordsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        List<GameController.ScoreRecord> scores = controller.getTopScores();
        for (int i = 0; i < scores.size(); i++) {
            GameController.ScoreRecord record = scores.get(i);
            GridPane row = new GridPane();
            row.setStyle("-fx-padding: 8px; -fx-border-color: #1f2937; -fx-border-width: 0 0 1 0;");
            row.getColumnConstraints().addAll(
                    new ColumnConstraints(50),
                    new ColumnConstraints(180),
                    new ColumnConstraints(100),
                    new ColumnConstraints(150),
                    new ColumnConstraints(150)
            );

            Label rLabel = new Label(String.valueOf(i + 1)); rLabel.getStyleClass().add("text-muted");
            Label nLabel = new Label(record.getUsername()); nLabel.getStyleClass().add("text-normal"); nLabel.setStyle("-fx-font-weight: bold;");
            Label sLabel = new Label(record.getScore() + " pts"); sLabel.setStyle("-fx-text-fill: #00f0ff; -fx-font-weight: bold;");
            Label bLabel = new Label(record.getBadge());
            
            String col = "#ef4444";
            if (record.getBadge().contains("Hero")) col = "#10b981";
            else if (record.getBadge().contains("Inter")) col = "#3b82f6";
            bLabel.setStyle("-fx-text-fill: " + col + "; -fx-font-size: 12px; -fx-font-weight: bold;");

            // Format date slightly
            String cleanDate = record.getPlayedAt();
            if (cleanDate.contains(".")) {
                cleanDate = cleanDate.substring(0, cleanDate.lastIndexOf("."));
            }
            Label dLabel = new Label(cleanDate); dLabel.getStyleClass().add("text-muted");
            dLabel.setStyle("-fx-font-size: 11px;");

            row.addRow(0, rLabel, nLabel, sLabel, bLabel, dLabel);
            recordsBox.getChildren().add(row);
        }

        tableContainer.getChildren().add(scrollPane);

        Button backBtn = new Button("RETURN TO DASHBOARD");
        backBtn.getStyleClass().add("btn-primary");
        backBtn.setOnAction(e -> showHomeScreen());

        root.getChildren().addAll(titleLabel, tableContainer, backBtn);
        setSceneRoot(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
