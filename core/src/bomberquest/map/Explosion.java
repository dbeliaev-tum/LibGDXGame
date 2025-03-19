package bomberquest.map;

import bomberquest.audio.MusicTrack;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import bomberquest.texture.Animations;
import bomberquest.texture.Drawable;
import java.util.ArrayList;
import java.util.List;
import static bomberquest.screen.GameScreen.SCALE;
import static bomberquest.screen.GameScreen.TILE_SIZE_PX;


/**
 * The Explosion class represents the behavior and visual representation of an explosion in the game.
 * Explosions occur when a bomb explodes, affecting multiple tiles depending on the blast radius.
 * Each explosion is made up of segments that propagate outward in four directions:
 * up, down, left, and right.
 */
public class Explosion implements Drawable {

    /** The x- and y-coordinates of the bomb in the game world. */
    private float x, y;

    /** Indicates whether the explosion is currently active. */
    private boolean active;

    /** Timer for managing the explosion's lifetime. */
    private float timer;

    /** The total duration of the explosion, in seconds. */
    private final float LIFETIME = 0.65f;

    /** Tracks the total elapsed time for animation purposes. */
    private float elapsedTime;

    /** The blast radius of the explosion, defining its range in tiles. */
    private int blastRadius = 1;

    /** A list of segments representing the explosion's reach in all directions. */
    private List<ExplosionSegment> segments;

    /** The game map where the explosion occurs. */
    private GameMap gameMap;

    /** The Box2D physics world for managing collisions. */
    World world;

    /**
     * Constructs a new Explosion object.
     *
     * @param world   the Box2D physics world where the explosion resides.
     * @param gameMap the game map that the explosion interacts with.
     */
    public Explosion(World world, GameMap gameMap) {
        this.x = -100;
        this.y = -100; //Default position: off-screen
        this.active = false;
        this.timer = 0;
        this.elapsedTime = 0;
        this.segments = new ArrayList<>();
        this.world = world;
        this.gameMap = gameMap;
    }

    /**
     * Activates the explosion and positions it based on the given coordinates.
     * @param x The x-coordinate to position the explosion(the center)
     * @param y The y-coordinate to position the explosion(the center)
     */

    public void activate(float x, float y) {
        this.x = x;
        this.y = y;
        this.active = true;
        this.timer = 0;
        this.elapsedTime = 0;

        MusicTrack.EXPLOSION.play();
        generateExplosionSegments();
    }

    /**
     * Generates the segments of the explosion, including its center and propagation in all directions.
     */
    private void generateExplosionSegments() {
        segments.clear();

        // Center segment of the explosion.
        segments.add(new ExplosionSegment(x, y, Animations.EXPLOSION_CENTER));

        // Propagate in all four directions.
        generateDirectionalSegments(0, 1, Animations.EXPLOSION_UP, Animations.EXPLOSION_END_UP);
        generateDirectionalSegments(0, -1, Animations.EXPLOSION_DOWN, Animations.EXPLOSION_END_DOWN);
        generateDirectionalSegments(-1, 0, Animations.EXPLOSION_LEFT, Animations.EXPLOSION_END_LEFT);
        generateDirectionalSegments(1, 0, Animations.EXPLOSION_RIGHT, Animations.EXPLOSION_END_RIGHT);
    }

    /**
     * Generates explosion segments in a specific direction.
     *
     * @param dx       the horizontal step (1 for right, -1 for left, 0 for no movement).
     * @param dy       the vertical step (1 for up, -1 for down, 0 for no movement).
     * @param midAnim  the animation for intermediate segments.
     * @param endAnim  the animation for the final segment in the direction.
     */
    private void generateDirectionalSegments(int dx, int dy, Animation<TextureRegion> midAnim, Animation<TextureRegion> endAnim) {
        for (int i = 1; i <= blastRadius; i++) {
            float nx = x + i * dx;
            float ny = y + i * dy;

            // Check if the tile is blocked.
            if (gameMap.isBlocked(nx, ny)) {
                segments.add(new ExplosionSegment(nx, ny, endAnim));
                break; // Stop propagation in this direction.
            }

            if (i == blastRadius) {
                segments.add(new ExplosionSegment(nx, ny, endAnim));
            } else {
                segments.add(new ExplosionSegment(nx, ny, midAnim));
            }
        }
    }

    /**
     * Updates the state of the explosion, managing its lifetime and animations.
     *
     * @param dt the time elapsed since the last frame, in seconds.
     */
    public void tick(float dt) {
        if (active) {
            timer += dt;
            elapsedTime += dt;
            if (timer >= LIFETIME) {
                active = false;
            }
        }
    }

    /**
     * Retrieves the current visual appearance of the explosion.
     * This method returns the animation frame for the center segment of the explosion
     * based on the elapsed time, if the explosion has active segments.
     *
     * @return a TextureRegion representing the current appearance of the explosion,
     *         or null, if there are no active segments or animations.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        if (!segments.isEmpty()) {
            ExplosionSegment centerSegment = segments.get(0); // Center segment
            if (centerSegment != null && centerSegment.animation != null) {
                return centerSegment.animation.getKeyFrame(elapsedTime, false);
            }
        }
        return null; // No active segments.
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Renders all segments of the explosion.
     *
     * @param batch the SpriteBatch used to render the explosion segments.
     */
    public void render(SpriteBatch batch) {
        if (active) {
            for (ExplosionSegment segment : segments) {
                if (segment != null) {
                    segment.render(batch, elapsedTime); // Render each segment.
                }
            }
        }
    }

    /**
     * Gets the blast radius of the explosion.
     *
     * @return the blast radius.
     */
    public int getBlastRadius() {
        return blastRadius;
    }

    /**
     * Sets the blast radius of the explosion.
     *
     * @param blastRadius the new blast radius.
     */
    public void setBlastRadius(int blastRadius) {
        this.blastRadius = blastRadius;
    }


    /**
     * Represents a single segment of the explosion.
     * Each segment is associated with a specific position and animation.
     */
    public class ExplosionSegment {
        private float x, y;
        private Animation<TextureRegion> animation;

        /**
         * Constructs an ExplosionSegment.
         *
         * @param x         the x-coordinate of the segment.
         * @param y         the y-coordinate of the segment.
         * @param animation the animation associated with the segment.
         */
        public ExplosionSegment(float x, float y, Animation<TextureRegion> animation) {
            this.x = x;
            this.y = y;
            this.animation = animation;
        }

        /**
         * Renders the explosion segment.
         *
         * @param batch       the SpriteBatch used for rendering.
         * @param elapsedTime the elapsed time for animation purposes.
         */
        public void render(SpriteBatch batch, float elapsedTime) {
            TextureRegion frame = animation.getKeyFrame(elapsedTime, false); // Get the current animation frame.
            if (frame != null) {
                // Convert coordinates to pixels.
                float pixelX = x * TILE_SIZE_PX * SCALE;
                float pixelY = y * TILE_SIZE_PX * SCALE;
                float width = frame.getRegionWidth() * SCALE;
                float height = frame.getRegionHeight() * SCALE;

                batch.draw(frame, pixelX, pixelY, width, height);  // Draw the animation frame.
            } else {
                System.out.println("Error: TextureRegion is null for explosion segment.");
            }
        }
    }
}
