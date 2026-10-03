# Object-Oriented Programming Concepts in the Dungeon Game

## Introduction

This report explains how the Java game in the file `DungeonGame.java` demonstrates the core principles of object-oriented programming (OOP). The program is a simple turn-based dungeon battle game where the player chooses a hero and fights monsters. Although the game is small, it effectively applies many OOP concepts such as classes, objects, inheritance, encapsulation, abstraction, polymorphism, interfaces, composition, and error handling. The design shows how OOP helps organize software into reusable, maintainable, and structured components.

## 1. Classes and Objects

A class is a blueprint for creating objects, while an object is an instance of that class. In this game, several classes represent the major components of the program. For example:

- `Hero` represents all player characters.
- `Monster` represents enemies.
- `Battle` manages combat between the hero and monsters.
- `Inventory` stores items.
- `Potion` is a type of item.

These classes are not just theoretical concepts; they are used to create actual game objects such as `Warrior`, `Mage`, `Goblin`, and `Dragon`. This demonstrates the fundamental OOP concept that real-world entities can be modeled as objects with attributes and behaviors.

## 2. Encapsulation

Encapsulation is the process of hiding internal data and exposing it only through methods. In the game, private fields such as `name`, `hp`, and `maxHp` are defined inside the `Entity` class. These values are not directly modified from outside the class. Instead, methods such as `getHp()`, `takeDamage()`, and `heal()` are used to access and update them.

This is important because it protects the integrity of the object. For instance, a player cannot directly change another object’s health value without following the rules of the class. This makes the code more secure, predictable, and easier to maintain.

## 3. Abstraction

Abstraction means focusing on what an object does rather than how it does it. The game uses abstract classes to represent general game entities. The `Entity` class is abstract, meaning it defines common features shared by characters and monsters, such as health, name, and attack behavior. The method `baseDamage()` is also abstract, which means subclasses must implement it in their own way.

This concept simplifies the design because the program can treat all entities as general objects without needing to know every implementation detail. The game only requires the abstract behavior: a character can attack, take damage, and stay alive.

## 4. Inheritance

Inheritance allows a class to inherit attributes and methods from another class. In this game, there is a clear inheritance hierarchy:

- `Entity` is the base class.
- `Hero` and `Monster` inherit from `Entity`.
- `Warrior`, `Mage`, and `Archer` inherit from `Hero`.
- `Goblin`, `Skeleton`, and `Dragon` inherit from `Monster`.

This design avoids repetition. For example, all entities share the common functionality of health management and basic attacks. Subclasses only add their own special features, making the code shorter and more organized.

## 5. Polymorphism

Polymorphism means the same method can behave differently depending on the object using it. The game demonstrates this in several ways.

First, method overloading is used in `attack()`. The class defines:

- `attack(Fighter target)`
- `attack(Fighter target, int bonus)`

Both methods have the same name but different parameters. This is compile-time polymorphism.

Second, overriding is used when subclasses provide different implementations of methods. For example:

- `Warrior.takeDamage()` reduces incoming damage.
- `Mage.element()` returns `FIRE`.
- `Skeleton.element()` returns `ICE`.

This means the same method call can produce different outcomes depending on the object type. This is essential in game design because each hero or monster has unique abilities and characteristics.

## 6. Interfaces

An interface defines a contract that a class must implement. This game uses several interfaces, including:

- `Fighter`
- `Healable`
- `Item`

For example, `Fighter` requires methods such as `getName()`, `isAlive()`, and `takeDamage()`. Any class that acts as a fighter must implement these methods. This ensures consistency across different character types. The `Item` interface allows objects like `Potion` to be used in the inventory and applied to a hero.

The `Healable` interface also includes a default method `rest()`, which calls `heal(10)`. This default method allows classes that implement `Healable` to inherit basic healing behavior without redefining it every time.

## 7. Method Overriding and Method Overloading

These are two important OOP features used in this game.

### Method Overloading

Method overloading occurs when methods share the same name but have different parameters. In `Entity`, both `attack` methods use the same name but accept different numbers of parameters. This helps the game support different attack behaviors without confusing the code.

### Method Overriding

Method overriding occurs when a subclass provides a different implementation of a method already defined in a parent class. For example, `Hero.attack(Fighter target, int bonus)` overrides the parent version to increase energy after attacking. Similarly, `Mage` and `Skeleton` override `element()` to define their damage type. Overriding is a core object-oriented mechanism that allows specialized behavior.

