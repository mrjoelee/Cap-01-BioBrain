package com.biobrain.view;

import com.biobrain.view.entities.Player;
import com.biobrain.view.event.KeyHandler;
import com.biobrain.view.locations.LocationManager;
import com.biobrain.view.panels.GamePanel;
import com.biobrain.view.panels.GameSetter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameSetterTest {

    @Test
    public void should_reset_Player_location_For_TryAgain(){
        new GameSetter();
        GamePanel gp = new GamePanel();
        LocationManager locations = new LocationManager(true);
        //test case : game starts at sector 1
        gp.currentRoom = locations.getLocations().get("sector1");
        //will reset to sector 2
        gp.tryAgain();
        String locationName = gp.currentRoom.getShortName();
        Assertions.assertEquals("sector2",locationName);
    }
}
