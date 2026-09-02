/*
 * U1 L9 — FOR LOOPS AND NESTED LOOPS · STARTER CODE
 * 7184 Software Development · Unit 1, Lesson 9
 *
 * ALREADY HERE:  Lessons 1-8 finished — including the game loop, so the fight
 *                already repeats until someone falls.
 * YOU'RE ADDING: for loops. A banner, a countdown, and — the real work — an
 *                arena drawn by a loop inside a loop, that your player walks
 *                around in.
 *
 *     javac Main.java
 *     java Main
 *
 * BEFORE YOU CHANGE ANYTHING: run it. It works. Today is not about fixing
 * something broken — it is about the console finally looking like a game.
 */

import java.util.Scanner;

public class Main {

    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;

    static final int ROWS = 5;
    static final int COLS = 11;

    public static void main(String[] args) {
        /*
         * PSEUDOCODE — the design, before the code (D1.7)
         *
         * ASK for the player's name
         * IF the name is blank
         * USE "Challenger" instead
         * ASK for difficulty 1-3
         * REPEAT UNTIL the answer is 1, 2, or 3 <- L8, done: do-while
         * COUNT DOWN from 3 <- L9, TODO 4
         * DRAW the arena <- L9, TODO 2
         * SHOW the menu and READ one action
         * MOVE, or FIGHT if the enemy is adjacent <- L9, TODO 3
         * REPEAT the whole turn until someone falls <- L8, done: while
         * SHOW a summary and wait for Enter
         */

        Scanner in = new Scanner(System.in);

        // ---------- L4 · text block title screen ----------
        String title = """
                ========================
                     THE ARENA
                ========================
                """;
        System.out.print(title);

        System.out.println("Sand, torchlight, and a crowd that has already decided how this ends.");
        System.out.println("The gate opens.");
        System.out.println("");

        System.out.print("What is your name, challenger? ");
        String playerName = in.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "Challenger";
        }

        // ---------- L8 · validated difficulty ----------
        int difficulty;
        do {
            System.out.print("Difficulty (1 = easy, 2 = normal, 3 = brutal): ");
            while (!in.hasNextInt()) {
                System.out.print("Numbers only. Try again: ");
                in.next();
            }
            difficulty = in.nextInt();
        } while (difficulty < 1 || difficulty > 3);
        in.nextLine();

        // ---------- L7 · a switch EXPRESSION ----------
        String difficultyName = switch (difficulty) {
            case 1 -> "Easy";
            case 2 -> "Normal";
            case 3 -> "Brutal";
            default -> "Unknown";
        };
        System.out.println("Difficulty: " + difficultyName);
        System.out.println("");

        int health = MAX_HEALTH;
        int gold = STARTING_GOLD;
        int level = 1;
        int potions = 2;
        boolean alive = true;
        double critChance = 0.15;

        String enemyName = "Cave Goblin";
        int enemyHealth = 30 + difficulty * 15;
        int enemyPower = 4 + difficulty * 3;

        int playerRow = 2, playerCol = 1;
        int enemyRow = 2, enemyCol = 9;

        System.out.printf("%-12s HP %3d/%3d  Gold %4d  Lv %d%n",
                playerName, health, MAX_HEALTH, gold, level);
        System.out.printf("Alive %-5b  Crit %.0f%%%n", alive, critChance * 100);
        System.out.println("");

        System.out.printf("%s enters the arena. The %s has %d HP.%n",
                playerName, enemyName, enemyHealth);
        System.out.print("Press Enter to begin...");
        in.nextLine();
        System.out.println("");

        for (int i = 3; i > 0; i--) {
            System.out.println(i + "...");
        }

        System.out.println(enemyName.toUpperCase() + " blocks your path!");
        System.out.printf("Opponent %-14s HP %3d  Power %2d%n",
                enemyName, enemyHealth, enemyPower);
        System.out.println("");

        // ================= L8 · THE GAME LOOP =================
        int turnNumber = 1;
        boolean playing = true;

        while (playing) {

            for (int i = 0; i < 40; i++) {
                System.out.print("=");
            }
            System.out.println();

            System.out.printf("  Turn %d%n", turnNumber);
            System.out.printf("%-12s HP %3d/%3d    %-14s HP %3d%n",
                    playerName, health, MAX_HEALTH, enemyName, enemyHealth);
            System.out.println("");

            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                }
                System.out.println();
            }

            boolean adjacent = (playerRow == enemyRow)
                    && (Math.abs(playerCol - enemyCol) == 1);

            int roll = (turnNumber * 3) % 10 + 1;
            int damage = 0;

            System.out.print("[A]ttack  [D]efend  [P]otion  [F]lee: ");
            String action = in.nextLine().trim().toUpperCase();

            switch (action) {
                case "A" -> {
                    if (roll >= 9) {
                        damage = enemyPower * 2;
                        System.out.println("CRITICAL HIT!");
                    } else if (roll >= 3) {
                        damage = enemyPower;
                        System.out.println("A solid hit.");
                    } else {
                        System.out.println("You miss.");
                    }
                }
                case "L" -> {
                    if (playerCol - 1 < 1) {
                        System.out.println("The wall stops you.");
                    } else {
                        playerCol--;
                        System.out.println("You step left.");
                    }
                }
                case "R" -> {
                    if (playerCol + 1 > COLS - 2) {
                        System.out.println("The wall stops you.");
                    } else if (playerCol + 1 == enemyCol) {
                        System.out.println("The " + enemyName + " blocks your way.");
                    } else {
                        playerCol++;
                        System.out.println("You step right.");
                    }
                }
                case "D" -> {
                    health += 5;
                    System.out.println("You raise your guard and recover 5 HP.");
                }
                case "P" -> {
                    if (potions > 0) {
                        potions--;
                        health += 25;
                        System.out.println("You drink a potion and recover 25 HP.");
                    } else {
                        System.out.println("You reach for a potion. There are none.");
                    }
                }
                case "F" -> {
                    alive = true;
                    System.out.println("You run for the gate. The crowd howls.");
                }
                default -> System.out.println("The crowd jeers. You hesitate and lose the turn.");
            }


            enemyHealth -= damage;

            if (alive && enemyHealth > 0) {
                health -= enemyPower;
                System.out.printf("The %s strikes back for %d.%n", enemyName, enemyPower);
            }

            // ---------- L6 · the clamp ----------
            if (health > MAX_HEALTH) {
                health = MAX_HEALTH;
            } else if (health < 0) {
                health = 0;
            }

            // ---------- L4 · the health bar ----------
            int bars = health / 5;
            String bar = "#".repeat(bars) + "-".repeat(20 - bars);
            System.out.printf("[%s] %d%%%n", bar, health);

            // ---------- the three ways this loop ends ----------
            if (!alive) {
                System.out.println("You escape with your life, and nothing else.");
                playing = false;
            } else if (enemyHealth <= 0) {
                System.out.printf("%nThe %s falls! You win on turn %d.%n", enemyName, turnNumber);
                playing = false;
            } else if (health <= 0) {
                System.out.printf("%nYou have fallen on turn %d.%n", turnNumber);
                alive = false;
                playing = false;
            }

            turnNumber++;
        }

        System.out.printf("%nThe arena empties after %d turns.%n", turnNumber - 1);
    }
}