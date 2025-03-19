package bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {

    public static final TextureRegion FLOWERS = SpriteSheet.BASIC_TILES.at(2, 4);

    public static final TextureRegion ExplosionBoost = SpriteSheet.ORIGINAL_BOMBERMAN.at(15, 2);
    public static final TextureRegion SpeedBoost = SpriteSheet.ORIGINAL_BOMBERMAN.at(15, 4);
    public static final TextureRegion GhostSpeedBoost = SpriteSheet.ORIGINAL_BOMBERMAN.at(15, 8);
    public static final TextureRegion BombBoost = SpriteSheet.ORIGINAL_BOMBERMAN.at(15, 1);

    //now we add the exit
     public static final TextureRegion EXIT = SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 12);

    //now we add the destructible and indestructible walls
    public static final TextureRegion INDESTRUCTIBLE_WALL = SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 4); // Update coordinates as needed
    public static final TextureRegion DESTRUCTIBLE_WALL = SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 5);   // Update coordinates as needed
}
