package bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import bomberquest.texture.Drawable;
import bomberquest.texture.Textures;

/**
 * Flowers are a static object without any special properties.
 * They do not have a hitbox, so the player does not collide with them.
 * They are purely decorative and serve as a nice floor decoration.
 */
public class Flowers implements Drawable {

    private final int x;
    private final int y;

    /**
     * Constructs a new Flower object at the specified position.
     *
     * @param x the x-coordinate of the flower in the game world.
     * @param y the y-coordinate of the flower in the game world.
     */
    public Flowers(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the visual appearance of the flower.
     * The texture is a static image and does not change over time.
     *
     * @return a  TextureRegion representing the flower's appearance.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.FLOWERS;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
