/*
 * U1 L4 — THE STATUS LINE · STARTER CODE
 * 7184 Software Development · Unit 1, Lesson 4
 *
 * ALREADY HERE:  Lessons 1–3 finished — stat block, combat arithmetic, the
 *                accuracy fixes, the enrage timer, the crit cast.
 * YOU'RE ADDING: printf, String methods, a health bar, and a title screen.
 *
 *     javac Main.java
 *     java Main
 *
 * Today is the day the output stops looking terrible. Everything you have been
 * told to ignore for three lessons — the crooked columns, the 42.857142857142854 —
 * gets fixed with printf.
 */
public class Main {


    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;


    public static void main(String[] args) {

        String title = """
                     ========================
                            THE ARENA
                     ========================
                    """;
        System.out.print(title);

        System.out.println("The arena awaits.");
        System.out.println("Sand, torchlight, and a crowd that has already decided how this ends.");
        System.out.println("The gate opens.");
        System.out.println("");


        String playerName = "Nameless";
        int health = MAX_HEALTH;
        int gold = STARTING_GOLD;
        int level = 1;
        boolean alive = true;
        double critChance = 0.15;


        String enemyName = "Cave Goblin";
        int enemyHealth = 40;
        int enemyPower = 7;


        // ---------- 1 · a status line that lines up ----------
        System.out.printf("%-12s HP %3d/%3d Gold %4d Lv %d%n", 
        playerName, health, MAX_HEALTH, gold, level);
        System.out.printf("Alive %-5b Crit %.0f%%%n", alive,  critChance * 100);
        System.out.println("");

        System.out.println("Opponent: " + enemyName);
        System.out.println("Health: " + enemyHealth);
        System.out.println("Power: " + enemyPower);
        System.out.println("");
        enemyName.toUpperCase();
        enemyName.length();
        enemyName.contains("Dragon");
        enemyName.equalsIgnoreCase("cave goblin");
        System.out.println(enemyName.toUpperCase() + " blocks your path!");
        System.out.println("Name length: " + enemyName.length());
        boolean isBoss = enemyName.contains("Dragon");


        int damage = enemyPower * 2;
        health -= damage;
        System.out.println("You take " + damage + " damage. Health: " + health);


        int potion = 15;
        health += potion;
        level++;
        System.out.println("You drink a potion. Health: " + health);
        System.out.println("You reach level " + level + ".");
        System.out.println("");


        int hits = 3;
        int swings = 7;
        int brokenAccuracy = hits / swings * 100;
        System.out.println("Accuracy (broken): " + brokenAccuracy + "%");


        double acc1 = (double) hits / swings * 100;
        double acc2 = hits * 100.0 / swings;

        System.out.printf("Accuracy (cast): %.1f%%%n", acc1);
        System.out.printf("Accuracy (reorder): %.1f%%%n", acc2);
        System.out.println("");


        int turn = 6;
        boolean enrages = (turn % 3 == 0);
        System.out.println("Turn " + turn + " — enrages: " + enrages);
        System.out.println("");


        double critDamage = damage * 1.75;
        int applied = (int) critDamage;
        System.out.println("Crit damage (double): " + critDamage);
        System.out.println("Crit damage (int):    " + applied);
        System.out.println("Lost to the cast:     " + (critDamage - applied));
        System.out.println("");


        int bars = health / 5;
        String bar = "#".repeat(bars) + "-".repeat(20 - bars);
        System.out.printf("[%s] %d%%%n", bar, health);

    }
}