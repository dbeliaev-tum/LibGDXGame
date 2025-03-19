package bomberquest.map;

import bomberquest.audio.MusicTrack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import bomberquest.texture.Animations;
import bomberquest.texture.Drawable;
import java.util.ArrayList;

/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Player implements Drawable {

    /** Total time elapsed since the game started. We use this for calculating the player movement and animating it. */
    private float elapsedTime;

    /** The player's movement speed. */
    float speed = 1.5f;

    /** The Box2D hitbox of the player, used for position and collision detection. */
    private Body hitbox;

    /** List of bombs available to the player. */
    private final ArrayList<Bomb> bombs;

    /** Number of active bombs currently placed in the game. */
    private int activeBombs = 0;

    /** Maximum number of bombs the player can place at once. */
    private int MAX_ACTIVEBOMBS = 1;

    private static final int MAX_BOMBS = 1000;

    /** The game map the player interacts with. */
    private final GameMap gameMap;

    /** Timer for player destruction animations. */
    float destructionTimer = 0.3f;

    /** Indicates if the player is destroyed. */
    private boolean destroyed = false;

    /** Indicates if the player is fully removed from the game. */
    private boolean fullydestroyed = false;

    private int bombLimit;

    /**
     * Initializes the player with a hitbox, bomb list, and interaction with the game map.
     *
     * @param world  The Box2D world to which the player's hitbox will be added.
     * @param gameMap The game map the player interacts with.
     * @param x       The initial x-coordinate of the player.
     * @param y       The initial y-coordinate of the player.
     */
    public Player(World world, GameMap gameMap, float x, float y) {
        this.hitbox = createHitbox(world, x, y);
        this.gameMap = gameMap;

        this.bombs = new ArrayList<>();
        for (int i = 0; i < MAX_BOMBS; i++) {
            bombs.add(new Bomb(world, gameMap)); // Создаём 4 объекта Bomb
        }
    }

    /**
     * Creates a Box2D body for the player.
     * This is what the physics engine uses to move the player around and detect collisions with other bodies.
     * @param world The Box2D world to add the body to.
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @return The created body.
     */
    private Body createHitbox(World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set the initial position of the body.
        bodyDef.position.set(startX, startY);
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        // Now we need to give the body a shape so the physics engine knows how to collide with it.
        // We'll use a circle shape for the player.
        CircleShape circle = new CircleShape();
        // Give the circle a radius of 0.3 tiles (the player is 0.6 tiles wide).
        circle.setRadius(0.3f);
        // Attach the shape to the body as a fixture.
        // Bodies can have multiple fixtures, but we only need one for the player.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;
        // We're done with the shape, so we should dispose of it to free up memory.
        body.createFixture(fixtureDef);
        circle.dispose();
        // Set the player as the user data of the body so we can look up the player from the body later.
        body.setUserData(this);
        return body;
    }

    /**
     * Updates the player's state each frame, handling movement, bomb placement, and destruction.
     * This method processes player input, updates the position, handles bomb activation, and manages destruction.
     * Optionally, the player can be moved in a circular path for specific game logic.
     *
     * @param frameTime Time elapsed since the last frame, used for smooth updates.
     */
    public void tick(float frameTime) {
        if (destroyed) {
            if (fullydestroyed) return;
            destructionTimer -= frameTime;
            elapsedTime += frameTime;

            if (destructionTimer <= 0 && !fullydestroyed) {
                if (hitbox != null) {
                    hitbox.getWorld().destroyBody(hitbox);
                    hitbox = null;
                }
                fullydestroyed = true;
            }
            return;
        }
        this.elapsedTime += frameTime;

        // Handle movement
        float xVelocity = 0;
        float yVelocity = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yVelocity = speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yVelocity = -speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xVelocity = -speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xVelocity = speed;
        }

        this.hitbox.setLinearVelocity(xVelocity, yVelocity);

        // Handle bomb placement
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (activeBombs < MAX_ACTIVEBOMBS) {
                for (Bomb bomb : bombs) {
                    if (!bomb.isActive()) {
                        bomb.activate(getX(), getY());
                        MusicTrack.PLANTING.play();
                        activeBombs++;
                        break;
                    }
                }
            }
        }

        // Update bombs
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            bomb.tick(frameTime);
            if (!bomb.isActive() && bomb.wasActive()) {
                activeBombs--;
                bomb.setWasActive(false);
            }

        }

    }

    /**
     * Determines the player's current visual appearance based on their movement and state.
     * If the player is destroyed, the "death" animation is displayed.
     * If the player is moving, the appropriate walking animation for the direction is displayed.
     * If the player is idle, the first frame of the "walk down" animation is shown.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        // Check if the player is destroyed. If so, return the "death" animation.
        if (destroyed) {
            return Animations.CHARACTER_DIES.getKeyFrame(elapsedTime, false);
        }

        // Retrieve the current velocities of the player's hitbox (movement in x and y directions).
        float xVelocity = hitbox.getLinearVelocity().x;
        float yVelocity = hitbox.getLinearVelocity().y;

        // Retrieve the current velocities of the player's hitbox (movement in x and y directions).
        if (yVelocity > 0) {
            // Player is moving upward.
            return Animations.CHARACTER_WALK_UP.getKeyFrame(elapsedTime, true);
        } else if (yVelocity < 0) {
            // Player is moving downward.
            return Animations.CHARACTER_WALK_DOWN.getKeyFrame(elapsedTime, true);
        } else if (xVelocity > 0) {
            // Player is moving to the right.
            return Animations.CHARACTER_WALK_RIGHT.getKeyFrame(elapsedTime, true);
        } else if (xVelocity < 0) {
            // Player is moving to the left.
            return Animations.CHARACTER_WALK_LEFT.getKeyFrame(elapsedTime, true);
        } else {
            // If no movement, return the first frame of the walk down animation (idle state)
            return Animations.CHARACTER_WALK_DOWN.getKeyFrames()[0];
        }
    }

    /**
     * Marks the player as destroyed, disabling their hitbox and removing them from active play.
     * This method ensures that the player no longer interacts with other game objects
     * and can transition to a destruction or "game over" state.
     */
    public void destroy() {
        // Check if the player is already destroyed. If not, proceed with destruction.
        if (!destroyed) {
            MusicTrack.DEATH.play();
            destroyed = true;
            elapsedTime = 0;

            // Disable the hitbox to remove physical interactions with the game world.
            if (hitbox != null) {
                hitbox.setActive(false);
            }
        }
    }

    // Getter and Setter methods

    @Override
    public float getX() {
        // The x-coordinate of the player is the x-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().x;
    }

    @Override
    public float getY() {
        // The y-coordinate of the player is the y-coordinate of the hitbox (this can change every frame).
        return hitbox.getPosition().y;
    }

    /**
     * Retrieves the list of bombs available to the player.
     *
     * @return An ArrayList of  Bomb objects representing the player's bomb inventory.
     */
    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    /**
     * Stops the player's movement by setting their velocity to zero.
     * This method can be used to pause the player or prevent movement temporarily.
     */
    public void stopMovement() {
        this.hitbox.setLinearVelocity(0, 0); // Stop player movement
    }

    /**
     * Gets the blast radius of the player's bombs.
     *
     * @return The current blast radius of bombs.
     */
    public int getBlastRadius() {
        return bombs.get(0).getBlastRadius();
    }

    /**
     * Updates the blast radius of all bombs in the player's inventory.
     *
     * @param i The new blast radius to set for bombs.
     */
    public void setBlastRadius(int i) {
        for (Bomb bomb : bombs){
            bomb.setBlastRadius(i);
        }
    }

    public int getBombLimit() {
        return bombs.size();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Body getHitbox() {
        return hitbox;
    }

    public boolean isFullydestroyed() {
        return fullydestroyed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setBombLimit(int bombLimit) {
        this.bombLimit = bombLimit;
    }

    public int getMAX_ACTIVEBOMBS() {
        return MAX_ACTIVEBOMBS;
    }

    /**
     * Updates the maximum number of bombs the player can place at once.
     *
     * @param MAX_ACTIVEBOMBS The new maximum limit for active bombs.
     */

    public void setMAX_ACTIVEBOMBS(int MAX_ACTIVEBOMBS) {
        this.MAX_ACTIVEBOMBS = MAX_ACTIVEBOMBS;
    }
}
