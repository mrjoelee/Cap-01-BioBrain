package com.biobrain.util.music;

import com.biobrain.view.panels.GamePanel;

public class InstructionVoice extends SoundManager {

    public InstructionVoice(GamePanel gamePanel) {
        super(gamePanel);
        soundsURL.put("instructionVoice",getClass().getResource("/sounds/voice/instructionVoice.wav"));
    }
}
