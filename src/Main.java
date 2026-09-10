import java.util.Scanner;

public class Main {

    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;
    static final int ROWS = 5;
    static final int COLS = 11;


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        printTitle();
        String playerName = readName(in);
        int difficulty = readChoice(in, 1, 3, "Difficulty (1 = easy, 2 = normal, 3 = brutal)");
        System.out.println("Difficulty: " + difficultyName(difficulty));
        System.out.println("");

        int health = MAX_HEALTH, potions = 2, playerCol = 1;
        final int playerRow = 2, enemyRow = 2, enemyCol = 9;
        String enemyName = "Cave Goblin";
        int enemyHealth = 30 + difficulty * 15;
        int enemyPower = 4 + difficulty * 3;

        openingCeremony(in, playerName, health, enemyName, enemyHealth);

        int turnNumber = 1;
        boolean playing = true, fled = false;

        while (playing) {
            drawTurn(turnNumber, playerName, health, enemyName, enemyHealth,
                     playerRow, playerCol, enemyRow, enemyCol);

            boolean adjacent = isAdjacent(playerRow, playerCol, enemyRow, enemyCol);
            int roll = (turnNumber * 3) % 10 + 1;
            String action = readAction(in, adjacent, enemyName);
            int damage = 0;

            switch (action) {
                case "A" -> damage = attack(adjacent, enemyPower, roll);
                case "L" -> playerCol = moveLeft(playerCol);
                case "R" -> playerCol = moveRight(playerCol, enemyCol, enemyName);
                case "D" -> health = defend(health);
                case "P" -> {
                    if (potions > 0) { potions--; health = drinkPotion(health); }
                    else System.out.println("You reach for a potion. There are none.");
                }
                case "F" -> fled = flee();
                default -> System.out.println("The crowd jeers. You hesitate and lose the turn.");
            }

            enemyHealth = applyDamage(enemyHealth, damage);
            health = enemyResponse(fled, adjacent, health, enemyHealth, enemyPower, enemyName);
            printHealthBar(health);

            playing = !endOfFight(fled, health, enemyHealth, enemyName, turnNumber);
            turnNumber++;
        }

        System.out.printf("%nThe arena empties after %d turns.%n", turnNumber - 1);
    }


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
        printStatus(name, hp, MAX_HEALTH, STARTING_GOLD, 1);
        System.out.printf("%s enters the arena. The %s has %d HP.%n", name, enemy, enemyHp);
        System.out.print("Press Enter to begin...");
        in.nextLine();
        System.out.println("");
        countdown(3);
        System.out.println("");
    }

    static void drawTurn(int turnNumber, String name, int hp, String enemy, int enemyHp,
                         int playerRow, int playerCol, int enemyRow, int enemyCol) {
        printBanner("Turn " + turnNumber);
        printFighters(name, hp, enemy, enemyHp);
        drawArena(playerRow, playerCol, enemyRow, enemyCol);
    }

    static void printBanner(String text) {
        System.out.println("=".repeat(40));
        System.out.printf("  %s%n", text);
    }

    static void printStatus(String name, int hp, int maxHp, int gold, int level) {
        System.out.printf("%-12s HP %3d/%3d  Gold %4d  Lv %d%n", name, hp, maxHp, gold, level);
        System.out.println("");
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
        for (int i = from; i > 0; i--) {
            System.out.println(i + "...");
        }
        System.out.println("FIGHT!");
    }


    static void drawArena(int playerRow, int playerCol, int enemyRow, int enemyCol) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (r == playerRow && c == playerCol)      System.out.print('@');
                else if (r == enemyRow && c == enemyCol)   System.out.print('X');
                else if (r == 0 || r == ROWS - 1)          System.out.print('-');
                else if (c == 0 || c == COLS - 1)          System.out.print('|');
                else                                       System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println("");
    }


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
        if (roll >= 9) return power * 2;
        if (roll >= 3) return power;
        return 0;
    }

    static int calculateDamage(int power, int roll, double critMultiplier) {
        if (roll >= 9) return (int) (power * critMultiplier);
        if (roll >= 3) return power;
        return 0;
    }

    static int applyDamage(int hp, int damage) {
        return hp - damage;
    }

    static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    static int attack(boolean adjacent, int enemyPower, int roll) {
        if (!adjacent) {
            System.out.println("You swing at empty air. Get closer first.");
            return 0;
        }
        int damage = calculateDamage(enemyPower, roll);
        if (damage == 0)                   System.out.println("You miss.");
        else if (damage > enemyPower)      System.out.println("CRITICAL HIT!");
        else                               System.out.println("A solid hit.");
        return damage;
    }

    static int moveLeft(int playerCol) {
        if (playerCol - 1 < 1) {
            System.out.println("The wall stops you.");
            return playerCol;
        }
        System.out.println("You step left.");
        return playerCol - 1;
    }

    static int moveRight(int playerCol, int enemyCol, String enemyName) {
        if (playerCol + 1 > COLS - 2) {
            System.out.println("The wall stops you.");
            return playerCol;
        }
        if (playerCol + 1 == enemyCol) {
            System.out.println("The " + enemyName + " blocks your way.");
            return playerCol;
        }
        System.out.println("You step right.");
        return playerCol + 1;
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