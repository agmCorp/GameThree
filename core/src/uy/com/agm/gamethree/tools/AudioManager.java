package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by amorales on 14/12/2017.
 */

public class AudioManager {
    public static final AudioManager instance = new AudioManager();

    private Music playingMusic;

    // Singleton: prevent instantiation from other classes
    private AudioManager() {
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
        if (GamePreferences.instance.sound) {
            sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
        }
    }

    public void play(Music music) {
        playingMusic = music;

        if (GamePreferences.instance.music) {
            if (music.isPlaying()) {
                music.stop();
            }
            music.setLooping(true);
            music.setVolume(GamePreferences.instance.volMusic);
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
            playingMusic.setVolume(GamePreferences.instance.volMusic);
            if (GamePreferences.instance.music) {
                if (!playingMusic.isPlaying()) {
                    playingMusic.play();
                }
            } else {
                playingMusic.pause();
            }
        }
    }
}