package bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import bomberquest.texture.Drawable;

/**
 * Represents a base class for all objects in the game that have a fixed position
 * and can be drawn on the screen. This class provides common functionality for
 * game entities like walls, players, boosts, etc.
 */
public abstract class GameObject implements Drawable {

    /** The X-coordinate of the object in the game world. */
    protected final float x;

    /** The Y-coordinate of the object in the game world. */
    protected final float y;

    /**
     * Constructs a new GameObject at the specified position.
     *
     * @param x the X-coordinate of the object.
     * @param y the Y-coordinate of the object.
     */
    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the X-coordinate of the object.
     *
     * @return the X-coordinate of the object.
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * Returns the Y-coordinate of the object.
     *
     * @return the Y-coordinate of the object.
     */
    @Override
    public float getY() {
        return y;
    }

    /**
     * Gets the current appearance of the object as a texture region.
     * This is an abstract method and must be implemented by subclasses.
     *
     * @return the current appearance as a {@link TextureRegion}.
     */
    @Override
    public abstract TextureRegion getCurrentAppearance();
}
