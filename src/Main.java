import java.util.Scanner;

public class Main {

    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;

    public static void main(String[] args) {

        /*
         * TODO 1: PSEUDOCODE — the design, in English, before any Java.
         *
         * Write out what the program will ASK for and what it will DO with
         * each answer. Use plain words in capitals for the decisions:
         * ASK, IF, SET, SHOW, REPEAT UNTIL.
         *
         * Mine looks like this — yours should match YOUR game:
         *
         * ASK for the player's name
         * IF the name is blank
         * USE "Warrior" instead
         * ASK for difficulty 1-3
         * SET enemy health based on difficulty
         * SHOW a summary and wait for Enter
         *
         * Leave it here as a comment when you're done. It is part of what
         * you turn in, and in six weeks it is how you'll remember what this
         * file was supposed to do.
         */

        Scanner in = new Scanner(System.in);

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
            playerName = "Warrior";
        }
        in.nextLine();

        System.out.print("Difficulty (1 = easy, 2 = normal, 3 = brutal): ");
        int difficulty = in.nextInt();
        in.nextLine(); // consume the leftover newline

        int enemyHealth = 30 + difficulty * 15;
        int enemyPower = 4 + difficulty * 3;
        String enemyName = "Cave Goblin";

        int health = MAX_HEALTH;
        int gold = STARTING_GOLD;
        int level = 1;
        boolean alive = true;
        double critChance = 0.15;

        System.out.printf("%-12s HP %3d/%3d  Gold %4d  Lv %d%n",
                playerName, health, MAX_HEALTH, gold, level);
        System.out.printf("Alive %-5b  Crit %.0f%%%n", alive, critChance * 100);
        System.out.println("");

        System.out.printf("%s enters the arena. The %s has %d HP.%n",
                playerName, enemyName, enemyHealth);
        System.out.print("Press Enter to begin...");
        in.nextLine();

        System.out.println(enemyName.toUpperCase() + " blocks your path!");
        System.out.printf("Opponent %-14s HP %3d  Power %2d%n",
                enemyName, enemyHealth, enemyPower);
        System.out.println("Name length: " + enemyName.length());

        boolean isBoss = enemyName.contains("Dragon");
        System.out.println("Boss fight: " + isBoss);

        if (enemyName.equalsIgnoreCase("cave goblin")) {
            System.out.println("You have fought one of these before.");
        }
        System.out.println("");

        int enemydamage = enemyPower * 2;
        health -= enemydamage;
        System.out.println("You take " + enemydamage + " damage. Health: " + health);

        int potion = 15;
        health += potion;
        level++;
        System.out.println("You drink a potion. Health: " + health);
        System.out.println("You reach level " + level + ".");
        System.out.println("");

        int hits = 3;
        int swings = 7;
        double acc1 = (double) hits / swings * 100;
        System.out.printf("Accuracy: %.1f%%%n", acc1);
        System.out.println("");

        int turn = 6;
        boolean enrages = (turn % 3 == 0);
        System.out.println("Turn " + turn + " — enrages: " + enrages);
        System.out.println("");

        int bars = health / 5;
        String bar = "#".repeat(bars) + "-".repeat(20 - bars);
        System.out.printf("[%s] %d%%%n", bar, health);

        Integer weaponDura = 30;
        Integer weaponDmg = 60;

        System.out.print("Choose your Weapon: ");
        String Weapon = in.nextLine().trim();
        if (Weapon.isEmpty()) {
            Weapon = "Sword";
        }

        System.out.println(Weapon + " Durability: " + weaponDura);
        System.out.println(Weapon + " Damage: " + weaponDmg);
        in.close();
        System.out.println();

        int roll = 7; // becomes random in L12

        int damage;

        if (roll >= 9) {
            damage = enemyPower * 2;
            System.out.println("CRITICAL HIT!");
        } else if (roll >= 3) {
            damage = enemyPower;
            System.out.println("A solid hit.");
        } else {
            damage = 0;
            System.out.println("You miss.");
        }

        enemyHealth -= damage;

        if (enemyHealth <= 0) {
            System.out.println("The " + enemyName + " falls!");
            alive = true;
        } else if (health <= 0) {
            System.out.println("You have fallen.");
            alive = false;
        }

        if (health > MAX_HEALTH) {
            health = MAX_HEALTH;
        } else if (health < 0) {
            health = 0;
        }

        if (health < MAX_HEALTH / 4 && gold >= 10) {
            System.out.println("You should buy a potion.");
        }
    }
}