package bomberquest.map;

import bomberquest.audio.MusicTrack;
import bomberquest.map.boost.*;
import bomberquest.screen.VictoryScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import bomberquest.*;
import java.util.*;

/**
 * Represents the game map.
 * Holds all the objects and entities in the game.
 */
public class GameMap {

    // A static block is executed once when the class is referenced for the first time.
    static {
        // Initialize the Box2D physics engine.
        Box2D.init();
    }

    // Box2D physics simulation parameters (you can experiment with these if you want, but they work well as they are)
    /**
     * The time step for the physics simulation.
     * This is the amount of time that the physics simulation advances by in each frame.
     * It is set to 1/refreshRate, where refreshRate is the refresh rate of the monitor, e.g., 1/60 for 60 Hz.
     */
    private static final float TIME_STEP = 1f / Gdx.graphics.getDisplayMode().refreshRate;
    /** The number of velocity iterations for the physics simulation. */
    private static final int VELOCITY_ITERATIONS = 6;
    /** The number of position iterations for the physics simulation. */
    private static final int POSITION_ITERATIONS = 2;
    /**
     * The accumulated time since the last physics step.
     * We use this to keep the physics simulation at a constant rate even if the frame rate is variable.
     */
    private float physicsTime = 0;

    /** list of actions to be executed after game tick */
    private final List<Runnable> pendingActions = new ArrayList<>();

    /** The game, in case the map needs to access it. */
    private final BomberQuestGame game;

    /** The Box2D world for physics simulation. */
    private final World world;

    /** The player character. */
    private Player player;

    /** List of ghosts in the game. */
    private List<Ghost> ghosts;

    /** List of boost items on the map. */
    private List<Boost> boosts;

    /** 2D array of flower tiles on the map. */
    private Flowers[][] flowers;

    /** 2D array of walls (both destructible and indestructible). */
    private  Wall[][] walls;

    /** The exit point for the level. */
    private Exit exit;

    /** Flag indicating if the player has won. */
    private boolean win = false;

    /** Countdown timer for the level (in seconds). */
    private int countdownTimer;

    /** Flag for pausing and resuming the timer */
    private boolean timerRunning;

    /** Tracks occupied cells to avoid overlapping placements. */
    Set<Vector2> occupiedCells = new HashSet<>();

    /**
     * Constructs a new GameMap.
     * Initializes the game map, including the player, enemies, boosts, walls, and other game elements.
     * Also sets up the Box2D physics world and handles collision detection.
     *
     * @param game The main game instance, used for managing the game state and interacting with other components.
     */
    public GameMap(BomberQuestGame game) {
        // Assign the main game instance
        this.game = game;

        // Create a new Box2D world with no gravity
        this.world = new World(Vector2.Zero, true);

        // Calculate the center coordinates of the map
        float centerX = Gdx.graphics.getWidth() / (16 * 4) / 2;
        float centerY = Gdx.graphics.getHeight() / (16 * 4) / 2;
        // Calculate the corners of the map
        float leftTopX = centerX - (Gdx.graphics.getWidth() / (16 * 4)) / 2;
        float leftTopY = centerY + (Gdx.graphics.getHeight() / (16 * 4)) / 2;
        float rightTopX = centerX + (Gdx.graphics.getWidth() / (16 * 4)) / 2;
        float rightTopY = centerY + (Gdx.graphics.getHeight() / (16 * 4)) / 2;
        float leftBottomX = centerX - (Gdx.graphics.getWidth() / (16 * 4)) / 2;
        float leftBottomY = centerY - (Gdx.graphics.getHeight() / (16 * 4)) / 2;
        float rightBottomX = centerX + (Gdx.graphics.getWidth() / (16 * 4)) / 2;
        float rightBottomY = centerY - (Gdx.graphics.getHeight() / (16 * 4)) / 2;

        // Initialize the player at the center of the map
        this.player = new Player(this.world, this, centerX, centerY);

        // Initialize the list of ghosts and place them at different map corners
        this.ghosts = new ArrayList<>();
        this.ghosts.add(new Ghost(this.world, leftTopX + 2, leftTopY - 2));
        this.ghosts.add(new Ghost(this.world, rightTopX -2, rightTopY-2));
        this.ghosts.add(new Ghost(this.world, leftBottomX+2, leftBottomY+2));
        this.ghosts.add(new Ghost(this.world, rightBottomX-2, rightBottomY+2));

        // Initialize the list of boosts
        this.boosts = new ArrayList<>(); // Initialize boosts list

        // Create a 2D grid of flowers across the map
        this.flowers = new Flowers[Gdx.graphics.getWidth()/(16*4)][Gdx.graphics.getHeight()/(16*4)];
        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {
                this.flowers[i][j] = new Flowers(i, j);
            }
        }

