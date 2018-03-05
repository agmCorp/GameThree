package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import uy.com.agm.gamethree.game.GameSettings;

/**
 * Created by amorales on 14/12/2017.
 */

public class AudioManager {
    private static final String TAG = AudioManager.class.getName();

    private static  AudioManager instance;
    private Sound playingSound;
    private Music playingMusic;

    // Singleton: prevent instantiation from other classes
    private AudioManager() {
    }

    // Singleton: retrieve instance
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }


    public void pauseSound() {
        if (playingSound != null) {
            playingSound.pause();
        }
    }

    public void resumeSound() {
        if (playingSound != null) {
            if (GameSettings.getInstance().isSound()) {
                playingSound.resume();
            }
        }
    }

    public void stopSound() {
        if (playingSound != null) {
            playingSound.stop();
        }
    }

    public void play(Sound sound) {
        playingSound = sound;
        play(sound, 1);
    }

    public void play(Sound sound, float volume) {
        play(sound, volume, 1);
    }

    public void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }

    public void play(Sound sound, float volume, float pitch, float pan) {
        if (GameSettings.getInstance().isSound()) {
            sound.play(GameSettings.getInstance().getVolSound() * volume, pitch, pan);
        }
    }

    public void play(Music music) {
        // Stop previous music
        if (playingMusic != null) {
            if (playingMusic != music) {
                playingMusic.stop();
            }
        }

        // Play new music
        playingMusic = music;
        if (GameSettings.getInstance().isMusic()) {
            music.setLooping(true);
            music.setVolume(GameSettings.getInstance().getVolMusic());
            music.play();
        }
    }

    public void resumeMusic() {
        if (playingMusic != null) {
            if (GameSettings.getInstance().isMusic()) {
                playingMusic.play();
            }
        }
    }

    public void stopMusic() {
        if (playingMusic != null) {
            playingMusic.stop();
        }
    }

    public void pauseMusic() {
        if (playingMusic != null) {
            playingMusic.pause();
        }
    }

    public void onSettingsUpdated() {
        if (playingMusic != null) {
            playingMusic.setVolume(GameSettings.getInstance().getVolMusic());
            if (GameSettings.getInstance().isMusic()) {
                if (!playingMusic.isPlaying()) {
                    playingMusic.play();
                }
            } else {
                playingMusic.pause();
            }
        }
    }
}