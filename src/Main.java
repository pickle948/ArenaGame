import java.util.Scanner;

public class Main {

    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;

    public static void main(String[] args) {

        /*
         * TODO 1: PSEUDOCODE — the design, in English, before any Java.
         *
         *   Write out what the program will ASK for and what it will DO with
         *   each answer. Use plain words in capitals for the decisions:
         *   ASK, IF, SET, SHOW, REPEAT UNTIL.
         *
         *   Mine looks like this — yours should match YOUR game:
         *
         *       ASK for the player's name
         *       IF the name is blank
         *           USE "Warrior" instead
         *       ASK for difficulty 1-3
         *       SET enemy health based on difficulty
         *       SHOW a summary and wait for Enter
         *
         *   Leave it here as a comment when you're done. It is part of what
         *   you turn in, and in six weeks it is how you'll remember what this
         *   file was supposed to do.
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
        if (playerName.isEmpty()){
        playerName = "Warrior";}
        in.nextLine();

        System.out.print("Select your Difficulty: ");
        int difficulty = in.nextInt();
        in.nextLine();

        int health = MAX_HEALTH;
        int gold = STARTING_GOLD;
        int level = 1;
        boolean alive = true;
        double critChance = 0.15;

        String enemyName = "Cave Goblin";
        int enemyHealth = 50 * difficulty;
        int enemyPower = 10 * difficulty;

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

        // TODO 5: design a feature of your own. Pseudocode FIRST, as a comment,
        //         then build it. Ask the player something your game cares about
        //         — a class, a weapon, a starting bonus — and use the answer.

              //*       ASK for the player's weapon
         //*       IF the weapon is blank
         //*           USE "Sword" instead
         //*       SET weapon durability based on difficulty
        // *       SHOW weapon stats

        Integer weaponDura = 30 / difficulty;
        Integer weaponDmg = 60 / difficulty;
        
        System.out.print("Choose your Weapon: ");
        String Weapon = in.nextLine().trim();
        if (Weapon.isEmpty()){
        Weapon = "Sword";}
        
        System.out.println(Weapon);
        System.out.println("Durability: " + weaponDura);
        System.out.println("Damage: " + weaponDmg);
        
        in.close();
    }
}