## 8. Constructors and `this` / `super`

Constructors are special methods used to initialize objects when they are created. The `Entity` constructor initializes the name and maximum health, while the `Hero` constructor calls the parent constructor using `super(name, maxHp)`. The `this` keyword is used to distinguish between instance variables and constructor parameters. This is an important part of object creation and initialization in Java.

Without constructors and these keywords, object creation would be inconsistent and harder to manage. The use of `this` and `super` makes the inheritance structure clear and reliable.

## 9. Static Members

The game uses the `static` keyword to define class-level members. For example, `created` tracks how many entities have been created, and `SPECIAL_COST` defines the energy cost required for a hero special attack. These values are shared among all objects rather than belonging to a single instance.

The static method `getCreated()` returns the total count of created entities. This is useful for tracking game state and illustrates how static members are used in object-oriented design.

## 10. Final Keyword

The `final` keyword is used to restrict changes. For example:

- `private final double multiplier;`
- `private final String name;`
- `static final int SPECIAL_COST = 3;`

The `Dragon` class is declared `final`, meaning it cannot be extended. This prevents accidental changes to key behavior and ensures that important values remain stable. Final fields and classes are used to create safer and more controlled code.

## 11. Enums

The `Element` enum defines a fixed set of attack types: `PHYSICAL`, `FIRE`, and `ICE`. Each enum constant stores a multiplier that changes how much damage a move deals. This is a clear example of an enum being used to represent a limited set of values while also attaching behavior to them. It organizes the game rules in a clean and readable way.

## 12. Composition

Composition is a relationship where a class contains another class as part of its structure. The `Hero` class contains an `Inventory` object, and the `Inventory` stores `Item` objects like `Potion`. This is a classic “has-a” relationship. The hero has an inventory, and the inventory has items. This type of relationship is common in game development because game objects are often built from smaller components.

## 13. Generics

The `Inventory` class is declared as a generic class: `Inventory<T extends Item>`. This means the inventory can hold different types of items as long as they implement the `Item` interface. Using generics improves type safety and makes the code reusable. The same inventory class can store many kinds of items without losing flexibility.

## 14. Exception Handling

The game defines a custom exception named `InvalidActionException`, which is thrown when the player chooses an invalid menu choice, tries to use an empty inventory, or lacks enough energy. This is a strong example of exception handling in OOP. Instead of crashing, the game catches the exception and prompts the user to try again.

This makes the game more robust and user-friendly, while also showing how Java handles errors in a structured and object-oriented manner.

## 15. Inner, Nested, and Anonymous Classes

The game also demonstrates advanced Java OOP features:

- `InvalidActionException` is a nested class.
- `Battle.Round` is an inner class.
- A mystery item is created using an anonymous class.

These classes help keep the program organized and allow logic to be placed near the code it belongs to. This is especially useful in a game where specific behaviors are temporary or closely tied to a surrounding object.

## 16. Object Methods (`toString()`, `equals()`, `hashCode()`)

Java classes inherit default methods from the `Object` class. The game overrides them to provide meaningful behavior:

- `toString()` displays each entity’s status, including health and class name.
- `equals()` compares two entities based on type and name.
- `hashCode()` works with `equals()` to maintain correct behavior in hash-based collections.

This improves object behavior and helps Java manage objects more effectively.

## 17. Access Modifiers

The game uses access modifiers such as `private`, `protected`, and `public` to control who can access class members. This is another important part of encapsulation. Private fields are protected from outside changes, while public methods allow controlled interaction with the object. Protected methods and fields are accessible to subclasses, making inheritance work properly.

## Conclusion

The Java dungeon game is an excellent example of object-oriented programming because it includes nearly all major OOP concepts in a single program. It uses classes and objects, encapsulation, abstraction, inheritance, polymorphism, interfaces, constructors, static members, generics, exceptions, and composition. These concepts make the game more organized, modular, and reusable.

By demonstrating these principles in a simple but effective way, the program shows that OOP is not only a programming style but a structured approach to building software. It simplifies the development of complex systems and improves maintainability, flexibility, and scalability.

## References

The concepts discussed above are implemented directly in the code of `DungeonGame.java`.
