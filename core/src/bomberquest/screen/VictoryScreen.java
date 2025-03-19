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
 * The VictoryScreen class represents the screen displayed when the player wins the game.
 * It provides options to retry, load a new game, or return to the main menu.
 */
public class VictoryScreen implements Screen {

    /** The stage containing all UI elements for the Victory screen. */
    private final Stage stage;


    /**
     * Constructs the Victory screen and initializes its UI elements.
     *
     * @param game The main game instance, used for navigating to other screens or restarting the game.
     */
    public VictoryScreen(final bomberquest.BomberQuestGame game) {
        stage = new Stage();

        // Create a table for layout
        Table table = new Table();
        table.setFillParent(true);// Make the table fill the entire stage
        stage.addActor(table);

        // Add "Victory!" label
        Label victoryLabel = new Label("Victory!", game.getSkin(), "title");
        table.add(victoryLabel).padBottom(50).row(); // Add padding below the label

        // Retry Button
        TextButton retryButton = new TextButton("Retry", game.getSkin());
        table.add(retryButton).width(300).padBottom(20).row();
        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.restartGame(); // Restart the game
            }
        });


        // Add "Load New Game" button
        TextButton loadNewGameButton = new TextButton("Load New Game", game.getSkin());
        table.add(loadNewGameButton).width(300).padBottom(20).row();
        loadNewGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.loadNewMap(); // Load a new map for the game
            }
        });

        // Add "Main Menu" button
        TextButton mainMenuButton = new TextButton("Main Menu", game.getSkin());
        table.add(mainMenuButton).width(300).row();
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu(); // Navigate back to the main menu
            }
        });
    }


    /**
     * Called when the Victory screen becomes the active screen.
     * Sets the input processor to the stage so it can handle user input.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }


    /**
     * Renders the Victory screen. Clears the screen and draws the stage.
     *
     * @param deltaTime Time since the last frame, used for updating the stage.
     */
    @Override
    public void render(float deltaTime) {
        ScreenUtils.clear(Color.BLACK); // Clear the screen
        stage.act(deltaTime); // Update the stage
        stage.draw(); // Render the stage
    }


    /**
     * Handles resizing of the screen. Updates the stage's viewport dimensions.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }



    /**
     * Called when the Victory screen is hidden. No specific actions are performed here.
     */
    @Override
    public void hide() {}

    /**
     * Called when the game is paused. Not used in this screen.
     */
    @Override
    public void pause() {}

    /**
     * Called when the game is resumed after being paused. Not used in this screen.
     */
    @Override
    public void resume() {}

    /**
     * Releases resources used by the Victory screen.
     * Disposes of the stage to free up memory.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
