import java.util.*;

/**
 * DUNGEON DUEL - a tiny turn-based console game that demonstrates every core OOP concept.
 *
 * Run:  javac DungeonGame.java && java DungeonGame     (or simply: java DungeonGame.java)
 *
 * OOP concepts are tagged in comments with  [CONCEPT].
 * Concepts covered: Class/Object, Encapsulation, Abstraction (abstract class + interface),
 * Inheritance, Polymorphism (overriding + overloading + dynamic dispatch), Constructors,
 * this/super, static, final, Enums, Composition, Generics, Exceptions, Nested/Inner/Anonymous
 * classes, Default interface methods, Access modifiers, Object methods (toString/equals/hashCode).
 */
public class DungeonGame {

    // [CONCEPT] Enum - a fixed set of constants that can carry data and behavior
    enum Element {
        PHYSICAL(1.0), FIRE(1.4), ICE(1.2);

        private final double multiplier;               // [CONCEPT] final field

        Element(double multiplier) { this.multiplier = multiplier; }
        public double getMultiplier() { return multiplier; }
    }

    // [CONCEPT] Custom (checked) exception - inheritance applied to error handling
    static class InvalidActionException extends Exception {
        InvalidActionException(String message) { super(message); }
    }

    // [CONCEPT] Interface - pure abstraction: a contract with no state
    interface Fighter {
        String getName();
        boolean isAlive();
        void takeDamage(int amount);
    }

    // [CONCEPT] Interface with a default method
    interface Healable {
        void heal(int amount);
        default void rest() { heal(10); }
    }

    interface Item {
        String label();
        void use(Hero hero);
    }

    // [CONCEPT] Generics with a bounded type parameter
    static class Inventory<T extends Item> {
        private final List<T> items = new ArrayList<>();

        void add(T item) { items.add(item); }

        T take() throws InvalidActionException {
            if (items.isEmpty()) throw new InvalidActionException("Your bag is empty!");
            return items.remove(0);
        }

        int size() { return items.size(); }
    }

    // [CONCEPT] Class implementing an interface
    static class Potion implements Item {
        private final int amount;
        Potion(int amount) { this.amount = amount; }
        @Override public String label() { return "Potion"; }
        @Override public void use(Hero hero) { hero.heal(amount); }
    }

    // [CONCEPT] Abstract class - partially implemented base type, cannot be instantiated
    static abstract class Entity implements Fighter {
        private static int created = 0;                // [CONCEPT] static (class-level) state

        private final String name;                     // [CONCEPT] Encapsulation: private fields,
        private final int maxHp;                       //           exposed only via methods
        private int hp;
        protected final Random rng = new Random();     // [CONCEPT] protected: visible to subclasses

        protected Entity(String name, int maxHp) {     // [CONCEPT] Constructor
            this.name = name;                          // [CONCEPT] this keyword
            this.maxHp = maxHp;
            this.hp = maxHp;
            created++;
        }

        public static int getCreated() { return created; }

        @Override public String getName() { return name; }
        @Override public boolean isAlive() { return hp > 0; }
        @Override public void takeDamage(int amount) { hp = Math.max(0, hp - amount); }

        public int getHp() { return hp; }
        public int getMaxHp() { return maxHp; }
        protected void restore(int amount) { hp = Math.min(maxHp, hp + amount); }

        // [CONCEPT] Abstract method - every subclass MUST define its own version
        protected abstract int baseDamage();

        // Default behavior that subclasses may override
        protected Element element() { return Element.PHYSICAL; }

        // [CONCEPT] Method overloading (compile-time polymorphism): same name, different parameters
        public void attack(Fighter target) { attack(target, 0); }

        public void attack(Fighter target, int bonus) {
            int dmg = (int) ((baseDamage() + bonus) * element().getMultiplier());
            target.takeDamage(dmg);
            String tag = element() == Element.PHYSICAL ? "" : " [" + element() + "]";
            System.out.println("  " + name + " hits " + target.getName() + " for " + dmg + tag + "!");
        }

        // [CONCEPT] Overriding Object methods
        @Override public String toString() {
            String bar = "#".repeat(Math.max(0, hp * 20 / maxHp));
            return String.format("%-9s %-8s HP %3d/%-3d |%-20s|",
                    getClass().getSimpleName(), name, hp, maxHp, bar);
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return name.equals(((Entity) o).name);
        }

        @Override public int hashCode() { return Objects.hash(getClass(), name); }
    }

