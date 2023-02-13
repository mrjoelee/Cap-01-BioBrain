package com.biobrain.app;

import com.biobrain.model.Inventory;
import com.biobrain.model.Item;
import com.biobrain.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BioBrainAppTest {
    BioBrainApp testApp;
    Player testPlayer;

    @BeforeEach
    void setup(){
        testApp = new BioBrainApp();
        testApp.setPlayer(Player.create());
        testPlayer = testApp.getPlayer();
        testApp.currentPlayerLocation();
    }

    @Test
    public void useItemNotinInventory_ExpectedFail() {
        //set up
        // new example item
        Item testItem = new Item("motherboard", "test item", 0);

        // only the tablet should be located in the starting room
        // attempting to use a different item will fail
        testApp.validateThenGetItem("tablet");
        IllegalArgumentException except = assertThrows(
                IllegalArgumentException.class,
                () -> testApp.validateThenUseItem("motherboard")
        );

        // act
        assertTrue(except.getMessage().contentEquals("You're not carrying a motherboard to be able to use!"));
    }
}