/*
 * U1 L12 — RANDOM, MATH, DEBUGGING, DOCUMENTATION · STARTER CODE
 * 7184 Software Development · Unit 1, Lesson 12 (two days)
 *
 * START FROM YOUR OWN LESSON 11 FILE. The code below is the L11 solution, for
 * anyone who lost theirs. The TODO markers are the same either way.
 *
 *     javac Main.java
 *     java Main
 *
 * DAY 1 — five TODOs, marked in the code below. In order. Compile after each.
 *
 *   TODO 1  clamp       Math.max / Math.min already does it: read it, then TEST it
 *   TODO 2  Random      one Random, made once, replaces the fake roll        (replace)
 *   TODO 3  loot        rollLoot + awardLoot, DESIGNED ON PAPER FIRST        (add)
 *   TODO 4  prove it    verifyLootTable rolls it 1000 times                  (add)
 *   TODO 5  variance    damage wobbles by -2..+2, never below 0              (edit attack)
 *
 * PAPER FIRST, for TODO 3. Four steps, written down before the editor opens:
 *   RESTATE the problem · DECOMPOSE it into steps · SEQUENCE them · TEST cases
 * The teacher asks for the paper before looking at the screen.
 *
 * DAY 2 — no new markers in this file:
 *   BuggyArena.java   three bugs; fix one at a time; write down HOW you found each
 *   a breakpoint      in your own game loop; step one full turn in the Variables panel
 *   TODO 6  Javadoc   type /** and Enter above every method from L10-L12
 *   README.md         what it is, how to run it, the controls, one known issue
 *
 * BEFORE YOU LEAVE: remove any fixed seed (a seeded game plays the same every
 * time and looks broken), back up as Arena_U1L12_LastnameF, and submit.
 * Day 2's backup is Project 1 checkpoint 2: Arena_P1_CP2_LastnameF.
 */

import java.util.Scanner;
import java.util.Random;

public class Main {

    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;
    static final int ROWS = 5;
    static final int COLS = 11;
    static final int PACK_SLOTS = 5;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Random rng = new Random();

        printTitle();
        String playerName = readName(in);
        int difficulty = readChoice(in, 1, 3, "Difficulty (1 = easy, 2 = normal, 3 = brutal)");
        System.out.println("Difficulty: " + difficultyName(difficulty));
        System.out.println("");

        int health = MAX_HEALTH, gold = STARTING_GOLD, playerCol = 1;
        final int playerRow = 2, enemyRow = 2, enemyCol = 9;
        int level = 1;

        String enemyName = "Cave Goblin";
        int enemyHealth = 30 + difficulty * 15;
        int enemyPower = 4 + difficulty * 3;

        // ---------- PARALLEL ARRAYS ----------
        // Two arrays that must be kept in step BY HAND. Sort one without the
        // other and every count belongs to the wrong item. That failure mode is
        // the argument for objects in U2 — do not hide it.
        String[] itemNames = new String[PACK_SLOTS];
        int[] itemCounts = new int[PACK_SLOTS];
        int itemSlots = 0;

        itemNames[0] = "Potion";
        itemCounts[0] = 2;
        itemSlots++;
        itemNames[1] = "Bomb";
        itemCounts[1] = 1;
        itemSlots++;

        // int[] starts full of 0. String[] starts full of null — which is why
        // touching an unassigned slot throws NullPointerException.
        int[] damageLog = new int[20];
        int loggedTurns = 0;

        // ---------- THE BOARD ----------
        char[][] arena = newArena();
        arena[playerRow][playerCol] = '@';
        arena[enemyRow][enemyCol] = 'X';
        arena[3][4] = '^'; // hazard — one line, because the grid stores the world
        arena[1][7] = '$'; // treasure

        openingCeremony(in, playerName, health, enemyName, enemyHealth);

        int turnNumber = 1;
        boolean playing = true, fled = false;

