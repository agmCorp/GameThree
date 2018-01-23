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

    public void play(Sound sound) {
        play(sound, 1);
    }

    public void play(Sound sound, float volume) {
        play(sound, volume, 1);
    }

    public void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }

    public void play(Sound sound, float volume, float pitch, float pan) {
        if (GameSettings.getInstance().getSound()) {
            sound.play(GameSettings.getInstance().getVolSound() * volume, pitch, pan);
        }
    }

    public void play(Music music) {
        playingMusic = music;

        if (GameSettings.getInstance().getMusic()) {
            music.setLooping(true);
            music.setVolume(GameSettings.getInstance().getVolMusic());
            music.play();
        }
    }

    public void stopMusic() {
        if (playingMusic != null) playingMusic.stop();
    }

    public Music getPlayingMusic() {
        return playingMusic;
    }

    public void onSettingsUpdated() {
        if (playingMusic != null) {
            playingMusic.setVolume(GameSettings.getInstance().getVolMusic());
            if (GameSettings.getInstance().getMusic()) {
                if (!playingMusic.isPlaying()) {
                    playingMusic.play();
                }
            } else {
                playingMusic.pause();
            }
        }
    }
}