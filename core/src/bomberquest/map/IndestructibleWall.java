package bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import bomberquest.texture.Textures;

/**
 * Represents an indestructible wall in the game.
 * Indestructible walls are static obstacles that cannot be destroyed by any means.
 */
public class IndestructibleWall extends Wall {

    /**
     * Constructs an indestructible wall at the specified coordinates.
     *
     * @param x the X-coordinate of the wall's position.
     * @param y the Y-coordinate of the wall's position.
     */
    public IndestructibleWall(float x, float y) {

        super(x, y, false);//Pass false to indicate that the wall is indestructible
    }

    /**
     * Overrides the destroy method to ensure no destruction behavior occurs.
     * Indestructible walls cannot be destroyed, so this method does nothing.
     */
    @Override
    public void destroy() {

    }

    /**
     * Returns the visual appearance (texture) of the indestructible wall.
     *
     * @return the texture representing the indestructible wall.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.INDESTRUCTIBLE_WALL;
    }

    /**
     * Indicates whether the wall is destructible.
     *
     * @return false, as indestructible walls cannot be destroyed.
     */
    @Override
    public boolean isDestructible() {
        return false;
    }
}
