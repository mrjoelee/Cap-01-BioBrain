package com.biobrain.util.music;


import javax.sound.sampled.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SoundManager {
    JFrame frame;
    JPanel panel;
    JButton button1, button2;
    String sound_track;
    Music se = new Music();

    public static void main(String args[]) {
        new SoundManager();
    }

    public class Music {
        Clip clip;
        AudioInputStream sound;

        public void setFile(String soundFileName) {
            try {
                File file = new File(soundFileName);
                sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            } catch (Exception e) {

            }
        }

        public void play() {
            clip.start();
        }

        public void stop() throws IOException {
            sound.close();
            clip.close();
            clip.stop();
        }
    }

    public SoundManager() {
        sound_track = "src/main/resources/sounds/music/mainTheme.wav";
        System.out.println(sound_track);
        frame = new JFrame("Audio");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.add(panel);

        frame.setVisible(true);

        button1 = new JButton("Start");
        button1.setBounds(200, 240, 100, 30);
        button1.setBackground(Color.WHITE);
        button1.setFocusPainted(false);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                se.setFile(sound_track);
                se.play();
            }
        });
        panel.add(button1);

        button2 = new JButton("Stop");
        button2.setBounds(400, 240, 100, 30);
        button2.setBackground(Color.WHITE);
        button2.setFocusPainted(true);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                se.setFile(null);
                try {
                    se.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.add(button2);
    }
}
