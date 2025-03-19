package bomberquest.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * This enum is used to manage the music tracks in the game.
 * Currently, only one track is used, but this could be extended to include multiple tracks.
 * Using an enum for this purpose is a good practice, as it allows for easy management of the music tracks
 * and prevents the same track from being loaded into memory multiple times.
 * See the assets/audio folder for the actual music files.
 * Feel free to add your own music tracks and use them in the game!
 */

public enum MusicTrack {

    BACKGROUND("background.mp3", true, 0.05f),
    BOMB("bomb.wav", false, 0.5f),
    PLANTING("planting.mp3", false, 0.5f),
    BOOST("boost.wav", false, 0.5f),
    DEATH("death.wav", false, 0.5f),
    EXPLOSION("explosion.wav", false, 0.3f),
    MENU("menu.mp3",true,0.20f);

    /** The music file owned by this variant. */
    private final Music music;

    MusicTrack(String fileName, boolean loop, float volume) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + fileName));
        this.music.setLooping(loop);
        this.music.setVolume(volume);
    }

    /**
     * Play this music track.
     * This will not stop other music from playing
     */
    public void play() {
        this.music.play();
    }

    /**
     * Stops playback of this music track.
     */
    public void stop(){
        this.music.stop();
    }

    /**
     * Stops playback of all music tracks.
     * This method iterates through all {@code MusicTrack} instances and stops any tracks that are currently playing.
     */
    public static void stopAll(){
        for (MusicTrack track : values()) {
            track.stop();
        }
    }

}
