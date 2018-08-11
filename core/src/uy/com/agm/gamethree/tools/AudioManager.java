package uy.com.agm.gamethree.tools;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.game.GameSettings;

/**
 * Created by amorales on 14/12/2017.
 */

public class AudioManager {
    private static final String TAG = AudioManager.class.getName();

    // Keeps a sound filename and the last playing time in nanoseconds.
    private ArrayMap<String, Long> trackSounds;  // Ordered map

    private static  AudioManager instance;
    private Sound playingSound;
    private Music playingMusic;
    private boolean looping;

    // Singleton: prevent instantiation from other classes
    private AudioManager() {
        trackSounds = new ArrayMap<String, Long>();
        looping = true;
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

    public void playSound(Sound sound) {
        playSound(sound, 1);
    }

    public void playSound(Sound sound, float volume) {
        playSound(sound, volume, 1);
    }

    public void playSound(Sound sound, float volume, float pitch) {
        playSound(sound, volume, pitch, 0);
    }

    public void playSound(Sound sound, float volume, float pitch, float pan) {
        playingSound = sound;
        trackSounds.put(Assets.getInstance().getAssetFileName(playingSound), TimeUtils.nanoTime()); // If the key exists, it overwrites its value.
        if (GameSettings.getInstance().isSound()) {
            sound.play(GameSettings.getInstance().getVolSound() * volume, pitch, pan);
        }
    }

    public void setMusic(Music music, boolean loop) {
        playingMusic = music;
        looping = loop;
        if (GameSettings.getInstance().isMusic()) {
            playingMusic.setLooping(looping);
            playingMusic.setVolume(GameSettings.getInstance().getVolMusic());
        }
    }

    public void playMusic() {
        if (GameSettings.getInstance().isMusic()) {
            playingMusic.setLooping(looping);
            playingMusic.setVolume(GameSettings.getInstance().getVolMusic());
            playingMusic.play();
        }
    }

    public void playMusic(Music music, boolean loop) {
        // Stop previous music
        if (playingMusic != null) {
            if (playingMusic != music) {
                playingMusic.stop();
            }
        }

        // Play new music
        playingMusic = music;
        looping = loop;
        playMusic();
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
        if (playingSound != null) {
            if (!GameSettings.getInstance().isSound()) {
                playingSound.stop();
            }
        }
    }

    // Returns null if the sound was never played
    public Long getLastPlayingTime(Sound sound) {
        return trackSounds.get(Assets.getInstance().getAssetFileName(sound));
    }
}