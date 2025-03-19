package bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all animation constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Animations {

    /**
     * The animation for the character walking down.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_DOWN = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(1, 1),
            SpriteSheet.CHARACTER.at(1, 2),
            SpriteSheet.CHARACTER.at(1, 3),
            SpriteSheet.CHARACTER.at(1, 4)
    );

    /**
     * The animation for the character walking up.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_UP = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(3, 1),
            SpriteSheet.CHARACTER.at(3, 2),
            SpriteSheet.CHARACTER.at(3, 3),
            SpriteSheet.CHARACTER.at(3, 4)
    );

    /**
     * The animation for the character walking left.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_LEFT = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(4, 1),
            SpriteSheet.CHARACTER.at(4, 2),
            SpriteSheet.CHARACTER.at(4, 3),
            SpriteSheet.CHARACTER.at(4, 4)
    );

    /**
     * The animation for the character walking right.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_RIGHT = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(2, 1),
            SpriteSheet.CHARACTER.at(2, 2),
            SpriteSheet.CHARACTER.at(2, 3),
            SpriteSheet.CHARACTER.at(2, 4)
    );

    /**
     * The animation for the character's death sequence.
     */
    public static final Animation<TextureRegion> CHARACTER_DIES = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(4, 8),
            SpriteSheet.CHARACTER.at(4, 7),
            SpriteSheet.CHARACTER.at(4, 6)
    );

    /**
     * The animation for a bomb ticking before it explodes.
     */
    public static final Animation<TextureRegion> BOMB = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 1),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 2),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 3)
    );

    /**
     * The animation for the explosion center.
     * This is displayed at the bomb's position when it explodes.
     */
    public static final Animation<TextureRegion> EXPLOSION_CENTER = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 3)
    );

    /**
     * The animation for the explosion spreading to the left.
     */
    public static final Animation<TextureRegion> EXPLOSION_LEFT = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 2),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 7),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 2),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 7),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 2),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 7),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 2)
    );

    /**
     * The animation for the explosion spreading to the right.
     */
    public static final Animation<TextureRegion> EXPLOSION_RIGHT = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 4),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 9),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 4),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 9),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 4),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 9),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 4)
    );

    /**
     * The animation for the explosion spreading upward.
     */
    public static final Animation<TextureRegion> EXPLOSION_UP = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(6, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(6, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(11, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(11, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(11, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(6, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(6, 3)
    );

    /**
     * The animation for the explosion spreading downward.
     */
    public static final Animation<TextureRegion> EXPLOSION_DOWN = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(8, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(8, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(13, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(13, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(13, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(8, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(8, 3)
    );

    /**
     * The animation for the upward end of an explosion.
     */
    public static final Animation<TextureRegion> EXPLOSION_END_UP = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(5, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(5, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(10, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(10, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(10, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(5, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(5, 3)
    );

    /**
     * The animation for the downward end of an explosion.
     */
    public static final Animation<TextureRegion> EXPLOSION_END_DOWN = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(9, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(9, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(14, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(14, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(14, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(9, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(9, 3)
    );


    /**
     * The animation for the left end of an explosion.
     */
    public static final Animation<TextureRegion> EXPLOSION_END_LEFT = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 1),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 6),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 1),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 6),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 1),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 6),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 1)
    );


    /**
     * The animation for the right end of an explosion.
     */
    public static final Animation<TextureRegion> EXPLOSION_END_RIGHT = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 5),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 10),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 5),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 10),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(12, 5),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 10),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(7, 5)
    );


    /**
     * The animation for a wall being destroyed.
     */
    public static final Animation<TextureRegion> WALL_DESTRUCTION = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 6),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 7),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 9),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 10),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 11)
    );

    /**
     * The animation for the ghost walking left.
     */
    public static final Animation<TextureRegion> GHOST_WALK_LEFT = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(20, 4),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(20, 5),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(20, 6)
    );

    /**
     * The animation for the ghost walking right.
     */
    public static final Animation<TextureRegion> GHOST_WALK_RIGHT = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(20, 1),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(20, 2),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(20, 3)
    );

    /**
     * The animation for the ghost destruction.
     */
    public static final Animation<TextureRegion> GHOST_GET_DISTRACTED = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(20, 7),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(19, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(19, 9),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(19, 10),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(19, 11)
    );

}
