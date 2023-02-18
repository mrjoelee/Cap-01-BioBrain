package com.biobrain.util.music;

/*
 * SoundManager | Class
 * holds sound file list
 * plays, loops, and stops sounds from playing
 * 2 of these exist in GamePanel: 1 for music and 1 for sound effects
 */

import com.biobrain.view.panels.GamePanel;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    GamePanel gamePanel;                       // reference to main game logic
    Clip clip;                                 // store current soundclip
    Map<String,URL> soundsURL = new HashMap(); // map acts as soundtrack
    FloatControl fc;                           // float control used to define volume settings
    private int volumeScale = 1;               // volume scale so user has 5 easy volume settings
    float volume;                              // current volume

    // CTOR
    public SoundManager(GamePanel gamePanel) {
        // create a map that acts as the game's soundtrack
        soundsURL.put("mainMenuTheme", getClass().getResource("/sounds/music/mainMenuTheme.wav"));
        soundsURL.put("mainGameTheme", getClass().getResource("/sounds/music/mainTheme.wav"));
        soundsURL.put("menuNavigationSound", getClass().getResource("/sounds/sfx/menuNavigationSound.wav"));
        soundsURL.put("menuSelectPlaySound", getClass().getResource("/sounds/sfx/menuSelectPlaySound.wav"));
        soundsURL.put("menuSelectSound", getClass().getResource("/sounds/sfx/menuSelectSound.wav"));
        soundsURL.put("gameOverSound", getClass().getResource("/sounds/sfx/gameOver.wav"));

        this.gamePanel = gamePanel;
    }


    // CLASS METHODS
    // sets sound file to be used
    public void setFile(String name) {
        try {
            // create audio input from the selected sound file
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundsURL.get(name));
            clip = AudioSystem.getClip(); // get a new clip
            clip.open(audioInput);        // add the new audio info to it

            // use the float control to act as control for clip volume
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume(); // call to reset volume level
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException err) {
            throw new RuntimeException(err);
        }
    }

    // play a sound
    public void play() {
        clip.start();
    }

    // play on sound on loop repeating
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // stop a sound from playing
    public void stop() {
        clip.stop();
    }

    // checks to see which level volume is set to then assigns float value to the FloatControl
    public void checkVolume(){
        switch(volumeScale){
            case 0:
                volume = -80f;
                break;
            case 1:
                volume = -20f;
                break;
            case 2:
                volume = -12f;
                break;
            case 3:
                volume = -5f;
                break;
            case 4:
                volume = 1f;
                break;
            case 5:
                volume = 6f;
                break;
        }

        fc.setValue(volume); // assigns current volume level to FloatControl which governs clip volume
    }


    // ACCESSOR METHODS
    public int getVolumeScale() {
        return volumeScale;
    }

    public void setVolumeScale(int volumeScale) {
        this.volumeScale = volumeScale;
    }
}