package bomberquest.map.boost;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import bomberquest.map.GameMap;
import bomberquest.map.Ghost;
import bomberquest.map.Player;
import bomberquest.texture.Textures;

import java.util.List;

/**
 * The GhostSpeedBoost class represents a power-up that reduces the speed of
 * all ghosts in the game. When collected, the speed of each ghost is halved,
 * making it easier for the player to evade them.
 */
public class GhostSpeedBoost extends Boost{

    /** The texture representing the GhostSpeedBoost power-up. */
    private static final TextureRegion explosionTexture = Textures.GhostSpeedBoost;

    /** The list of ghosts whose speed will be affected by this power-up. */
    private List<Ghost> ghosts;


    /**
     * Constructs a GhostSpeedBoost at the specified location on the map.
     *
     * @param world   the physics world where the power-up exists
     * @param gameMap the map where the power-up is placed
     * @param player  the player who can collect and benefit from this power-up
     * @param ghosts  the list of ghosts whose speed will be reduced
     * @param x       the horizontal position of the power-up on the map
     * @param y       the vertical position of the power-up on the map
     */
    public GhostSpeedBoost(World world, GameMap gameMap, Player player, List<Ghost> ghosts, float x, float y) {
        super(world, gameMap, player, x, y);
        this.ghosts = ghosts;
        this.textures = explosionTexture;
    }

    /**
     * Activates the GhostSpeedBoost power-up, reducing the speed of all ghosts
     * in the game by half. This effect makes the ghosts slower and easier to avoid.
     */
    @Override
    public void activate() {
        for (Ghost ghost : ghosts){
            ghost.setSpeed(ghost.getSpeed() / 2);
        }
    }
}