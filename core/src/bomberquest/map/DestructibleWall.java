package bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import bomberquest.map.boost.Boost;
import bomberquest.texture.Animations;
import bomberquest.texture.Textures;

/**
 * Represents a destructible wall in the game.
 * Destructible walls can be destroyed by explosions, revealing potential boosts or the exit.
 */
public class DestructibleWall extends Wall {

    /** Indicates whether the wall has been destroyed. */
    private boolean destroyed = false;

    /** Indicates whether the wall is fully destroyed (animation complete). */
    private boolean fullyDestroyed = false;

    /** Tracks the elapsed time since the wall was destroyed. */
    private float elapsedTime = 0f;

    /** Duration for which the destruction animation plays. */
    private float destructionTimer = 0.8f;

    /** Indicates whether the destruction animation is currently playing. */
    private boolean playingAnimation = false;

    /** Reference to the game  */
    private GameMap gameMap;

    /** A hidden boost that is revealed when the wall is destroyed. */
    private Boost hiddenBoost;

    /**
     * Constructs a destructible wall at the specified coordinates.
     *
     * @param x       the X-coordinate of the wall's position.
     * @param y       the Y-coordinate of the wall's position.
     * @param gameMap the game map where the wall resides.
     */
    public DestructibleWall( float x, float y,GameMap gameMap) {

        super(x, y, true);// Pass true to indicate that the wall is destructible
        this.gameMap=gameMap;
    }

    /**
     * Destroys the wall, initiating its destruction animation and revealing any hidden items.
     */
    @Override
    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            playingAnimation = true;
            elapsedTime = 0f;

            // Remove the physical hitbox
            removeHitbox();

            // Reveal the exit if it's hidden beneath this wall
            if (gameMap.getExit() != null && gameMap.getExit().getX() == x && gameMap.getExit().getY() == y) {
                gameMap.getExit().reveal();
            }
            // Reveal the hidden boost
            if (hiddenBoost != null) {
                hiddenBoost.reveal();
            }
        }
    }

    /**
     * Updates the wall's state, handling destruction animation and full removal.
     *
     * @param deltaTime the time elapsed since the last update, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        if (destroyed) {
            elapsedTime += deltaTime;

            // Check if the destruction animation is finished
            if (playingAnimation) {
                if (Animations.WALL_DESTRUCTION.isAnimationFinished(elapsedTime)) {
                    playingAnimation = false;
                    fullyDestroyed = true; // Mark the wall as fully destroyed
                }
            }

            // Remove the wall after the destruction timer elapses
            destructionTimer -= deltaTime;
            if (destructionTimer <= 0 && !fullyDestroyed) {
                fullyDestroyed = true;
            }
        }
    }

    /**
     * Returns the current appearance (texture or animation frame) of the wall.
     *
     * @return the texture or animation frame representing the wall's current state.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        if (destroyed) {
            if (playingAnimation) {
                return Animations.WALL_DESTRUCTION.getKeyFrame(elapsedTime, false);
            }
            return Textures.FLOWERS; // Texture displayed after destruction
        }
        return Textures.DESTRUCTIBLE_WALL; // Default texture for the wall
    }

    /**
     * Checks whether the wall is fully destroyed (animation complete).
     *
     * @return true if the wall is fully destroyed, false otherwise.
     */
    @Override
    public boolean isFullyDestroyed() {
        return fullyDestroyed;
    }

    /**
     * Indicates that the wall is destructible.
     *
     * @return true, as this wall can be destroyed.
     */
    @Override
    public boolean isDestructible() {
        return true;
    }

    /**
     * Sets a hidden boost under the wall, which will be revealed upon destruction.
     *
     * @param boost the boost to be hidden under the wall.
     */
    public void setHiddenBoost(Boost boost) {
        this.hiddenBoost = boost;
        if (hiddenBoost != null) {
            hiddenBoost.hide(); // Hide the boost until the wall is destroyed
        }
    }

    /**
     * Retrieves the hidden boost under the wall.
     *
     * @return the hidden boost, or null if none exists.
     */
    public Boost getHiddenBoost() {
        return hiddenBoost;
    }
}