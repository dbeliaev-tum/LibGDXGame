package bomberquest.screen;

import bomberquest.map.*;
import bomberquest.map.boost.Boost;
import bomberquest.texture.Drawable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import bomberquest.*;

import java.util.List;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    /**
     * The size of a grid cell in pixels.
     * This allows us to think of coordinates in terms of square grid tiles
     * (e.g. x=1, y=1 is the bottom left corner of the map)
     * rather than absolute pixel coordinates.
     */
    public static final int TILE_SIZE_PX = 16;

    /**
     * The scale of the game.
     * This is used to make everything in the game look bigger or smaller.
     */
    public static final int SCALE = 4;

    /**
     * Game dependencies
     */
    private final BomberQuestGame game;//Main game instance
    private final SpriteBatch spriteBatch;//for rendering all elements on te screen
    private final GameMap map;//the current gameMap and its objects
    private final Hud hud;//Heads-up display for players

    /**
     * Camera and rendering
     */
    private final OrthographicCamera mapCamera; // Camera that follows the player and determines the visible game area.
    private Stage stage; // Stage used for managing additional UI components

    /**
     * Game state
     */
    private List<Ghost> ghosts; //list of ghosts currently present in the game
    private int countdownTimer;//the remaining time in the game
    private List<Boost> boosts; //list to define the boosts



    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(BomberQuestGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        this.map = game.getMap();
        this.hud = new Hud(spriteBatch, game.getSkin().getFont("font"));
        // Create and configure the camera for the game view
        this.mapCamera = new OrthographicCamera();
        this.mapCamera.setToOrtho(false);
        this.stage = new Stage(new ScreenViewport());
    }

    /**
     * The render method is called every frame to render the game.
     *
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Handle win
        if (map.hasWon()) {
            game.goToVictoryScreen(); // Use the helper method to switch to the Victory screen
            return;
        }
        //Handle loss
        if (map.hasLost()) {
            game.goToGameOverScreen(); // Use the helper method to switch to the Game Over screen
            return;
        }
        // Clear the previous frame from the screen, or else the picture smears
        ScreenUtils.clear(Color.BLACK);

        // Cap frame time to 250ms to prevent spiral of death
        float frameTime = Math.min(deltaTime, 0.250f);

        // Update the map state
        map.tick(frameTime);

        // Update all bombs and explosions
        for (Bomb bomb : map.getPlayer().getBombs()) {
            bomb.tick(frameTime); // Update the state of bombs and explosions
        }

        // Update the camera
        updateCamera();

        // Render the map on the screen
        renderMap();

        // Render the HUD on the screen
        hud.render();

        // Update the HUD with the latest game state - where do i put this
        hud.updateHud(
                map.getPlayer().getBlastRadius(), // Update blast radius dynamically
                map.getPlayer().getMAX_ACTIVEBOMBS(),            // Fetch the current bomb limit
                map.getCountdownTimer(),                   // Countdown timer from GameMap
                map.getGhostsLeft(),                      // Remaining enemies from GameMap
                map.isExitUnlocked()                       // Whether the exit is unlocked
        );

    }

    /**
     * Updates the camera to match the current state of the game.
     * Currently, this just centers the camera at the origin.
     */
    private void updateCamera() {
        // Get the player's current position on the map
        Player player = map.getPlayer();

        // Calculate the new camera position to center on the player
        mapCamera.position.x = player.getX() * TILE_SIZE_PX * SCALE + TILE_SIZE_PX * SCALE / 2;
        mapCamera.position.y = player.getY() * TILE_SIZE_PX * SCALE + TILE_SIZE_PX * SCALE / 2;

        // Update the camera to apply the new position
        mapCamera.update();

    }

    /**
     * Renders the map and all game elements (walls, boosts, ghosts, etc.).
     */
    private void renderMap() {
        // This configures the spriteBatch to use the camera's perspective when rendering
        spriteBatch.setProjectionMatrix(mapCamera.combined);

        // Start drawing
        spriteBatch.begin();

        // Render everything in the map here, in order from lowest to highest (later things appear on top)
        // Render flowers
        for (Flowers flowers : map.getFlowers()) {
            draw(spriteBatch, flowers);
        }

        // Render walls
        for (Wall[] row : map.getWalls()) {
            for (Wall wall : row) {
                if (wall != null) {
                    draw(spriteBatch, wall); // Always render the wall. Handle textures within the Wall class.
                }
            }
        }
        // Render boosts (only revealed ones)
        for (Boost boost : map.getBoosts()) {
            if (!boost.isHidden() && !boost.isDestroyed()) { // Only render revealed boosts
                draw(spriteBatch, boost);
            }
        }

        //Render ghosts
        for (Ghost ghost : map.getGhosts()) {
            draw(spriteBatch, ghost);
        }

        //Render Exit
        if (map.getExit() != null && !map.getExit().isHidden()) {
            draw(spriteBatch, map.getExit());
        }


        // Render player
        draw(spriteBatch, map.getPlayer());

        // Draw bombs and explosions
        for (Bomb bomb : map.getPlayer().getBombs()) {
            if (bomb.isActive()) {
                draw(spriteBatch, bomb);
            } else if (bomb.getExplosion().isActive()) {
                draw(spriteBatch, bomb.getExplosion());
            }
        }

        // Finish drawing, and send the drawn items to the graphics card
        spriteBatch.end();


    }


    /**
     * Draws this object on the screen.
     * The texture will be scaled by the game scale and the tile size.
     * This should only be called between spriteBatch.begin() and spriteBatch.end(), e.g. in the renderMap() method.
     *
     * @param spriteBatch The SpriteBatch to draw with.
     */
    private static void draw(SpriteBatch spriteBatch, Drawable drawable) {
        if (drawable == null) {
            // Log an error if the drawable is null and exit early
            System.out.println("Error: Drawable is null.");
            return;
        }

        // Check if the drawable is an Explosion and render it directly
        if (drawable instanceof Explosion) {
            ((Explosion) drawable).render(spriteBatch);
            return;
        }
        // Retrieve the current appearance (texture) of the drawable
        TextureRegion texture = drawable.getCurrentAppearance();
        if (texture == null) {
            // Log an error if the texture is null and exit early
            System.out.println("Error: TextureRegion is null for drawable: " + drawable.getClass().getSimpleName());
            return;
        }

        // Convert drawable's tile-based coordinates to pixel-based coordinates
        float x = drawable.getX() * TILE_SIZE_PX * SCALE;
        float y = drawable.getY() * TILE_SIZE_PX * SCALE;

        // Scale the texture dimensions by the game scale
        float width = texture.getRegionWidth() * SCALE;
        float height = texture.getRegionHeight() * SCALE;

        // Render the texture at the calculated position and size
        spriteBatch.draw(texture, x, y, width, height);
    }

    /**
     * Called when the window is resized.
     * This is where the camera is updated to match the new window size.
     *
     * @param width  The new window width.
     * @param height The new window height.
     */
    @Override
    public void resize(int width, int height) {
        mapCamera.setToOrtho(false);//adjust camera
        mapCamera.update();
        hud.resize(width, height); //resize the HUD to fit the new dimensions
    }



    // Unused methods from the Screen interface
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }


    //Getters
    public int getCountdownTimer() {
        return countdownTimer; // Replace with your actual timer logic
    }

    public int getGhostsLeft() {
        return ghosts.size(); // Assuming you have a list of enemies
    }

    public boolean isExitUnlocked() {
        return ghosts.isEmpty(); // Exit is unlocked when all enemies are defeated
    }

    public Stage getStage() {
        return stage;
    }
}
