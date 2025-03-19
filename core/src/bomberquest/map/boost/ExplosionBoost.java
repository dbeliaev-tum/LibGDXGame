package bomberquest.map.boost;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import bomberquest.map.GameMap;
import bomberquest.map.Player;
import bomberquest.texture.Textures;

/**
 * The ExplosionBoost class represents a power-up that increases the player's
 * bomb blast radius. When collected, the blast radius of the player's bombs
 * increases by one tile.
 */
public class ExplosionBoost extends Boost{

    /** The texture representing the ExplosionBoost power-up. */
    private static final TextureRegion explosionTexture = Textures.ExplosionBoost;

    /**
     * Constructs an ExplosionBoost at the specified location on the map.
     *
     * @param world   the physics world where the power-up exists
     * @param gameMap the map where the power-up is placed
     * @param player  the player who can collect and benefit from this power-up
     * @param x       the horizontal position of the power-up on the map
     * @param y       the vertical position of the power-up on the map
     */
    public ExplosionBoost(World world, GameMap gameMap, Player player, float x, float y) {
        super(world, gameMap, player, x, y);
        this.textures = explosionTexture;
    }

    /**
     * Activates the ExplosionBoost, increasing the player's bomb blast radius
     * by one tile. If the maximum blast radius (8 tiles) is already reached,
     * no further increase is applied.
     */
    @Override
    public void activate() {
        if(player.getBlastRadius() == 8){
            player.setBlastRadius(8);
        }else {
            player.setBlastRadius(player.getBlastRadius() + 1);
        }
    }
}
