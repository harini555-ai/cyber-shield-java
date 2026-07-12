-- Cyber Shield – Cybersecurity Awareness Game
-- Database creation and tables setup

CREATE DATABASE IF NOT EXISTS cyber_shield;
USE cyber_shield;

-- Table to store user details
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table to store game scores
CREATE TABLE IF NOT EXISTS scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    score INT NOT NULL,
    badge VARCHAR(30) NOT NULL,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Pre-seed some default high scores for testing or scoreboard visuals
INSERT INTO users (username) VALUES ('AdminCyber'), ('NetGuardian'), ('ByteSlayer')
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO scores (username, score, badge) VALUES 
('AdminCyber', 100, 'Cyber Hero'),
('NetGuardian', 80, 'Intermediate'),
('ByteSlayer', 40, 'Beginner')
ON DUPLICATE KEY UPDATE score=score;