        while (playing) {
            printBanner("Turn " + turnNumber);
            printFighters(playerName, health, enemyName, enemyHealth);
            drawArena(arena);
            printInventory(itemNames, itemCounts, itemSlots);

            boolean adjacent = isAdjacent(playerRow, playerCol, enemyRow, enemyCol);
            int roll = rng.nextInt(1, 11);
            String action = readAction(in, adjacent, enemyName);
            int damage = 0;

            switch (action) {
                case "A" -> damage = attack(adjacent, enemyPower, roll, rng);
                case "L", "R" -> {
                    int target = playerCol + (action.equals("L") ? -1 : 1);
                    char cell = peek(arena, playerRow, target);
                    if (cell == '#' || cell == 'X') {
                        System.out.println(cell == '#' ? "The wall stops you."
                                : "The " + enemyName + " blocks your way.");
                    } else {
                        // Three steps, every time: clear, update, set.
                        // Miss the clear and the player leaves a trail of '@'.
                        arena[playerRow][playerCol] = ' ';
                        if (cell == '^') {
                            health = applyDamage(health, 8);
                            System.out.println("You step on a spike trap. 8 damage.");
                        } else if (cell == '$') {
                            gold += 15;
                            System.out.println("You scoop up 15 gold.");
                        } else {
                            System.out.println(action.equals("L") ? "You step left." : "You step right.");
                        }
                        playerCol = target;
                        arena[playerRow][playerCol] = '@';
                    }
                }
                case "D" -> health = defend(health);
                case "P" -> {
                    int slot = findItem(itemNames, itemSlots, "Potion");
                    if (slot >= 0 && itemCounts[slot] > 0) {
                        itemCounts[slot]--;
                        health = drinkPotion(health);
                    } else {
                        System.out.println("You reach for a potion. There are none.");
                    }
                }
                case "F" -> fled = flee();
                default -> System.out.println("The crowd jeers. You hesitate and lose the turn.");
            }

            enemyHealth = applyDamage(enemyHealth, damage);
            if (loggedTurns < damageLog.length) {
                damageLog[loggedTurns++] = damage; // < length, never <=
            }

            health = enemyResponse(fled, adjacent, health, enemyHealth, enemyPower, enemyName);
            printHealthBar(health);

            boolean over = endOfFight(fled, health, enemyHealth, enemyName, turnNumber);
            if (over && !fled && !isAlive(enemyHealth)) {
                String loot = rollLoot(rng);
                if (loot.equals("Relic")) {
                    level++;
                    System.out.println("The relic hums. You reach level " + level + ".");
                }
                itemSlots = awardLoot(loot, itemNames, itemCounts, itemSlots);
            }
            playing = !over;

            playing = !endOfFight(fled, health, enemyHealth, enemyName, turnNumber);
            turnNumber++;
        }

