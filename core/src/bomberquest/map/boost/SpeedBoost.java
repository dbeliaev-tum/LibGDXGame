package bomberquest.map.boost;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import bomberquest.map.GameMap;
import bomberquest.map.Player;
import bomberquest.texture.Textures;

/**
 * Represents a power-up that increases the player's movement speed.
 * When collected, the player's speed is incremented, allowing them to move faster.
 */
public class SpeedBoost extends Boost{
    private static final TextureRegion explosionTexture = Textures.SpeedBoost;

    /**
     * Initializes a SpeedBoost power-up at the specified location.
     *
     * @param world   the physics world where the power-up exists
     * @param gameMap the map where the power-up is located
     * @param player  the player who can collect and benefit from this power-up
     * @param x       the horizontal position of the power-up on the map
     * @param y       the vertical position of the power-up on the map
     */
    public SpeedBoost(World world, GameMap gameMap, Player player, float x, float y) {
        super(world, gameMap, player, x, y);
        this.textures = explosionTexture;
    }

    /**
     * Activates the SpeedBoost, increasing the player's movement speed by 1.
     */
    @Override
    public void activate() {
        player.setSpeed(player.getSpeed() + 1);
    }
}