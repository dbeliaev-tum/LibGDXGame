package bomberquest;

import bomberquest.audio.MusicTrack;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;
import bomberquest.map.GameMap;
import bomberquest.screen.GameOverScreen;
import bomberquest.screen.GameScreen;
import bomberquest.screen.MenuScreen;
import bomberquest.screen.VictoryScreen;

/**
 * The bomberquest.BomberQuestGame class represents the core of the Bomber Quest game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class BomberQuestGame extends Game {

    /**
     * Sprite Batch for rendering game elements.
     * This eats a lot of memory, so we only want one of these.
     */
    private SpriteBatch spriteBatch;

    /** The game's UI skin. This is used to style the game's UI elements. */
    private Skin skin;

    /**
     * The file chooser for loading map files from the user's computer.
     * This will give you access to a {@link FileHandle} object,
     * which you can use to read the contents of the map file as a String, and then parse it into a {@link GameMap}.
     */
    private final NativeFileChooser fileChooser;

    private boolean isPaused = false; // Track whether the game is paused

    /**
     * The map. This is where all the game objects are stored.
     * This is owned by {@link BomberQuestGame} and not by {@link GameScreen}
     * because the map should not be destroyed if we temporarily switch to another screen.
     */
    private GameMap map;

    /**
     * Constructor for bomberquest.BomberQuestGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public BomberQuestGame(NativeFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     * During the class constructor, libGDX is not fully initialized yet.
     * Therefore, this method serves as a second constructor for the game,
     * and we can use libGDX resources here.
     */
    @Override
    public void create() {
        this.spriteBatch = new SpriteBatch(); // Create SpriteBatch for rendering
        this.skin = new Skin(Gdx.files.internal("skin/craftacular/craftacular-ui.json")); // Load UI skin
        this.map = new GameMap(this); // Create a new game map (you should change this to load the map from a file instead)
        MusicTrack.MENU.play(); // Start menu music by default
        goToMenu();
    }

    /**
     * Switches to the menu screen and pauses the game.
     * Stops all active music and plays the menu theme.
     */
    public void goToMenu() {
        MusicTrack.stopAll();
        MusicTrack.MENU.play();
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        isPaused = true; // Pause the game when switching to the menu
    }

    /**
     * Switches to the game screen and resumes gameplay.
     * Stops all active music and plays the background music.
     */
    public void goToGame() {
        MusicTrack.stopAll();
        MusicTrack.BACKGROUND.play();
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        isPaused = false; // Unpause the game
    }

    /**
     * Switches to the victory screen when the player wins the game.
     * Stops all active music and plays the menu theme.
     */
    public void goToVictoryScreen() {
        MusicTrack.stopAll();
        MusicTrack.MENU.play();
        this.setScreen(new VictoryScreen(this));
    }

    /**
     * Switches to the game over screen when the player loses the game.
     * Stops all active music and plays the menu theme.
     */
    public void goToGameOverScreen() {
        MusicTrack.BACKGROUND.stop();
        MusicTrack.MENU.play();
        this.setScreen(new GameOverScreen(this));
    }

    /**
     * Exits the game by closing the application.
     */
    public void exitGame() {
        Gdx.app.exit(); // Exit the game
    }

    /**
     * Restarts the game by reinitializing the map and switching to the game screen.
     */
    public void restartGame() {
        this.map = new GameMap(this); // Reset the game map (fresh start)
        this.goToGame(); // Navigate to the game screen
    }


    /** Returns the skin for UI elements. */
    public Skin getSkin() {
        return skin;
    }

    /** Returns the main SpriteBatch for rendering. */
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    /** Returns the current map, if there is one. */
    public GameMap getMap() {
        return map;
    }

    /**
     * Switches to the given screen and disposes of the previous screen.
     * @param screen the new screen
     */
    @Override
    public void setScreen(Screen screen) {
        Screen previousScreen = super.screen; // Store the current screen
        super.setScreen(screen); // Set the new screen
        if (previousScreen != null) {
            previousScreen.dispose(); // Dispose of the previous screen to free resources
        }
    }

    /** Cleans up resources when the game is disposed. */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
        MusicTrack.stopAll();// Stop all active music
    }

    /**
     * Prompts the user to select a new map file using a native file chooser.
     * The method opens a file selection dialog configured to filter for `.properties` files.
     * Once a valid file is selected, the map is loaded from the file, and the game switches to the game screen.
     *
     * This method relies on the {@link NativeFileChooser} to handle file selection and is suitable for desktop platforms.
     *
     * File selection outcomes are handled as follows:
     * - **File chosen:** Loads the selected map and starts the game.
     * - **Cancellation:** Logs that the operation was cancelled.
     * - **Error:** Logs any errors encountered during the file selection process.
     */
    public void loadNewMap() {
        NativeFileChooserConfiguration conf = new NativeFileChooserConfiguration(); //github/arthurtemple/gdx-nativefilechooser
        conf.directory = Gdx.files.absolute(System.getProperty("user.home"));
        conf.nameFilter = (dir, name) -> name.endsWith(".properties");
        conf.title = "Choose a map file";

        fileChooser.chooseFile(conf, new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle file) {
                if (file != null && file.exists()) {
                    System.out.println("Chosen File: " + file.path());
                    // Here you load the map from the file
                    map = new GameMap(BomberQuestGame.this, file);
                    goToGame();
                }
            }

            @Override
            public void onCancellation() {
                System.out.println("Cancelled");
            }

            @Override
            public void onError(Exception exception) {
                System.err.println("Error: " + exception.getMessage());
            }
        });
    }
}