package bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import bomberquest.texture.Drawable;
import bomberquest.texture.Textures;

/**
 * Represents the exit point in the game.
 * The exit is initially hidden beneath a destructible wall and is revealed once the wall is destroyed.
 * It remains locked until all enemies are defeated, at which point it becomes accessible.
 */
public class Exit extends GameObject implements Drawable {

    /** Indicates whether the exit is hidden beneath a destructible wall. */
    private boolean hidden = true;

    /** Indicates whether the exit is unlocked (all enemies defeated). */
    private boolean unlocked = false;

    /** The Box2D hitbox representing the physical presence of the exit. */
    private Body hitbox;

    /** The world where the exit resides. */
    private World world;

    /**
     * Constructs an Exit object at the specified position in the given world.
     *
     * @param world the Box2D world where the exit resides.
     * @param x     the x-coordinate of the exit.
     * @param y     the y-coordinate of the exit.
     */
    public Exit(World world, float x, float y) {
        super(x,y);
        this.world = world;
        this.hitbox = createHitbox(world, x, y);
    }

    /**
     * Creates a Box2D hitbox for the exit at the specified position.
     *
     * @param world   the Box2D world where the hitbox is created.
     * @param startX  the x-coordinate of the hitbox.
     * @param startY  the y-coordinate of the hitbox.
     * @return the created Box2D body.
     */
    Body createHitbox(World world, float startX, float startY) {

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
     * Checks whether the exit is currently hidden.
     *
     * @return true if the exit is hidden, false otherwise.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Reveals the exit, making it visible and accessible to the player.
     */
    public void reveal() {
        hidden = false; // Reveal the exit
    }

    /**
     * Checks whether the exit is currently unlocked.
     *
     * @return true if the exit is unlocked, false otherwise.
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Unlocks the exit, allowing the player to access it.
     * When all enemies in the level are defeated.
     */
    public void unlock() {
        unlocked = true; // Unlock the exit
    }

    /**
     * Returns the current visual appearance of the exit.
     * If the exit is hidden, this method returns null.
     *
     * @return the texture representing the exit, or null if hidden.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        if (hidden) {
            return null;
        }
        if (Textures.EXIT == null) {
            System.out.println("Error: Exit texture not set!");
        }
        return Textures.EXIT;
    }

    /**
     * Returns the x-coordinate of the exit.
     *
     * @return the x-coordinate of the exit.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the exit.
     *
     * @return the y-coordinate of the exit.
     */
    public float getY() {
        return y;
    }
}