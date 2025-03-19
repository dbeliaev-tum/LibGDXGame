package bomberquest.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Enumerates all spritesheets used in the game and provides helper methods for grabbing texture regions from them.
 * It is assumed that every spritesheet has some standard grid size which can be used for easier coordinate specification.
 * See the assets/texture folder for the actual texture files (plus some more samples which are not enumerated here).
 * Feel free to add your own spritesheets and use them in the game!
 *
 * @see Texture a whole image
 * @see TextureRegion a part of an image
 */
public enum SpriteSheet {

    /** The character spritesheet, which has a grid size of 16x32. */
    CHARACTER("character.png", 16, 32),
    /** The basic tiles spritesheet, which has a grid size of 16x16. */
    BASIC_TILES("basictiles.png", 16, 16),
    /** The original Bomberman spritesheet, assuming 16x16 tiles. */
    ORIGINAL_BOMBERMAN ("original-bomberman.png", 16, 16);


    /** The texture for this spritesheet. */
    private final Texture spritesheet;

    /** The width of a single tile in the grid, in pixels. */
    private final int width;

    /** The height of a single tile in the grid, in pixels. */
    private final int height;

    /**
     * Constructor for each variant of this enum.
     * Every SpriteSheet has a corresponding file, width, and height.
     * @param filename the filename of the spritesheet
     * @param tileWidth the width of a single grid cell
     * @param tileHeight the height of a single grid cell
     */
    SpriteSheet(String filename, int tileWidth, int tileHeight) {
        this.spritesheet = new Texture(Gdx.files.internal("texture/" + filename));
        this.width = tileWidth;
        this.height = tileHeight;
    }

    /**
     * Returns the TextureRegion at the specified row and column (1-based coordinates)
     * according to the grid specified by {@code this.width} and {@code this.height}.
     * This method assumes the size of the texture to be a single grid cell.
     * Keep in mind that since spritesheet textures typically start in the top-left corner,
     * the row index starts at 1 at the top and the column index starts at 1 on the left.
     *
     * @param row the row of the texture to fetch, starting from 1 at the TOP of the spritesheet
     * @param column the column of the texture to fetch, starting from 1 on the LEFT of the spritesheet
     * @return the texture
     */
    public TextureRegion at(int row, int column) {
        return new TextureRegion(
                spritesheet,
                (column - 1) * this.width,
                (row - 1) * this.height,
                this.width,
                this.height
        );
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
