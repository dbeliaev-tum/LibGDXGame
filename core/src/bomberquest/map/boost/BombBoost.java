package bomberquest.map.boost;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import bomberquest.map.GameMap;
import bomberquest.map.Player;
import bomberquest.texture.Textures;


/**
 * The BombBoost class represents a power-up that increases the player's
 * maximum number of active bombs. When collected, the player can place
 * one additional bomb simultaneously, up to a maximum of 8 bombs.
 */
public class BombBoost extends Boost{

    /** The texture used to represent the BombBoost power-up. */
    private static final TextureRegion explosionTexture = Textures.BombBoost;


    /**
     * Creates a new BombBoost power-up at the specified location.
     *
     * @param world   the game world
     * @param gameMap the game map
     * @param player  the player who can collect this power-up
     * @param x       the x-coordinate of the power-up
     * @param y       the y-coordinate of the power-up
     */
    public BombBoost(World world, GameMap gameMap, Player player, float x, float y) {
        super(world, gameMap, player, x, y);
        this.textures = explosionTexture;
    }

    /**
     * Activates the BombBoost power-up, increasing the player's maximum
     * number of active bombs by 1, up to a maximum limit of 8.
     */
    @Override
    public void activate() {
        if(player.getMAX_ACTIVEBOMBS() == 8){
            player.setMAX_ACTIVEBOMBS(8);
        }else {
            player.setMAX_ACTIVEBOMBS(player.getMAX_ACTIVEBOMBS() + 1);
        }
    }
}