        // Initialize walls and place boosts and exit
        this.walls = new Wall[Gdx.graphics.getWidth()/(16*4)][Gdx.graphics.getHeight()/(16*4)]; // Example grid size (10x10)
        initWalls();// Set up destructible and indestructible walls
        placeExitUnderRandomWall();// Place the exit randomly under a destructible wall
        placeBoostsUnderRandomWalls(); // Place random boosts on the map

        // Set up collision detection for game elements
        setupContactListener();

        // Initialize the countdown timer (e.g., 5 minutes)
        this.countdownTimer = 30000;
        this.timerRunning = true;

    }

    /**
     * Constructs a new GameMap by loading map data from a file.
     * The map file defines the layout of walls, boosts, player, enemies, and exit.
     *
     * @param game     The main game instance, used for managing the game state and interacting with other components.
     * @param mapFile  The file containing the map configuration, specifying the layout and game elements.
     */
    public GameMap(bomberquest.BomberQuestGame game, FileHandle mapFile) {
        this.game = game;
        this.world = new World(Vector2.Zero, true);

        // Initialize game objects
        this.ghosts = new ArrayList<>();
        this.boosts = new ArrayList<>();

        // Initialize walls and flowers with a default 21x21 grid size
        this.walls = new Wall[21][21];
        this.flowers = new Flowers[21][21];

        // Add flowers in non-wall spaces
        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {
                if (this.walls[i][j] == null || !(this.walls[i][j] instanceof IndestructibleWall)) {
                    this.flowers[i][j] = new Flowers(i, j);
                }
            }
        }

        // Load the map file and configure walls, boosts, and other entities
        Properties mapProperties = new Properties();
        try {
            mapProperties.load(mapFile.read()); // Load the map properties from the file

            for (String key : mapProperties.stringPropertyNames()) {
                if (key.startsWith("#") || key.trim().isEmpty()) continue;  // Skip comments and empty lines

                // Parse coordinates and type from the file
                String[] coordinates = key.split(",");
                int x = Integer.parseInt(coordinates[0].trim());
                int y = Integer.parseInt(coordinates[1].trim());

                int type = Integer.parseInt(mapProperties.getProperty(key).trim());

                // Handle each type of map element
                switch (type) {
                    case 0: // Indestructible Wall
                        this.walls[x][y] = new IndestructibleWall(x, y);
                        break;
                    case 1: // Destructible Wall
                        this.walls[x][y] = new DestructibleWall(x, y, this);
                        break;
                    case 2: // Player (Entrance)
                        this.player = new Player(world, this, x, y);
                        break;
                    case 3: // Enemy (Ghost)
                        this.ghosts.add(new Ghost(world, x, y));
                        break;
                    case 4: // Exit under a destructible wall
                        this.exit = new Exit(world, x, y);
                        this.exit.reveal();
                        this.walls[x][y] = new DestructibleWall(x, y, this);
                        break;
                    case 5: // Bomb Boost under a destructible wall
                    case 6:// Explosion Boost under a destructible wall
                        Boost boost = null;
                        if (type == 5) {
                            boost = new BombBoost(world, this, null, x, y); // player будет инициализирован позже
                        } else if (type == 6) {
                            boost = new ExplosionBoost(world, this, null, x, y); // player будет инициализирован позже
                        }
                        DestructibleWall wall = new DestructibleWall(x, y, this);
                        wall.setHiddenBoost(boost);
                        walls[x][y] = wall;
                        break;
                    default:
                        System.out.println("Unknown object type: " + type);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading map: " + e.getMessage());
            e.printStackTrace();
        }

        // Ensure the player is initialized
        if (this.player == null) {
            throw new IllegalStateException("Player was not initialized. Please ensure the map file contains an entrance.");
        }

        // Assign hidden boosts to the player and add them to the boosts list
        for (Wall[] wall : walls) {
            for (Wall wall1 : wall) {
                if (wall1 instanceof DestructibleWall) {
                    DestructibleWall destructibleWall = (DestructibleWall) wall1;
                    Boost hiddenBoost = destructibleWall.getHiddenBoost();
                    if (hiddenBoost != null) {
                        hiddenBoost.setPlayer(player);// Associate the boost with the player
                        boosts.add(hiddenBoost);
                    }
                }
            }
        }
        // Initialize hitboxes for all walls
        for (Wall[] wall : walls) {
            for (Wall wall1 : wall) {
                if (wall1 != null) {
                    wall1.createHitbox(world);
                }
            }
        }

        // If no exit was defined, place it under a random destructible wall
        if (this.exit == null) {
            placeExitUnderRandomWall();
        }

        // Set up collision detection for game elements
        setupContactListener();

        // Initialize the countdown timer (e.g., 5 minutes)
        this.countdownTimer = 30000;
        this.timerRunning = true;
    }

    /**
     * Initializes the wall grid with destructible and indestructible walls.
     */
    private void initWalls() {
        // Get the player's initial position on the grid
        int playerX = (int) player.getX();
        int playerY = (int) player.getY();

        // Get the initial positions of all ghosts
        List<Ghost> ghosts = this.ghosts;
        Set<Vector2> ghostPositions = new HashSet<>();
        for (Ghost ghost : ghosts) {
            ghostPositions.add(new Vector2(ghost.getX(), ghost.getY()));
        }

        // Iterate over the wall grid to initialize each position
        for (int i = 0; i < walls.length; i++) {
            for (int j = 0; j < walls[i].length; j++) {
                // Skip placing walls near the player's position or the ghosts' positions
                if (Math.abs(i - playerX) <= 1 && Math.abs(j - playerY) <= 1 || ghostPositions.contains(new Vector2(i, j))) {
                    continue;
                }


                if (i == 0 || j == 0 || i == walls.length - 1 || j == walls[i].length - 1) {
                    walls[i][j] = new IndestructibleWall(i, j);
                } else if (Math.random() < 0.3) {
                    walls[i][j] = new DestructibleWall(i, j, this);
                }
                if (walls[i][j] != null) {
                    walls[i][j].createHitbox(world);
                }
            }
        }
    }

    /**
     * Determines if the specified position on the map is blocked.
     * A position is considered blocked if:
     * - It is outside the bounds of the map, or
     * - It contains a non-destructible wall.
     *
     * @param x The x-coordinate (in tile units) to check.
     * @param y The y-coordinate (in tile units) to check.
     * @return {@code true} if the position is blocked, {@code false} otherwise.
     */
    public boolean isBlocked(float x, float y) {
        int tileX = Math.round(x);
        int tileY = Math.round(y);
        if (tileX < 0 || tileY < 0 || tileX >= walls.length || tileY >= walls[0].length) {
            return true;
        }
        Wall wall = walls[tileX][tileY];
        return wall != null && !wall.isDestructible();
    }

    /**
     * Sets up the contact listener to handle collisions between game objects.
     */
    private void setupContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();

                // Handle player-wall collisions
                if ((a instanceof Player && b instanceof Wall) || (a instanceof Wall && b instanceof Player)) {
                    handlePlayerWallCollision((Player) (a instanceof Player ? a : b));
                }

                if ((a instanceof Player && b instanceof Ghost) || (a instanceof Ghost && b instanceof Player)) {
                    handlePlayerGhostCollision((Player) (a instanceof Player ? a : b), (Ghost) (b instanceof Ghost ? b : a));
                }

                if ((a instanceof Ghost && b instanceof Player) || (a instanceof Player && b instanceof Ghost)) {
                    handleGhostPlayerCollision((Ghost) (a instanceof Ghost ? a : b), (Player) (b instanceof Player ? b : a));
                }

                if ((a instanceof Player && b instanceof Boost) || (a instanceof Boost && b instanceof Player)) {
                    handleBoostCollision((Boost) (a instanceof Boost ? a : b));
                }

                if ((a instanceof Player && b instanceof Exit) || (a instanceof Exit && b instanceof Player)) {
                    handleExitCollision((Exit) (a instanceof Exit ? a : b));
                }
            }

            @Override
            public void endContact(Contact contact) {
                // Optional: Handle when collisions end (not needed here)
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    /**
     * Handles what happens when the player collides with a wall.
     * @param player The player instance involved in the collision.
     */
    private void handlePlayerWallCollision(Player player) {
        // Stop the player's movement when colliding with a wall
        player.stopMovement();
    }

    int collisioncounter1 = 0;
    private void handlePlayerGhostCollision(Player player, Ghost ghost) {
        collisioncounter1 += 1;
        if (collisioncounter1 > 1) {
            scheduleAction(() -> player.destroy());
        }
    }

    private void handleGhostPlayerCollision(Ghost ghost, Player player) {
        scheduleAction(() -> player.destroy());
    }

    /**
     * Handles the player's collision with the exit and triggers the victory screen.
     *
     * @param exit The exit object the player collided with.
     */
    private void handleExitCollision(Exit exit) {
        if(exit.isUnlocked()) {
            MusicTrack.BOOST.play();
            this.win = true;
            game.setScreen(new VictoryScreen(game));
        }
    }

    /**
     * Handles the player's collision with a boost by activating and destroying it.
     *
     * @param boost The boost object the player collided with.
     */
    private void handleBoostCollision(Boost boost) {
        scheduleAction(() -> boost.activate());
        MusicTrack.BOOST.play();
        scheduleAction(() -> boost.destroy());
    }

    /**
     * Places the exit under a random destructible wall within the map.
     * The exit is hidden until the player fulfills the conditions to reveal it.
     */
    public void placeExitUnderRandomWall() {
        for (int i = 1; i < walls.length - 1; i++) {
            for (int j = 1; j < walls[i].length - 1; j++) {
                if (walls[i][j] instanceof DestructibleWall) {
                    exit = new Exit(world, i, j); // Place the exit under this destructible wall
                    occupiedCells.add(new Vector2(i, j));
                    return;
                }
            }
        }
    }

    /**
     * Places boosts randomly under destructible walls on the map.
     * Ensures that no boost is placed in a cell already occupied by the exit or other boosts.
     */
    public void placeBoostsUnderRandomWalls() {
        Random rand = new Random();

        // Mark the exit cell as occupied, if it exists
        if (exit != null) {
            occupiedCells.add(new Vector2(exit.getX(), exit.getY()));
        }

        // Loop to place a fixed number of boosts (e.g., 15)
        for (int a = 0; a < 15; a++) {
            int i, j;
            boolean validPosition;

            // Find a random valid position under a destructible wall
            do {
                validPosition = true;
                i = rand.nextInt(walls.length - 2) + 1;
                j = rand.nextInt(walls[i].length - 2) + 1;

                if (!(walls[i][j] instanceof DestructibleWall) || occupiedCells.contains(new Vector2(i, j))) {
                    validPosition = false;
                }
            } while (!validPosition);

            // Create a specific boost based on the loop index
            Boost boost;
            switch (a) {
                case 0, 4, 8, 12:
                    boost = new BombBoost(world, this, player, i, j);
                    break;
                case 1, 5:
                    boost = new SpeedBoost(world, this, player, i, j);
                    break;
                case 2, 6, 10, 14:
                    boost = new ExplosionBoost(world, this, player, i, j);
                    break;
                case 3, 7:
                    boost = new GhostSpeedBoost(world, this, player, ghosts, i, j);
                    break;
                default:
                    boost = new BombBoost(world, this, player, i, j);
                    break;
            }

            // Add the boost to the list and hide it under the destructible wall
            boosts.add(boost);
            ((DestructibleWall) walls[i][j]).setHiddenBoost(boost);

            // Mark the cell as occupied
            occupiedCells.add(new Vector2(i, j));
        }
    }

    /**
     * Schedules an action to be executed later.
     *
     * @param action A {@link Runnable} representing the action to schedule.
     */
    public void scheduleAction(Runnable action) {
        pendingActions.add(action);
    }

    /**
     * Processes all scheduled actions.
     * Executes and clears the list of actions that were scheduled during the update cycle.
     * This ensures safe modification of game objects while avoiding concurrency issues.
     */
    public void processPendingActions() {
        for (Runnable action : pendingActions) {
            action.run();
        }
        pendingActions.clear();
    }

    /**
     * Updates the game state. This is called once per frame.
     * Every dynamic object in the game should update its state here.
     * @param frameTime the time that has passed since the last update
     */
    public void tick(float frameTime) {
        // Update the player if it exists
        if(player != null) {
            this.player.tick(frameTime);
        }

        // Update all walls
        Arrays.stream(walls)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .forEach(wall -> wall.update(frameTime));

        // Update all ghosts
        for (Ghost ghost : ghosts) {
            if (ghost != null) {
                ghost.update(player, frameTime); // Пример с игроком
            }
        }

        if(ghosts.isEmpty()){
            exit.unlock();
        }

        // Remove destroyed boosts and ghosts
        boosts.removeIf(Boost::isDestroyed);
        ghosts.removeIf(Ghost::isFullydestroyed);
        // Perform physics updates
        doPhysicsStep(frameTime);

        // Update the Box2D world
        world.step(frameTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        // Process any scheduled actions
        processPendingActions();

        // Update the countdown timer
        updateTimer(frameTime);
    }

    /**
     * Updates the game timer, decrementing it by the delta time.
     * Ends the game if the timer reaches zero.
     *
     * @param deltaTime The time passed since the last update.
     */
    public void updateTimer(float deltaTime) {
        if (timerRunning && countdownTimer > 0) {
            countdownTimer -= deltaTime; // Decrement timer
            if (countdownTimer <= 0) {
                countdownTimer = 0;
                MusicTrack.DEATH.play();
                game.goToGameOverScreen();
            }
        }
    }

    /**
     * Performs as many physics steps as necessary to catch up to the given frame time.
     * This will update the Box2D world by the given time step.
     * @param frameTime Time since last frame in seconds
     */
    private void doPhysicsStep(float frameTime) {
        this.physicsTime += frameTime;
        while (this.physicsTime >= TIME_STEP) {
            this.world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            this.physicsTime -= TIME_STEP;
        }
    }

    /**
     * Handles the explosion of a bomb by propagating its blast in all directions
     * and applying its effects to walls, ghosts, and the player.
     *
     * @param bomb The bomb to explode.
     */
    public void explodeBomb(Bomb bomb) {
        int centerX = Math.round(bomb.getX());
        int centerY = Math.round(bomb.getY());
        int radius = bomb.getBlastRadius();

        checkBlast(centerX, centerY);//the center

        // Check each direction (up, down, left, right)
        propagateBlast(centerX, centerY, 0, 1, radius);  // Up
        propagateBlast(centerX, centerY, 0, -1, radius); // Down
        propagateBlast(centerX, centerY, -1, 0, radius); // Left
        propagateBlast(centerX, centerY, 1, 0, radius);  // Right
    }

    /**
     * Checks if the blast affects a given position, applying destruction
     * or effects to walls, ghosts, or the player.
     *
     * @param x The x-coordinate of the blast position.
     * @param y The y-coordinate of the blast position.
     */
    private void checkBlast(int x, int y) {
        // Handle wall destruction
        if (x >= 0 && x < walls.length && y >= 0 && y < walls[0].length) {
            Wall wall = walls[x][y];
            if (wall != null && wall.isDestructible()) {
                if (!world.isLocked()) {
                    wall.destroy();
                    if (wall.isFullyDestroyed()) {
                        walls[x][y] = null;
                    }
                }
                return;
            }
        }

        // Handle player destruction
        if (Math.abs(player.getX() - x) <= 0.5f && Math.abs(player.getY() - y) <= 0.5f) {
            if (!player.isDestroyed()) {
                scheduleAction(() -> player.destroy());
            }
        }

        // Handle ghost destruction
        for (Ghost ghost : ghosts) {
            if (ghost.getHitbox() == null || ghost.isFullydestroyed()) {
                continue;
            }

            int ghostX = Math.round(ghost.getX());
            int ghostY = Math.round(ghost.getY());

            if (Math.abs(ghostX - x) <= 0.5f && Math.abs(ghostY - y) <= 0.5f) {
                if (!ghost.isDestroyed()) {
                    scheduleAction(() -> ghost.destroy());
                }
            }
        }
    }

    /**
     * Propagates a blast in a given direction and radius, affecting walls, ghosts, and the player.
     *
     * @param startX The starting X position.
     * @param startY The starting Y position.
     * @param dx     The X direction of propagation.
     * @param dy     The Y direction of propagation.
     * @param radius The blast radius.
     */
    private void propagateBlast(int startX, int startY, int dx, int dy, int radius) {
        for (int i = 1; i <= radius; i++) {
            int x = startX + (dx * i);
            int y = startY + (dy * i);

            // If the blast goes out of bounds, stop propagation
            if (x < 0 || x >= walls.length || y < 0 || y >= walls[0].length) {
                return;
            }

            Wall wall = walls[x][y];
            // Handle destructible walls
            if (wall != null) {
                if (wall.isDestructible()) {
                    if (!world.isLocked()) {
                        wall.destroy();
                        // Remove fully destroyed walls from the map
                        if (wall.isFullyDestroyed()) {
                            walls[x][y] = null;
                        }
                    }
                    continue;
                } else {
                    return; //Stop propagation when hitting an indestructible wall
                }
            }

            // Check for damage to the player, ghosts, or other entities at this position
            checkBlast(x, y);

            // Check for damage to ghosts in the current cell
            for (Ghost ghost : ghosts) {
                if (ghost.getHitbox() == null || ghost.isFullydestroyed()) {
                    continue;
                }

                int ghostX = Math.round(ghost.getX());
                int ghostY = Math.round(ghost.getY());

                // If the ghost is within the blast radius, destroy it
                if (Math.abs(ghostX - x) <= 0.5f && Math.abs(ghostY - y) <= 0.5f) {
                    if (!ghost.isDestroyed()) {
                        scheduleAction(() -> ghost.destroy());
                    }
                }
            }

            // Check for damage to the player in the current cell
            if (Math.abs(player.getX() - x) <= 0.5f && Math.abs(player.getY() - y) <= 0.5f) {
                if (!player.isDestroyed()) {
                    scheduleAction(() -> player.destroy());
                }
            }
        }
    }

    /**
     * Resets the map for a new game.
     */
    public void reset() {
        walls = new Wall[10][10];
        ghosts.clear();
        countdownTimer = 300;
        timerRunning = true;

        initWalls();
        placeExitUnderRandomWall();
    }

    /** Returns the player on the map. */
    public Player getPlayer() {
        return player;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    /** Returns the flowers on the map. */
    public List<Flowers> getFlowers() {
        return Arrays.stream(flowers).flatMap(Arrays::stream).toList();
    }
    /** Returns the walls on the map. */
    public Wall[][] getWalls() {
        return walls;
    }
    //Getters
    public boolean isWin() {
        return win;
    }
    public boolean hasWon() {
        return !exit.isHidden() && isExitUnlocked() && isWin();
    }
    public boolean hasLost() {
        return player.isDestroyed() || countdownTimer <= 0;
    }
    public Exit getExit() {
        return exit;
    }

    public int getCountdownTimer() {
        return countdownTimer;
    }

    public int getGhostsLeft() {
        return ghosts.size(); // Assuming ghosts is a List<Ghost> containing all ghosts
    }
    public boolean isExitUnlocked() {
        return ghosts.isEmpty(); // Exit is unlocked when all ghosts are defeated
    }
    public List<Boost> getBoosts() {
        return boosts;
    }
}


