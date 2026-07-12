# Cyber Shield – Cybersecurity Awareness Game

A polished **JavaFX Desktop Game** designed to train and certify users in critical cybersecurity awareness vectors (Phishing, Passwords, Legitimate URLs, Public Wi-Fi protection, and USB security drops).

This game connects to a **MySQL database** to authenticate players and log high scores dynamically.

---

## 🛠️ Tech Stack & Requirements

- **Runtime**: Java 17 (or newer)
- **GUI Engine**: JavaFX 17.0.6 (plain classpath app — no `module-info.java` needed)
- **Database**: MySQL 8.x
- **Dependency & Build Management**: Maven 3.x. `pom.xml` includes OS-detection profiles (`windows` / `mac` / `linux`) that automatically pick the matching JavaFX native-library classifier, so the same `pom.xml` builds correctly on any platform.
- **Database Driver**: JDBC (MySQL Connector/J)

---

## 📂 Project Architecture

```text
cyber-shield/
│
├── pom.xml                     # Maven build descriptor with JavaFX + MySQL dependencies
├── cyber_shield.sql            # DB seeding script (creates tables: users, scores)
├── README.md                   # Setup instruction guide
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── cybershield/
        │           ├── database/
        │           │   └── DatabaseConnection.java   # JDBC Connector & setup logic
        │           ├── model/
        │           │   ├── Player.java               # Operative account details
        │           │   └── Challenge.java            # Scenario questions/options
        │           ├── controller/
        │           │   └── GameController.java       # Central engine coordinating state transitions & SQL saves
        │           │   └── utils/
        │           │       └── GameData.java         # Content for Levels 1 to 5
        │           └── view/
        │               └── GameApp.java              # JavaFX UI Entry Application (Styles, Screens, Transitions)
        │
        └── resources/
            └── css/
                └── style.css                         # Dark modern terminal cybersecurity stylesheet
```

---

## 🗄️ Database Setup (MySQL)

1. Make sure your local **MySQL Server** is running on port `3306`.
2. Connect to your MySQL database using your terminal, workbench, or IDE tool and run the seeding script:
   ```sql
   source cyber_shield.sql;
   ```
   *Alternatively, manually execute the commands in `cyber_shield.sql` to establish the database `cyber_shield` and create the `users` and `scores` tables.*

3. If your MySQL credentials are not `root` / `password`, you have two options:
   - **Quick fix (recommended):** set environment variables (or JVM `-D` system properties) before running — no source changes needed:
     ```bash
     export DB_URL="jdbc:mysql://localhost:3306/cyber_shield?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
     export DB_USER="your_mysql_username"
     export DB_PASSWORD="your_mysql_password"
     mvn clean javafx:run
     ```
   - **Or edit the defaults directly** in `/src/main/java/com/cybershield/database/DatabaseConnection.java`:
     ```java
     private static final String USER = resolveConfig("DB_USER", "db.user", "your_mysql_username");
     private static final String PASSWORD = resolveConfig("DB_PASSWORD", "db.password", "your_mysql_password");
     ```

If MySQL is not running at all, the game still works — it drops into an offline fallback mode with an in-memory scoreboard so you can play without a database.

---

## 🚀 How to Run the App

### Option A: From Terminal (Maven CLI)

Navigate to the project root directory containing `pom.xml` and run the following command:

```bash
mvn clean javafx:run
```

### Option B: In IntelliJ IDEA / Eclipse

1. Select **File > Open** and choose the `cyber-shield` root folder containing `pom.xml`.
2. Let Maven import the dependencies (`javafx-controls`, `javafx-fxml`, `mysql-connector-j`).
3. Locate `src/main/java/com/cybershield/view/GameApp.java`.
4. Right-click `GameApp.java` and choose **Run 'GameApp.main()'**.

---

## 🎮 Gameplay & Levels

### 👤 Login Screen
Enter your username. The app checks if your MySQL database is active:
- **Connected**: Player credentials will be saved inside the `users` table.
- **Offline Fallback**: If MySQL is offline, the game notifies you and transitions into a safe offline mode using in-memory state tracking.

### 🏠 Agent Dashboard
Start a fresh mission challenge, view the **Global High Scores**, or safely terminate your session.

### 🛡️ Challenge Levels (20 points each)
1. **Level 1: Phishing Emails**: Analyze a critical alert template and determine if it is a trap or a real communication.
2. **Level 2: Password Strengths**: Contrast dictionary phrases and character complexes to identify secure passphrases.
3. **Level 3: Typosquatting & URL spoofing**: Inspect subdomains and HTTPS security to find genuine banking/utility urls.
4. **Level 4: Public Wi-Fi Encryptions**: Formulate secure remote work configurations with WPA3 and VPN structures.
5. **Level 5: USB Drop Vulnerabilities**: Solve physical social engineering baits like found drives in company lots.

### 🏆 Mission Report (Badge levels)
- **Score 0 – 40**: Beginner Rank
- **Score 41 – 80**: Intermediate Rank
- **Score 81 – 100**: **Cyber Hero** Rank (Inserts high scores dynamically into the database)
