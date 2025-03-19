package bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A Heads-Up Display (HUD) that displays information on the screen.
 * It uses a separate camera so that it is always fixed on the screen.
 */
public class Hud {

    /** The SpriteBatch used to draw the HUD. This is the same as the one used in the GameScreen. */
    private final SpriteBatch spriteBatch;
    /** The font used to draw text on the screen. */
    private final BitmapFont font;
    /** The camera used to render the HUD. */
    private final OrthographicCamera camera;

    /** Text showing the current bomb blast radius. */
    private String bombRadiusText = "Blast Radius: 1";

    /** Text showing the current bomb limit. */
    private String bombLimitText = "Bomb Limit: 1";

    /** Text showing the remaining time in the game. */
    private String timerText = "Time Left: 300";

    /** Text showing the number of enemies left. */
    private String enemiesLeftText = "Enemies Left: 10";

    /** Text indicating whether the exit has been unlocked. */
    private String exitUnlockedText = "Exit Unlocked: No";


    /**
     * Constructs the HUD with the given SpriteBatch and BitmapFont.
     *
     * @param spriteBatch The SpriteBatch used for rendering the HUD.
     * @param font        The font used to render text on the screen.
     */
    public Hud(SpriteBatch spriteBatch, BitmapFont font) {
        this.spriteBatch = spriteBatch;
        this.font = font;
        this.camera = new OrthographicCamera();
    }

    /**
     * Renders the HUD on the screen.
     * The HUD is displayed using a fixed camera, ensuring it stays in place regardless of game camera movements.
     */
    public void render() {
        // Render from the camera's perspective
        spriteBatch.setProjectionMatrix(camera.combined);

        // Start drawing
        spriteBatch.begin();

        // Display HUD elements dynamically with proper spacing
        font.setColor(Color.WHITE);
        font.draw(spriteBatch, bombRadiusText, 10, Gdx.graphics.getHeight() - 30); // 30 px from the top
        font.setColor(Color.WHITE);
        font.draw(spriteBatch,  bombLimitText, 10, Gdx.graphics.getHeight() - 70);  // 40 px below
        font.setColor(Color.WHITE);
        font.draw(spriteBatch,  timerText, 10, Gdx.graphics.getHeight() - 110);  // 40 px below
        font.setColor(Color.WHITE);
        font.draw(spriteBatch,  enemiesLeftText, 10, Gdx.graphics.getHeight() - 150); // 40 px below
        font.setColor(Color.WHITE);
        font.draw(spriteBatch, exitUnlockedText, 10, Gdx.graphics.getHeight() - 190); // 40 px below
        font.setColor(Color.WHITE);
        font.draw(spriteBatch, "Press Esc to Pause!", 10, Gdx.graphics.getHeight() - 230);  // 40 px below

        // Finish drawing
        spriteBatch.end();
    }

    /**
     * Resizes the HUD when the screen size changes.
     * This is called when the window is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    /**
     * Updates the HUD elements with the current game state.
     *
     * @param bombRadius  The player's current bomb blast radius.
     * @param bombLimit   The maximum number of bombs the player can place.
     * @param timeLeft    The remaining time in the game.
     * @param enemiesLeft The number of enemies left in the game.
     * @param exitUnlocked Whether the exit is unlocked.
     */
    public void updateHud(int bombRadius, int bombLimit, int timeLeft, int enemiesLeft, boolean exitUnlocked) {
        this.bombRadiusText = "Blast Radius: " + bombRadius;
        this.bombLimitText = "Bomb Limit: " + bombLimit;
        this.timerText = "Time Left: " + timeLeft;
        this.enemiesLeftText = "Enemies Left: " + enemiesLeft;
        this.exitUnlockedText = "Exit Unlocked: " + (exitUnlocked ? "Yes" : "No");
    }
}
