public class App {

    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;

    public static void main(String[] args) throws Exception {
        String playerName = "Frodo";
        int health = MAX_HEALTH;
        boolean alive = true;
        double critChance = 0.15;
        int gold = STARTING_GOLD;
        int level = 1;
        
        String enemyName = "Shark";
        int enemyHealth = 50;
        int enemyPower = 10;

        System.out.println("Fighter: " + playerName);
        System.out.println("Health: " + health + " / " + MAX_HEALTH);
        System.out.println("Gold: " + gold);
        System.out.println("Level: " + level);
        System.out.println("Alive: " + alive);
        System.out.println("Crit chance:" + critChance);
        System.out.println();
        System.out.println("Opponent: " + enemyName);
        System.out.println("Health: " + enemyHealth);
        System.out.printf("Power: %s%n", enemyPower);
        System.out.println();
        
        int damage = enemyPower * 2;
        health -= damage;
        System.out.println("You take " + damage + " damage. Health: " + health);

         int potion = 15;
        health += potion;
        level++;

        int hits = 3;
        int swings = 7;
        int accuracy = hits / swings * 100;
        System.out.println("Accuracy: " + accuracy + "%");

        double acc1 = (double) hits / swings * 100;
        double acc2 = hits * 100.0 / swings;
        
        System.out.println("You drink a potion. Health: " + health);
        System.out.println("You reach level " + level + ".");
        System.out.println("");

        int turn = 6;
        boolean enrages = (turn % 3 == 0);
        System.out.println("Turn " + turn + " — enrages: " + enrages);
        System.out.println();

        double critDamage = damage * 1.75;
        int applied = (int) critDamage;
        System.out.println("Crit! " + applied + " damage.");
        System.out.println("Crit damage (double): " + critDamage);

    }

}