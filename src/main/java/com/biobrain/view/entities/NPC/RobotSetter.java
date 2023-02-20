package com.biobrain.view.entities.NPC;

import com.biobrain.util.JSONParser;
import com.biobrain.view.panels.GamePanel;
import com.google.gson.JsonObject;

import java.util.List;

public class RobotSetter {
    GamePanel gamePanel;
    public RobotSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setRobots(){
        List<JsonObject> npcList = JSONParser.npcToJSONObjects();
        for (JsonObject aiRobot: npcList) {
            String name = aiRobot.get("name").getAsString();
            String description = aiRobot.get("description").getAsString();
            int roomCode = aiRobot.get("roomCode").getAsInt();
            int speed = aiRobot.get("speed").getAsInt();
            int health = aiRobot.get("health").getAsInt();
            int width = aiRobot.get("width").getAsInt();
            int height = aiRobot.get("height").getAsInt();
            int blockX = aiRobot.get("blockX").getAsInt();
            int blockY = aiRobot.get("blockY").getAsInt();

            Npc newRobot = new Npc(gamePanel, name, description, roomCode, health, speed, width, height, blockX, blockY);
            gamePanel.aiRobots.add(newRobot);
        }
    }
}