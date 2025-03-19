# Bomber Quest

BomberQuest is a 2D action-strategy game built using LibGDX Library. 
Players navigate through a map, plant bombs and kill enemies, destroying walls and collecting different boosts. 
The objective is to kill enemies, find and reach the hidden exit located beneath a destructible wall, without dying in the process.

---

## Game Structure

### Welcoming Menu:
- You can immediately `Go to Game`. Via that option game map is generated randomly.
- You can choose your own map from File Chooser via `Load New Map`.
- If you do not have time, you can `Exit Game`.

### Core Class: `GameMap`
After the game starts, the generation takes place. The `GameMap` class handles:
- Initialization of the game world and physics objects.
- Management of game elements: walls, boosts, exit, enemies, and the player.
- Collision handling via `ContactListener`.
- Countdown timer and win/loss logic within HUD.

### Key Classes:
- **Player (`Player`)**: The controllable character, navigated by ↓↑→← capable of planting bombs no more than one in 3sec (without boosts).
- **Bomb (`Bomb`)**: After planting bomb starts ticking and explodes in 3 seconds.
- **Explosion (`Explosion`)**: An explosion occurs after the bomb explodes. Keep in mind that this can destroy both characters, enemies, and destructible walls.
- **Enemies (`Ghost`)**: Enemies that chase and challenge the player.
- **Exit (`Exit`)**: The main goal, hidden beneath a destructible wall.
- **Boosts (`Boost`)**: Randomly generated power-ups hidden under destructible walls.
- **Walls (`Wall`)**:
    - **Destructible (`DestructibleWall`)**: Can be destroyed with bombs.
    - **Indestructible (`IndestructibleWall`)**: Form the map boundaries. It is impossible to be passed through or destroyed.

---

## Features

- **Box2D Physics Engine** for collision detection and object dynamics.
- **Gdx-nativefilechooser** for easier usage of your own maps.
- **Boost System**: Speed boost, explosion radius, extra bombs, and more.
- **Random Map Generation**: Each game features a unique map layout.
- **Enemy AI**: Ghosts with dynamic movement and behaviors.
- **Countdown Timer**: Adds urgency and challenge to gameplay.

---

## Functionality

#### Map and Gameplay Basics
- **Random map generation function**: it provides an option to generate a new absolutely random map to check the functionality and improve your gaming skiils.
- The program **reads any map from a properties file and play it**.
- **Destructible and indestructible walls** are supported.
- If no exit is included in the map, **it is placed under a random destructible wall**.
#### Player Mechanics
- The player moves using arrow keys in **four directions**.
- To win, the player must **defeat all enemies, uncover the exit, and reach it**.
- The player dies if hit by an **explosion or enemy**, resulting in a **game over**.
- The player can place **bombs, initially limited to one at a time**, and must wait for the bomb to explode before placing another.
#### Power-Ups
- **Bomb Power-Ups**: Each increases the concurrent bomb limit by 1 (up to 8). 
- **Flame Power-Ups**: Each increases the bomb blast radius by 1 field in all directions (up to 8).
- **Speed boost**: Increases the speed of the Character.
- **Ghost speed anti-boost**: Decreases the speed of enemies.
#### Enemies
- Enemies are **dynamic objects moving through the map**.
- Enemies can be killed by **bomb explosion**.
#### Game Rules
- A **countdown timer** determines the game duration; the game is over when time runs out.
- **The camera** ensures the player is always visible
- A **HUD** displays essential information: blast radius, bomb limit, timer, remaining enemies, and status of exit.
#### Screens
- **Game Menu**: Available at startup and through the Esc button; allows resuming, selecting a new map, or exiting. 
The game pauses while the menu is displayed.
- **Victory**: Shows win screens, stops gameplay, and provides an option to return to the main menu.
- **Game Over**: Shows lose screen, stops gameplay, and provides an option to return to the main menu.
#### Graphics and Performance
- Render the game as a **2D top-down view using libGDX** with simple 2D assets. 
- Ensures **smooth gameplay** at a playable framerate on modern hardware. 
- Supports **various screen sizes**.
#### Audio
- Includes different background **music** for gameplay and menus.
- There are different **sound effects** for in-game actions like bomb planting, explosion, death, boost's obtaining, etc.
#### Additional
- **Object-oriented programming** is used, with each object type implemented as a dedicated class in a proper class hierarchy.
- **Documentation** is provided.
- **Two sample map files** are attached.
- **Demonstration videos** are attached.

---

## Setup

### Characteristics:
- **Java 17+**
- **Gradle 8.4** (project is configured for Gradle builds)
- **LibGDX**
- *github/arthurtemple/**gdx-nativefilechooser***

---

The Project has been made as an educational one within Technical University of Munich Programming course.