    // [CONCEPT] Inheritance + multiple interfaces: Hero IS-A Entity and IS-A Healable
    static abstract class Hero extends Entity implements Healable {
        static final int SPECIAL_COST = 3;

        private int energy = 0;
        private final Inventory<Item> bag = new Inventory<>();   // [CONCEPT] Composition (HAS-A)

        protected Hero(String name, int maxHp) {
            super(name, maxHp);                                  // [CONCEPT] super(...) constructor call
            bag.add(new Potion(25));
            bag.add(new Potion(25));
        }

        @Override public void heal(int amount) {
            restore(amount);
            System.out.println("  " + getName() + " recovers " + amount + " HP.");
        }

        // [CONCEPT] Overriding + super: extend the parent's behavior instead of replacing it
        @Override public void attack(Fighter target, int bonus) {
            super.attack(target, bonus);
            energy++;
        }

        public void useSpecial(Fighter target) throws InvalidActionException {
            if (energy < SPECIAL_COST)
                throw new InvalidActionException("Not enough energy (" + energy + "/" + SPECIAL_COST + ").");
            special(target);
            energy -= SPECIAL_COST;
        }

        public void useItem() throws InvalidActionException {
            Item item = bag.take();
            System.out.println("  " + getName() + " uses " + item.label() + ".");
            item.use(this);
        }

        protected abstract void special(Fighter target);   // each class has a unique special move

        public void gainEnergy(int amount) { energy += amount; }
        public Inventory<Item> getBag() { return bag; }

        @Override public String toString() {
            return super.toString() + " EN " + energy + " | Items " + bag.size();
        }
    }

    // [CONCEPT] Concrete subclasses - each one overrides the abstract methods differently
    static class Warrior extends Hero {
        Warrior(String name) { super(name, 80); }

        @Override protected int baseDamage() { return 10 + rng.nextInt(7); }

        @Override public void takeDamage(int amount) {          // armor reduces incoming damage
            super.takeDamage(Math.max(1, amount - 3));
        }

        @Override protected void special(Fighter target) {
            System.out.println("  " + getName() + " unleashes POWER SLAM!");
            attack(target, 12);
        }
    }

    static class Mage extends Hero {
        Mage(String name) { super(name, 55); }

        @Override protected int baseDamage() { return 9 + rng.nextInt(6); }
        @Override protected Element element() { return Element.FIRE; }

        @Override protected void special(Fighter target) {
            System.out.println("  " + getName() + " summons a METEOR!");
            attack(target, 14);
        }
    }

    static class Archer extends Hero {
        Archer(String name) { super(name, 65); }

        @Override protected int baseDamage() { return 8 + rng.nextInt(6); }

        @Override protected void special(Fighter target) {
            System.out.println("  " + getName() + " fires a DOUBLE SHOT!");
            attack(target, 4);
            attack(target, 4);
        }
    }

    static abstract class Monster extends Entity {
        protected Monster(String name, int maxHp) { super(name, maxHp); }
        abstract String taunt();
    }

    static class Goblin extends Monster {
        Goblin() { super("Grix", 30); }
        @Override protected int baseDamage() { return 6 + rng.nextInt(5); }
        @Override String taunt() { return "\"Shiny things! Give them!\""; }
    }

    static class Skeleton extends Monster {
        Skeleton() { super("Bonesy", 40); }
        @Override protected int baseDamage() { return 7 + rng.nextInt(6); }
        @Override protected Element element() { return Element.ICE; }
        @Override String taunt() { return "\"Rattle rattle...\""; }
    }

    // [CONCEPT] final class - cannot be extended any further
    static final class Dragon extends Monster {
        Dragon() { super("Ignis", 70); }
        @Override protected int baseDamage() { return 10 + rng.nextInt(7); }
        @Override protected Element element() { return Element.FIRE; }
        @Override String taunt() { return "\"You dare enter my lair?!\""; }
    }

    // [CONCEPT] Nested class that orchestrates the game (association with Hero and Monster)
    static class Battle {
        private final Hero hero;
        private final List<Monster> monsters;
        private final Scanner in;
        private int turn = 0;

        // [CONCEPT] Inner (non-static) class - has access to the enclosing Battle's fields
        class Round {
            void start() {
                turn++;
                System.out.println("\n--- Round " + turn + " ---");
            }
        }
        private final Round round = new Round();

        Battle(Hero hero, List<Monster> monsters, Scanner in) {
            this.hero = hero;
            this.monsters = monsters;
            this.in = in;
        }

