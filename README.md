# 🛡️ Cyber Shield - Cybersecurity Awareness Game

Cyber Shield is an interactive cybersecurity awareness game developed using JavaFX and MySQL.  
The game provides an engaging way for users to learn cybersecurity concepts through challenge-based gameplay.

## 🎮 Features

- Player login system
- Cybersecurity challenge-based gameplay
- Score tracking system
- Restart game option
- Global high score leaderboard
- MySQL database integration
- Persistent player score storage
- User and score management

## 🛠️ Technologies Used

- Java
- JavaFX
- JDBC
- MySQL
- Maven

## 📂 Project Structure

```text
cyber-shield-java
│
├── src/main/java/com/cybershield
│ ├── controller
│ │ └── GameController.java
│ │
│ ├── database
│ │ └── DatabaseConnection.java
│ │
│ ├── model
│ │ ├── Challenge.java
│ │ └── Player.java
│ │
│ ├── utils
│ │ └── GameData.java
│ │
│ └── view
│ └── GameApp.java
│
├── cyber_shield.sql
├── pom.xml
└── README.md


## 🗄️ Database

The application uses MySQL for storing player information and game scores.

Database:


Tables:

- `users` - Stores player details
- `scores` - Stores player scores and high score records

The global leaderboard displays the highest scores of all players.

## ▶️ How to Run

### Prerequisites

- Java JDK installed
- MySQL Server installed
- Maven installed

### Steps

1. Clone the repository

```bash
git clone https://github.com/harini555-ai/cyber-shield-java.git

cd cyber-shield-java

mvn clean javafx:run


🎯 Gameplay
Enter player credentials
Complete cybersecurity challenges
Earn scores based on performance
View global high score rankings
Restart or exit the game

🚀 Future Enhancements
More cybersecurity challenge levels
Improved animations
Additional game modes

👩‍💻 Author
Harini M
