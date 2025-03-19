package bomberquest.map.boost;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import bomberquest.map.GameMap;
import bomberquest.map.Player;
import bomberquest.texture.Drawable;


/**
 * Represents a power-up item in the game.
 * Boosts are hidden beneath destructible walls and can be revealed and collected by the player.
 * Subclasses define the specific behavior of each type of boost.
 */
public abstract class Boost implements Drawable {

    /** The Box2D body representing the physical presence of the boost in the game world. */
    private Body hitbox;

    /** Indicates whether the boost has been destroyed (collected or removed). */
    private boolean destroyed = false;

    /** The game map where the boost exists. */
    GameMap gameMap;

    /** The player who can collect and activate the boost. */
    Player player;
    /** The Box2D world where the boost resides. */
    World world;

    /** The texture representing the visual appearance of the boost. */
    TextureRegion textures;

    /** Indicates whether the boost is currently hidden (unrevealed). */
    private boolean hidden = true;


    /**
     * Constructs a new Boost object at the specified location.
     *
     * @param world   the Box2D world where the boost resides.
     * @param gameMap the game map containing the boost.
     * @param player  the player who can collect the boost.
     * @param x       the x-coordinate of the boost.
     * @param y       the y-coordinate of the boost.
     */
    public Boost(World world, GameMap gameMap, Player player, float x, float y) {
        this.world = world;
        this.hitbox = createHitbox(world, x, y);
        this.gameMap = gameMap;
        this.player = player;
    }


    /**
     * Creates a Box2D hitbox for the boost at the specified position.
     *
     * @param world   the Box2D world to create the hitbox in.
     * @param startX  the x-coordinate of the hitbox.
     * @param startY  the y-coordinate of the hitbox.
     * @return the created Box2D body.
     */
    private Body createHitbox(World world, float startX, float startY) {
        if (destroyed) return null;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(startX, startY);

        Body body = world.createBody(bodyDef);

        PolygonShape squareShape = new PolygonShape();
        squareShape.setAsBox(0.4f, 0.4f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = squareShape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        squareShape.dispose();
        body.setUserData(this);

        return body;
    }

    /**
     * Reveals the boost(power-up), making it visible and collectable.
     */
    public void reveal() {
        hidden = false; // Reveal the exit
    }


    /**
     * Destroys the boost, removing it from the game world.
     * This deactivates the boost and removes its hitbox.
     */
    public void destroy() {
        if (!destroyed) {
            destroyed = true;

            if (hitbox != null) {
                world.destroyBody(hitbox);
                hitbox = null;
            }
        }
    }

    /**
     * Activates the boost, applying its effect to the player.
     * Subclasses must implement this method to define specific behavior.
     */
    public abstract void activate();

    /**
     * Returns the current visual appearance of the boost.
     * If the boost is hidden or destroyed, this method returns null.
     *
     * @return the texture representing the boost, or null if hidden or destroyed.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        if (destroyed||hidden) {
            return null;
        }
        return textures;
    }


    /**
     * Hides the boost, making it invisible and uncollectible.
     * This method sets the `hidden` flag to true.
     */
    public void hide(){
        hidden=true;
    }


    /**
     * Returns the x-coordinate of the boost in the game world.
     *
     * @return the x-coordinate of the boost.
     */
    @Override
    public float getX() {
        return hitbox.getPosition().x;
    }

    /**
     * Returns the y-coordinate of the boost in the game world.
     *
     * @return the y-coordinate of the boost.
     */
    @Override
    public float getY() {
        return hitbox.getPosition().y;
    }

    /**
     * Checks whether the boost has been destroyed.
     *
     * @return true if the boost is destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return destroyed;
    }


    /**
     * Checks whether the boost is currently hidden.
     *
     * @return true if the boost is hidden, false otherwise.
     */
    public boolean isHidden() {
        return hidden;
    }


    /**
     * Sets the player associated with this boost.
     *
     * @param player the player to associate with this boost
     */
    public void setPlayer(Player player){
        this.player=player;
    }
}