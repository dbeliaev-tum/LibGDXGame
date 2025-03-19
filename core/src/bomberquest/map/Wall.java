package bomberquest.map;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Represents a base class for all walls in the game.
 * Walls can be either destructible or indestructible and may have hitboxes
 * for collision detection using the Box2D physics engine.
 */
public abstract class Wall extends GameObject {

    /** The Box2D body representing the wall's physical presence in the game world. */
    protected Body body;

    /** Indicates whether the wall is destructible or not. */
    private final boolean destructible;

    /** Tracks whether the wall has been destroyed. */
    private boolean destroyed = false; // Track destruction state


    /**
     * Constructs a wall at the specified position with the specified destructibility.
     *
     * @param x            the X-coordinate of the wall.
     * @param y            the Y-coordinate of the wall.
     * @param destructible true if the wall is destructible, false otherwise.
     */
    public Wall(float x, float y, boolean destructible) {
        super(x, y);
        this.destructible = destructible;
    }

    /**
     * Creates a Box2D hitbox for the wall in the specified physics world.
     * This hitbox defines the wall's collision properties.
     *
     * @param world the Box2D world where the wall's hitbox will be created.
     */
    public void createHitbox(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(this.x, this.y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f); // Creates a square hitbox (1x1 tiles)

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this); // Associate this wall with the body for collision handling
    }

    /**
     * Destroys the wall. This method must be implemented by subclasses to define specific destruction behavior.
     */
    public abstract void destroy();


    /**
     * Removes the wall's hitbox, cleaning up Box2D resources.
     */
    public void removeHitbox() {
        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }
    }

    /**
     * Checks whether the wall is fully destroyed.
     *
     * @return true if the wall is destroyed, false otherwise.
     */
    public boolean isFullyDestroyed() {
        return destroyed; // Use a `destroyed` flag to check if the wall is completely destroyed
    }

    /**
     * Updates the wall's state, typically called every frame.
     * Subclasses can override this to add animations or behaviors.
     *
     * @param deltaTime the time elapsed since the last update, in seconds.
     */
    public void update(float deltaTime) {
        // Optional: Add animation or behavior updates here if needed
    }

    /**
     * Checks whether the wall is destructible.
     * Subclasses must implement this method to specify the wall's destructibility.
     *
     * @return true if the wall is destructible, false otherwise.
     */
    public abstract boolean isDestructible();

    /**
     * Checks whether the wall has been destroyed.
     *
     * @return true if the wall is destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return destroyed;
    }
}