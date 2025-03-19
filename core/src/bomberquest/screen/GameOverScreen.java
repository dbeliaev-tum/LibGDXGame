package bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Represents the Game Over screen displayed after the player loses the game.
 * Provides options to retry, load a new game, or return to the main menu.
 */
public class GameOverScreen implements Screen {

    /** The Stage used to display UI elements. */
    private final Stage stage;

    /**
     * Constructs the Game Over screen.
     *
     * @param game The main game instance for handling navigation and actions.
     */
    public GameOverScreen(final bomberquest.BomberQuestGame game) {
        stage = new Stage();

        // Create the UI table
        Table table = new Table();
        table.setFillParent(true); // Make the table fill the screen
        stage.addActor(table);

        // Add "Game Over" label
        Label gameOverLabel = new Label("Game Over", game.getSkin(), "title");
        table.add(gameOverLabel).padBottom(50).row();// Add label with padding

        // Retry Button
        TextButton retryButton = new TextButton("Retry", game.getSkin());
        table.add(retryButton).width(300).padBottom(20).row(); // Set button width and padding
        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.restartGame(); // Restart the game when clicked
            }
        });

        // Add "Load New Game" button
        TextButton loadNewGameButton = new TextButton("Load New Game", game.getSkin());
        table.add(loadNewGameButton).width(300).padBottom(20).row(); // Set button width and padding
        loadNewGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.loadNewMap(); // Load a new map when clicked
            }
        });

        // Add "Main Menu" button
        TextButton mainMenuButton = new TextButton("Main Menu", game.getSkin());
        table.add(mainMenuButton).width(300).row(); // Set button width
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu(); // Navigate back to the main menu when clicked
            }
        });
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * Sets the input processor to handle user interaction on this screen.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders the Game Over screen.
     * Clears the screen with a black background, updates the stage, and renders its UI elements.
     *
     * @param deltaTime The time in seconds since the last frame.
     */
    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(Color.BLACK); // Clear the screen
        stage.act(deltaTime); // Update the stage
        stage.draw(); // Render the stage
    }

    /**
     * Resizes the Game Over screen's viewport.
     * Updates the stage's viewport to handle new screen dimensions.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }



    /**
     * Called when this screen is no longer the current screen.
     * This method can be used to pause ongoing animations or interactions.
     */
    @Override
    public void hide() {}


    /**
     * Called when the game is paused (e.g., minimized or sent to the background).
     */
    @Override
    public void pause() {}


    /**
     * Called when the game is resumed after being paused.
     */
    @Override
    public void resume() {}



    /**
     * Releases resources used by the Game Over screen.
     * Disposes of the stage and its UI elements to free up memory.
     */
    @Override
    public void dispose() {
        stage.dispose(); // Dispose of the stage to free resources
    }
}
