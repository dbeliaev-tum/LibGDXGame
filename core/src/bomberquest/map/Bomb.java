package bomberquest.map;

import bomberquest.audio.MusicTrack;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import bomberquest.texture.Animations;
import bomberquest.texture.Drawable;

/**
 * Represents a bomb in the game. The bomb can be placed on the map, activates, ticks for a set duration,
 * and then explodes. It uses Box2D for collision detection and triggers an explosion when its timer ends.
 */
public class Bomb implements Drawable {

    /** The x- and y-coordinates of the bomb in the game world. */
    private float x, y;

    /** Indicates whether the bomb is currently active (visible and ticking). */
    private boolean active;

    /** Tracks whether the bomb was ever active during its lifecycle. */
    private boolean wasActive = false;

    /** Tracks the time passed since the bomb was activated. */
    private float timer;

    /** The total lifetime of the bomb before it explodes, in seconds. */
    private final float LIFETIME = 3.0f;

    /** Tracks the total elapsed(passed) time for animation purposes. */
    private float elapsedTime;

    /** The explosion triggered by the bomb after its lifetime ends. */
    private Explosion explosion;

    /** The Box2D hitbox representing the bomb's collision area. */
    private Body hitbox;

    /** The world the bomb interacts with. */
    private World world;

    /** The game map where the bomb exists and interacts with other objects. */
    private final GameMap gameMap;

    /**
     * Constructs a new bomb object.
     * @param world   the Box2D physics world where the bomb resides.
     * @param gameMap the game map the bomb interacts with.
     */
    public Bomb(World world, GameMap gameMap) {
        // bomb's position in the game world (grid coordinates) -Default value: -100 ->off-screen
        this.x = -100;
        this.y = -100;

        this.active = false;
        this.timer = 0;
        this.elapsedTime = 0;

        this.world = world;
        this.explosion = new Explosion(world, gameMap);
        this.gameMap = gameMap;
    }

    /**
     * Creates a Box2D hitbox for the bomb at the specified position.
     * @param world  the Box2D world to create the hitbox in.
     * @param startX the x-coordinate of the hitbox.
     * @param startY the y-coordinate of the hitbox.
     * @return the created Box2D body representing the bomb's hitbox.
     */
    private Body createHitbox(World world, float startX, float startY) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(startX, startY);

        Body body = world.createBody(bodyDef);

        PolygonShape squareShape = new PolygonShape();
        squareShape.setAsBox(0.4f, 0.4f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = squareShape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        squareShape.dispose();
        body.setUserData(this);

        return body;
    }

    /**
     * Activates the bomb at the specified position.
     * The bomb becomes visible, starts its timer, and begins ticking(sound).
     *
     * @param x the x-coordinate where the bomb is activated.
     * @param y the y-coordinate where the bomb is activated.
     */
    public void activate(float x, float y) {
        this.x = x;
        this.y = y;
        this.hitbox = createHitbox(world, x, y);
        active = true;
        wasActive = true;
        timer = 0;
        elapsedTime = 0;
        MusicTrack.BOMB.play();
    }

    /**
     * Updates the bomb's state every frame, ticking its timer and triggering an explosion
     * when its lifetime ends. Also updates the explosion's state if active.
     * @param dt the time elapsed since the last frame, in seconds.
     */
    public void tick(float dt) {
        if (active) {
            timer += dt/2;
            elapsedTime += dt/2;
            if (timer >= LIFETIME) {
                active = false;
                explosion.activate(x, y);
                gameMap.explodeBomb(this); // Trigger explosion logic in the game map.
                if (hitbox != null) {
                    world.destroyBody(hitbox);
                    hitbox = null;
                }
            }
        }
        explosion.tick((dt/2)); // Update explosion's state.
    }

    /**
     * Returns the current appearance of the bomb.
     * If the bomb is active, it returns the appropriate animation frame.
     * If the bomb has exploded, it returns the explosion's appearance.
     * @return the texture region representing the bomb's current state.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        if (active) {
            return Animations.BOMB.getKeyFrame(elapsedTime, true);
        }

        if (explosion.isActive()) {
            return explosion.getCurrentAppearance();
        }

        return null;
    }

    /**
     * Gets the x-coordinate of the bomb in the game world.
     * @return the x-coordinate of the bomb.
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the bomb in the game world.
     *
     * @return the y-coordinate of the bomb.
     */
    @Override
    public float getY() {
        return y;
    }

    /**
     * Checks if the bomb is currently active.
     *
     * @return true if the bomb is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Gets the explosion object associated with this bomb.
     *
     * @return the explosion object.
     */
    public Explosion getExplosion() {
        return explosion;
    }

    /**
     * Gets the blast radius of the bomb's explosion.
     *
     * @return the blast radius of the bomb's explosion.
     */
    public int getBlastRadius() {
        return explosion.getBlastRadius();
    }

    /**
     * Sets the blast radius of the bomb's explosion.
     *
     * @param blastRadius the new blast radius.
     */
    public void setBlastRadius(int blastRadius) {
        explosion.setBlastRadius(blastRadius);
    }

    /**
     * Checks if the bomb was ever active during its lifecycle.
     *
     * @return true if the bomb was active at some point, false otherwise.
     */
    public boolean wasActive() {
        return wasActive;
    }

    /**
     * Sets the "was active" flag for the bomb.
     *
     * @param wasActive the new value for the "was active" flag.
     */
    public void setWasActive(boolean wasActive) {
        this.wasActive = wasActive;
    }
}
