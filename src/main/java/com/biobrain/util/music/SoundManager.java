package com.biobrain.util.music;


import com.biobrain.view.panels.GamePanel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    GamePanel gamePanel;
    JButton button1, button2;
    Clip clip;
    Map<String,URL> soundsURL = new HashMap();
    FloatControl fc;
    private int volumeScale = 3;
    float volume;

    public SoundManager(GamePanel gamePanel) {
        soundsURL.put("mainMenuTheme", getClass().getResource("/sounds/music/mainMenuTheme.wav"));
        soundsURL.put("mainGameTheme", getClass().getResource("/sounds/music/mainTheme.wav"));
        soundsURL.put("menuNavigationSound", getClass().getResource("/sounds/sfx/menuNavigationSound.wav"));
        soundsURL.put("menuSelectPlaySound", getClass().getResource("/sounds/sfx/menuSelectPlaySound.wav"));
        soundsURL.put("menuSelectSound", getClass().getResource("/sounds/sfx/menuSelectSound.wav"));

        this.gamePanel = gamePanel;
    }

    public void setFile(String name) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundsURL.get(name));
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException err) {
            throw new RuntimeException(err);
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

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

        fc.setValue(volume); // assigns current volume level to FloatControl
    }


    // ACCESSOR METHODS

    public int getVolumeScale() {
        return volumeScale;
    }

    public void setVolumeScale(int volumeScale) {
        this.volumeScale = volumeScale;
    }
}
