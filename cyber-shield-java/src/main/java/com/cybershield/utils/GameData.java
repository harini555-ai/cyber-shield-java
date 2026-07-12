package com.cybershield.utils;

import com.cybershield.model.Challenge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class loaded with the core challenge content for the 5 game levels.
 */
public class GameData {
    private static final List<Challenge> challenges = new ArrayList<>();

    static {
        // Level 1: Phishing Email (Safe vs Phishing)
        challenges.add(new Challenge(
                "Level 1: Phishing Email Challenge",
                "You received an urgent email with a suspicious address and an attachment.",
                "From: security-update@secure-bank-login-alert.net\n" +
                        "Subject: URGENT: Your account has been suspended!\n\n" +
                        "Dear Customer,\n" +
                        "We detected suspicious login activity on your banking portal from another country.\n" +
                        "To protect your funds, we have temporarily suspended your account access.\n" +
                        "You must immediately click the link below to verify your identity and restore access.\n" +
                        "Failure to respond within 24 hours will result in permanent closure of your account.\n\n" +
                        "Verify Here: http://secure-bank-login-alert.net/auth-verify\n\n" +
                        "Sincerely,\n" +
                        "Security Team Support",
                Arrays.asList("Safe", "Phishing"),
                1, // 1 is Phishing (Index starts from 0)
                "Correct! This is a phishing email. Indicators include: " +
                        "1) Suspicious sender domain (secure-bank-login-alert.net is not a real bank domain). " +
                        "2) High urgency and fear-mongering (threatening permanent closure within 24 hours). " +
                        "3) Requesting credentials/verification via a non-secure HTTP link.",
                20
        ));

        // Level 2: Password Challenge (Choose strongest)
        challenges.add(new Challenge(
                "Level 2: Password Strength Challenge",
                "Your organization is updating its credential policy. Choose the STRONGEST password from the choices below.",
                "Analyze the following passwords and select the one that resists brute-force and dictionary attacks best.",
                Arrays.asList(
                        "password123", 
                        "Admin@2026", 
                        "correct horse battery staple", 
                        "Tr0ub4dor&3"
                ),
                2, // "correct horse battery staple" is a passphrase, which is generally stronger because of high entropy and length!
                "Correct! 'correct horse battery staple' is the strongest choice. " +
                        "It is a 'passphrase' consisting of multiple unrelated random words. " +
                        "While 'Tr0ub4dor&3' uses special characters and substitution, it is shorter (11 chars) and harder to remember. " +
                        "A long passphrase (28 characters) provides significantly higher entropy and is extremely resistant to modern brute-force algorithms.",
                20
        ));

        // Level 3: Fake Website Detection (Legitimate URL)
        challenges.add(new Challenge(
                "Level 3: Website URL Verifier",
                "You need to pay your electricity bill. Identify the LEGITIMATE official website address.",
                "Review these four URLs carefully. Cybercriminals use typosquatting and fake subdomains to mimic official portals.",
                Arrays.asList(
                        "https://www.official-utility-pay.com.phish.ru/login",
                        "https://support.utility-power-co.net/pay",
                        "https://www.utilitypowercompany.com/billing/pay",
                        "http://www.utilitypowercompany-portal.com/secure"
                ),
                2, // "https://www.utilitypowercompany.com/billing/pay" is the official clean domain
                "Correct! 'https://www.utilitypowercompany.com/billing/pay' is the legitimate URL. " +
                        "Let's break down the tricks:\n" +
                        "- Choice 1 is on a subdomain of phish.ru (the domain is actually phish.ru).\n" +
                        "- Choice 2 uses a slightly altered domain name (utility-power-co.net) and is unverified.\n" +
                        "- Choice 4 uses HTTP (unencrypted) and a hyphenated domain (utilitypowercompany-portal.com), a classic typosquatting technique.",
                20
        ));

        // Level 4: Public Wi-Fi (Safest Network)
        challenges.add(new Challenge(
                "Level 4: Public Wi-Fi Dilemma",
                "You are working from a local coffee shop. Which of these network configurations is the SAFEST for your business laptop?",
                "You need to connect to the internet to review sensitive company financial reports. Choose the safest network setup.",
                Arrays.asList(
                        "Connect to 'CoffeeShop_Free_Wifi' (unsecured, no password required)",
                        "Connect to 'CoffeeShop_Guest_Secure' (requires WPA3 password from barista) and immediately turn on your corporate VPN",
                        "Connect to 'HP_Printer_Direct_33' ad-hoc network",
                        "Set up an open hotspot from your own phone named 'Free Public Wi-Fi' to save data"
                ),
                1, // Connect to CoffeeShop_Guest_Secure + corporate VPN
                "Correct! Connecting to an encrypted network ('CoffeeShop_Guest_Secure' with WPA3) " +
                        "paired with a Virtual Private Network (VPN) is the safest method. " +
                        "The WPA3 password encrypts local airwaves, and the corporate VPN encapsulates all your internet traffic " +
                        "in an encrypted tunnel, protecting you from packet sniffing and Man-in-the-Middle (MitM) attacks.",
                20
        ));

        // Level 5: USB Security (Decide what to do)
        challenges.add(new Challenge(
                "Level 5: The Found USB Drive",
                "You find a sleek USB flash drive in the corporate parking lot labelled 'Q3 Salary & Bonus Adjustments'. What is your course of action?",
                "This drive could contain valuable company spreadsheet drafts, or it could contain malicious payloads.",
                Arrays.asList(
                        "Plug it into your personal laptop first to check if there are any viruses, to protect your work computer",
                        "Plug it into your corporate desktop, open the files immediately, and verify if it belongs to your colleague",
                        "Bring it immediately to the IT Security department or submit it to the system administrator without plugging it in anywhere",
                        "Discard it in the trash bin outside the building so nobody can find it"
                ),
                2, // Bring it immediately to IT security
                "Correct! USB Drop Attacks are a very common physical intrusion tactic. " +
                        "Cybercriminals deliberately drop USB drives loaded with automated malware (like rubber ducky keystroke injectors or Trojan horses) " +
                        "in hopes that an inquisitive employee plugs it in. " +
                        "Never connect unauthorized drives to any device. Submit them directly to the IT Security Desk.",
                20
        ));
    }

    public static List<Challenge> getChallenges() {
        return challenges;
    }

    public static Challenge getChallenge(int levelIndex) {
        if (levelIndex >= 0 && levelIndex < challenges.size()) {
            return challenges.get(levelIndex);
        }
        return null;
    }
}
