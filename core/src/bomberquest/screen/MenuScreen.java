package bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage; // The stage for UI elements
    private final bomberquest.BomberQuestGame game; // Reference to the main game class
    private boolean showContinueButton = false; // Flag to toggle "Continue" button

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(bomberquest.BomberQuestGame game) {
        this.game = game; // Initialize the game field
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Hello World from the Menu!", game.getSkin(), "title")).padBottom(80).row();

        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton("Go To Game", game.getSkin());
        table.add(goToGameButton).width(300).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(); // Change to the game screen when button is pressed
            }
        });

        // Add other buttons below using a helper method
        createAdditionalButtons(table, game);


    }
    /**
     * Adds additional buttons to the menu, including "Continue", "Load New Map", and "Exit Game".
     * - The "Continue" button resumes gameplay if available.
     * - The "Load New Map" button loads a new game map.
     * - The "Exit Game" button closes the game.
     *
     * @param table The table to which the buttons will be added.
     * @param game  The main game instance for handling button actions.
     */
    private void createAdditionalButtons(Table table, bomberquest.BomberQuestGame game) {
        // "Continue" button (conditionally added)
        if (showContinueButton) {
            TextButton continueButton = new TextButton("Continue", game.getSkin());
            table.add(continueButton).width(300).padBottom(20).row();
            continueButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.goToGame(); // Resume gameplay
                }
            });
        }

        // "Load New Map" button
        TextButton loadMapButton = new TextButton("Load New Map", game.getSkin());
        table.add(loadMapButton).width(300).padBottom(20).row();
        loadMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.loadNewMap(); // Load a new game map
            }
        });

        // "Exit Game" button
        TextButton exitButton = new TextButton("Exit Game", game.getSkin());
        table.add(exitButton).width(300).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.exitGame(); // Call the exitGame method
            }
        });
    }


    /**
     * Updates the "Continue" button visibility based on the game state.
     *
     * @param showContinue True to show the "Continue" button, false to hide it.
     */
    public void setShowContinueButton(boolean showContinue) {
        this.showContinueButton = showContinue;
    }

    /**
     * The render method is called every frame to render the menu screen.
     * It clears the screen and draws the stage.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.250f); // Cap frame time to 250ms to prevent spiral of death        ScreenUtils.clear(Color.BLACK);
        ScreenUtils.clear(Color.BLACK);
        stage.act(frameTime); // Update the stage
        stage.draw(); // Draw the stage
    }

    /**
     * Resize the stage when the screen is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }


    /**
     * Disposes of resources used by the menu screen.
     * This includes the stage and its UI components to free memory and avoid resource leaks.
     */
    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);

    }

    // The following methods are part of the Screen interface but are not used in this screen.

    @Override
    public void pause() {
    }


    @Override
    public void resume() {
    }


    @Override
    public void hide() {
    }

    /**
     * Provides access to the stage for testing or advanced customization.
     *
     * @return The stage associated with this screen.
     */
    public Stage getStage() {
        return stage;
    }
}