        System.out.printf("%nGold: %d%n", gold);
        System.out.printf("Average damage per turn: %.1f%n", average(damageLog, loggedTurns));
        System.out.printf("The arena empties after %d turns.%n", turnNumber - 1);
        verifyLootTable(rng, 1000);
    }

    // ================= loot (new today) =================
    //
    // SPEC: when an enemy dies, roll ONE item: 50% Potion, 30% Coin Pouch,
    // 15% Shield, 5% Relic. Print what dropped. Add it to the pack if there is
    // room; if the pack is full, print "Your pack is full!" and drop nothing.
    static String rollLoot(Random rng) {
        int roll = rng.nextInt(100);
        if (roll < 50)
            return "Potion";
        if (roll < 80)
            return "Coin Pouch";
        if (roll < 95)
            return "Shield";
        return "Relic";
    }

    static int awardLoot(String loot, String[] names, int[] counts, int slots) {
        System.out.printf("The %s drops!%n", loot);
        int existing = findItem(names, slots, loot);
        if (existing >= 0) {
            counts[existing]++;
            return slots;
        }
        if (slots >= names.length) {
            System.out.println("Your pack is full!");
            return slots;
        }
        names[slots] = loot;
        counts[slots] = 1;
        return slots + 1;
    }

    static void verifyLootTable(Random rng, int rolls) {
        int[] counts = new int[4];
        for (int i = 0; i < rolls; i++) {
            String loot = rollLoot(rng);
            if (loot.equals("Potion"))
                counts[0]++;
            else if (loot.equals("Coin Pouch"))
                counts[1]++;
            else if (loot.equals("Shield"))
                counts[2]++;
            else
                counts[3]++;
        }
        String[] labels = { "Potion", "Coin Pouch", "Shield", "Relic" };
        System.out.printf("%n-- Loot table over %d rolls --%n", rolls);
        for (int i = 0; i < labels.length; i++) {
            System.out.printf(" %-11s %5.1f%%%n", labels[i], counts[i] * 100.0 / rolls);
        }
    }

    // ================= arrays =================

    static char[][] newArena() {
        char[][] arena = new char[ROWS][COLS];
        // arena.length is the number of ROWS.
        // arena[r].length is the number of COLUMNS in row r.
        for (int r = 0; r < arena.length; r++) {
            for (int c = 0; c < arena[r].length; c++) {
                boolean edge = (r == 0 || r == arena.length - 1
                        || c == 0 || c == arena[r].length - 1);
                arena[r][c] = edge ? '#' : ' ';
            }
        }
        return arena;
    }

    // A 2D array is an ARRAY OF ROWS, so the enhanced for hands you each row.
    static void drawArena(char[][] arena) {
        for (char[] row : arena) {
            System.out.println(new String(row));
        }
        System.out.println("");
    }

    // Reading the cell you are about to enter IS collision detection. U3 does
    // exactly this with sprites.
    static char peek(char[][] arena, int row, int col) {
        if (row < 0 || row >= arena.length)
            return '#';
        if (col < 0 || col >= arena[row].length)
            return '#';
        return arena[row][col];
    }

    static void printInventory(String[] names, int[] counts, int slots) {
        System.out.println("-- Pack --");
        for (int i = 0; i < slots; i++) {
            System.out.printf("  %d) %-10s x%d%n", i + 1, names[i], counts[i]);
        }
        System.out.println("");
    }

    // Loops to `slots`, not to names.length — the tail of the array is still
    // null, and calling anything on null throws.
    static int findItem(String[] names, int slots, String wanted) {
        for (int i = 0; i < slots; i++) {
            if (names[i] != null && names[i].equals(wanted))
                return i;
        }
        return -1;
    }

    static double average(int[] log, int used) {
        if (used == 0)
            return 0.0;
        int total = 0;
        for (int i = 0; i < used; i++) {
            total += log[i];
        }
        return (double) total / used; // the L3 cast, still earning its keep
    }

    // ================= output =================

    static void printTitle() {
        System.out.print("""
                ========================
                     THE ARENA
                ========================
                """);
        System.out.println("Sand, torchlight, and a crowd that has already decided how this ends.");
        System.out.println("The gate opens.");
        System.out.println("");
    }

    static void openingCeremony(Scanner in, String name, int hp, String enemy, int enemyHp) {
        System.out.printf("%-12s HP %3d/%3d%n", name, hp, MAX_HEALTH);
        System.out.printf("%s enters the arena. The %s has %d HP.%n", name, enemy, enemyHp);
        System.out.print("Press Enter to begin...");
        in.nextLine();
        System.out.println("");
        countdown(3);
        System.out.println("");
    }

    static void printBanner(String text) {
        System.out.println("=".repeat(40));
        System.out.printf("  %s%n", text);
    }

    static void printFighters(String name, int hp, String enemy, int enemyHp) {
        System.out.printf("%-12s HP %3d/%3d    %-14s HP %3d%n", name, hp, MAX_HEALTH, enemy, enemyHp);
        System.out.println("");
    }

    static void printHealthBar(int hp) {
        int bars = hp / 5;
        System.out.printf("[%s] %d%%%n", "#".repeat(bars) + "-".repeat(20 - bars), hp);
    }

    static void countdown(int from) {
        for (int i = from; i > 0; i--)
            System.out.println(i + "...");
        System.out.println("FIGHT!");
    }

    // ================= input =================

    static String readName(Scanner in) {
        System.out.print("What is your name, challenger? ");
        String name = in.nextLine().trim();
        return name.isEmpty() ? "Challenger" : name;
    }

    static int readChoice(Scanner in, int min, int max, String prompt) {
        int choice;
        do {
            System.out.printf("%s: ", prompt);
            while (!in.hasNextInt()) {
                in.next();
                System.out.printf("Numbers only. %s: ", prompt);
            }
            choice = in.nextInt();
            in.nextLine();
        } while (choice < min || choice > max);
        return choice;
    }

    static String readAction(Scanner in, boolean adjacent, String enemyName) {
        if (adjacent) {
            System.out.print("[A]ttack  [D]efend  [P]otion  [L]eft  [R]ight  [F]lee: ");
        } else {
            System.out.print("The " + enemyName + " is out of reach.  "
                    + "[L]eft  [R]ight  [D]efend  [P]otion  [F]lee: ");
        }
        return in.nextLine().trim().toUpperCase();
    }

    // ================= values =================

    static String difficultyName(int difficulty) {
        return switch (difficulty) {
            case 1 -> "Easy";
            case 2 -> "Normal";
            case 3 -> "Brutal";
            default -> "Unknown";
        };
    }

    static boolean isAlive(int hp) {
        return hp > 0;
    }

    static boolean isAdjacent(int r1, int c1, int r2, int c2) {
        return r1 == r2 && Math.abs(c1 - c2) == 1;
    }

    static int calculateDamage(int power, int roll) {
        if (roll >= 9)
            return power * 2;
        if (roll >= 3)
            return power;
        return 0;
    }

    static int applyDamage(int hp, int damage) {
        return hp - damage;
    }

    static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    static int attack(boolean adjacent, int enemyPower, int roll, Random rng) {
        if (!adjacent) {
            System.out.println("You swing at empty air. Get closer first.");
            return 0;
        }
        int damage = calculateDamage(enemyPower, roll);
        if (damage == 0)
            System.out.println("You miss.");
        else if (damage > enemyPower)
            System.out.println("CRITICAL HIT!");
        else
            System.out.println("A solid hit.");
        return damage;
    }

    static int defend(int health) {
        System.out.println("You raise your guard and recover 5 HP.");
        return health + 5;
    }

    static int drinkPotion(int health) {
        System.out.println("You drink a potion and recover 25 HP.");
        return health + 25;
    }

    static boolean flee() {
        System.out.println("You run for the gate. The crowd howls.");
        return true;
    }

    static int enemyResponse(boolean fled, boolean adjacent, int health,
            int enemyHealth, int enemyPower, String enemyName) {
        if (!fled && isAlive(enemyHealth) && adjacent) {
            health = applyDamage(health, enemyPower);
            System.out.printf("The %s strikes back for %d.%n", enemyName, enemyPower);
        }
        return clamp(health, 0, MAX_HEALTH);
    }

    static boolean endOfFight(boolean fled, int health, int enemyHealth,
            String enemyName, int turnNumber) {
        if (fled) {
            System.out.println("You escape with your life, and nothing else.");
            return true;
        }
        if (!isAlive(enemyHealth)) {
            System.out.printf("%nThe %s falls! You win on turn %d.%n", enemyName, turnNumber);
            return true;
        }
        if (!isAlive(health)) {
            System.out.printf("%nYou have fallen on turn %d.%n", turnNumber);
            return true;
        }
        return false;
    }
}