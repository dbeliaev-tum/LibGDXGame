package bomberquest.map;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import bomberquest.texture.Animations;
import bomberquest.texture.Drawable;

/**
 * Represents a ghost enemy in the game.
 * The ghost chases the player and can be destroyed.
 */
public class Ghost implements Drawable {

    /** The ghost's physical hitbox in the game world. */
    private Body hitbox;

    /** Whether the ghost is destroyed and inactive. */
    private boolean destroyed;

    /** Whether the ghost is fully removed from the game. */
    private boolean fullydestroyed;

    /** The ghost's movement speed. */
    private float speed = 20.0f;

    /** Tracks time for animations and destruction. */
    private float elapsedTime;

    /** Time before the ghost is fully removed after being destroyed. */
    float destructionTimer = 0.4f;

    /**
     * Creates a ghost at the given position.
     *
     * @param world  The Box2D world the ghost belongs to.
     * @param startX The starting X position.
     * @param startY The starting Y position.
     */
    public Ghost(World world, float startX, float startY) {
        this.hitbox = createHitbox(world, startX, startY);
        this.elapsedTime = 0;
        this.destroyed = false;
        this.fullydestroyed = false;
    }

    /**
     * Creates a Box2D body for the ghost.
     * @param world The Box2D world to add the body to.
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @return The created body(hitbox) for the ghost.
     */
    private Body createHitbox(World world, float startX, float startY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Set as a dynamic body to allow movement.
        bodyDef.position.set(startX, startY); // Set the initial position.

        Body body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape(); // Define a circular shape for the ghost's hitbox.
        circle.setRadius(0.3f); // Set the radius of the circular hitbox.

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = false; // Allow collisions.
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;

        body.createFixture(fixtureDef); // Attach the fixture to the body.

        circle.dispose();// Dispose of the shape after use.

        body.setUserData(this);// Set this ghost instance as the user data for the body.
        return body;
    }

    /**
     * Updates the ghost's position to chase the player.
     * If the ghost is destroyed, it counts down the destruction timer and removes itself when time is up.
     *
     * @param player    The player the ghost is chasing.
     * @param deltaTime The time elapsed since the last update (in seconds).
     */
    public void update(Player player, float deltaTime) {
        if (destroyed) {
            destructionTimer -= deltaTime;
            elapsedTime += deltaTime;

            // Fully remove the ghost when the destruction timer expires
            if (destructionTimer <= 0 && !fullydestroyed) {
                if (hitbox != null) {
                    hitbox.getWorld().destroyBody(hitbox);
                    hitbox = null;
                }
                fullydestroyed = true;
            }
            return; // Skip movement logic if the ghost is destroyed.
        }

        this.elapsedTime += deltaTime;

        // Calculate direction to the player
        float playerX = player.getX();
        float playerY = player.getY();

        float dx = playerX - hitbox.getPosition().x;
        float dy = playerY - hitbox.getPosition().y;

        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance != 0) { // Normalize direction
            dx /= distance;
            dy /= distance;
        }

        // Set the ghost's velocity towards the player
        hitbox.setLinearVelocity(dx * speed * deltaTime, dy * speed * deltaTime);
    }

    /**
     * Marks the ghost as destroyed, stopping its movement and interactions.
     */
    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            elapsedTime = 0;
            if (hitbox != null) {
                hitbox.setActive(false); // Деактивируем, чтобы убрать взаимодействие
            }
        }
    }

    /**
     * Retrieves the current texture for the ghost based on its state and direction.
     * The ghost's texture depends on whether it is destroyed or moving.
     *
     * @return The current TextureRegion for Ghost
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        if (destroyed) {
            return Animations.GHOST_GET_DISTRACTED.getKeyFrame(elapsedTime, false); // Destruction animation.
        }

        float xVelocity = hitbox.getLinearVelocity().x; // Check movement direction.

        if (xVelocity < 0) {
            return Animations.GHOST_WALK_LEFT.getKeyFrame(elapsedTime, true); // Left movement animation.
        } else {
            return Animations.GHOST_WALK_RIGHT.getKeyFrame(elapsedTime, true); // Right movement animation.
        }
    }

    /**
     * @return The X position of the ghost.
     */
    @Override
    public float getX() {
        return hitbox.getPosition().x;
    }

    /**
     * @return The Y position of the ghost.
     */
    @Override
    public float getY() {
        return hitbox.getPosition().y;
    }

    /**
     * @return True if the ghost is destroyed.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * @return True if the ghost is fully removed.
     */
    public boolean isFullydestroyed() {
        return fullydestroyed;
    }

    /**
     * @return The ghost's hitbox.
     */
    public Body getHitbox() {
        return hitbox;
    }

    /**
     * @return The ghost's movement speed.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the ghost's movement speed.
     *
     * @param speed The new speed value.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