        boolean play() {
            for (Monster m : monsters) {
                System.out.println("\n>>> " + m.getClass().getSimpleName() + " " + m.getName()
                        + " appears! " + m.taunt());

                while (hero.isAlive() && m.isAlive()) {
                    round.start();
                    System.out.println(hero);
                    System.out.println(m);
                    playerTurn(m);
                    if (m.isAlive()) m.attack(hero);   // [CONCEPT] Polymorphism: same call, different behavior
                }

                if (!hero.isAlive()) {
                    System.out.println("\n*** " + hero.getName() + " has fallen. GAME OVER ***");
                    return false;
                }
                System.out.println("\n" + m.getName() + " is defeated!");
                hero.rest();                            // default interface method
            }
            System.out.println("\n*** VICTORY! " + hero.getName() + " conquered the dungeon in "
                    + turn + " rounds! ***");
            return true;
        }

        int getTurns() { return turn; }

        private void playerTurn(Monster target) {
            while (true) {
                System.out.print("[1] Attack  [2] Special  [3] Use item > ");
                String choice = in.hasNextLine() ? in.nextLine().trim() : "1";
                try {
                    switch (choice) {
                        case "1": hero.attack(target); return;
                        case "2": hero.useSpecial(target); return;
                        case "3": hero.useItem(); return;
                        default: throw new InvalidActionException("Pick 1, 2 or 3.");
                    }
                } catch (InvalidActionException e) {     // [CONCEPT] Exception handling
                    System.out.println("  ! " + e.getMessage());
                }
            }
        }
    }

    // Reads one line; returns "q" if input has ended so the program can never loop forever
    private static String readLine(Scanner in) {
        return in.hasNextLine() ? in.nextLine().trim() : "q";
    }

    private static boolean showTitleScreen(Scanner in) {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("               D U N G E O N   D U E L");
        System.out.println("==============================================");
        System.out.println("  Fight a Goblin, a Skeleton and a Dragon.");
        System.out.println("  Attack to build energy, then unleash specials!");
        System.out.println("----------------------------------------------");
        System.out.println("   [ENTER] Start game        [Q] Quit");
        System.out.println("==============================================");
        System.out.print("> ");
        return !readLine(in).equalsIgnoreCase("q");
    }

    private static void showEndScreen(boolean won, Hero hero, int rounds) {
        System.out.println();
        System.out.println("==============================================");
        System.out.println(won ? "                 V I C T O R Y" : "                G A M E   O V E R");
        System.out.println("==============================================");
        System.out.println("  Hero:    " + hero.getName() + " the " + hero.getClass().getSimpleName());
        System.out.println("  Rounds:  " + rounds);
        System.out.println("  Result:  " + (won ? "The dungeon is conquered!" : "The dungeon claimed another soul."));
        System.out.println("  Entities created so far: " + Entity.getCreated());
        System.out.println("----------------------------------------------");
        System.out.println("   [R] Play again            [Q] Quit");
        System.out.println("==============================================");
    }

    private static Hero createHero(Scanner in) {
        System.out.print("\nName your hero: ");
        String name = readLine(in);
        if (name.isEmpty() || name.equals("q")) name = "Hero";

        System.out.print("Choose class - [1] Warrior  [2] Mage  [3] Archer: ");
        String pick = readLine(in);

        // [CONCEPT] Upcasting: a Hero reference can hold any Hero subclass object
        Hero hero;
        switch (pick) {
            case "2": hero = new Mage(name); break;
            case "3": hero = new Archer(name); break;
            default:  hero = new Warrior(name);
        }

        // [CONCEPT] Anonymous class - a one-off Item implementation defined inline
        hero.getBag().add(new Item() {
            @Override public String label() { return "Mystery Elixir"; }
            @Override public void use(Hero h) { h.heal(15); h.gainEnergy(3); }
        });
        return hero;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        if (!showTitleScreen(in)) {                 // START: press ENTER to begin, Q to quit
            System.out.println("Goodbye, adventurer!");
            return;
        }

        boolean playAgain = true;
        while (playAgain) {
            Hero hero = createHero(in);
            List<Monster> monsters = List.of(new Goblin(), new Skeleton(), new Dragon());
            Battle battle = new Battle(hero, monsters, in);

            boolean won = battle.play();
            showEndScreen(won, hero, battle.getTurns());

            // END: press R to replay, anything else (or Q) to quit
            System.out.print("> ");
            playAgain = readLine(in).equalsIgnoreCase("r");
        }
        System.out.println("\nThanks for playing Dungeon Duel!");
    }
}