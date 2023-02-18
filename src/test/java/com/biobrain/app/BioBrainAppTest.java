package com.biobrain.app;

import com.biobrain.model.Item;
import com.biobrain.model.Location;
import com.biobrain.view.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//@Disabled - disable the test to package into a jar

class BioBrainAppTest {
    BioBrainApp testApp;
    Player testPlayer;

    @BeforeEach
    void setup(){
        testApp = new BioBrainApp();
        testApp.setPlayer(new Player());
        testPlayer = testApp.getPlayer();
        testApp.currentPlayerLocation();
    }

    @Test
    public void getItemNotinRoom_ExpectedRuntimeException() {
        //SET-UP

        // ACT
        // only the tablet should be located in the starting room
        // attempting to pick up a different item will fail
        String itemNotPresent = "cheese";
        RuntimeException except = assertThrows(
                RuntimeException.class,
                () -> testApp.validateThenGetItem(itemNotPresent)
        );
        String expectedMessage = "java.lang.IllegalArgumentException:\n" +
                "cheese was not found! Please try again.";


        // ASSERT
        assertTrue(except.getMessage().contentEquals(expectedMessage));
    }

    @Test
    public void getNullItem_ExpectedRuntimeException() {
        //SET-UP

        // ACT
        // only the tablet should be located in the starting room
        // attempting to pick up a different item will fail
        String itemNotPresent = null;
        RuntimeException except = assertThrows(
                RuntimeException.class,
                () -> testApp.validateThenGetItem(itemNotPresent)
        );
        String expectedMessage = "java.lang.IllegalArgumentException: \n" +
                "null was not found! Please try again.";

        System.out.println("MESSAGE: " + except.getMessage());

        // ASSERT
        assertTrue(except.getMessage().contentEquals(expectedMessage));
    }

    @Test
    public void getIteminRoom_ExpectedinInventory() {
        //SET-UP

        // ACT
        // only the tablet should be located in the starting room
        String itemPresent = "tablet";
        // add it to inventory
        testApp.validateThenGetItem(itemPresent);

        // ASSERT
        // does player inventory contain the given item?
        assertTrue(testPlayer.getInventory().containsKey(itemPresent));
    }

    @Test
    public void useItemNotinInventory_ExpectedRuntimeException() {
        //SET-UP
        // new example item
        Item testItem = new Item("motherboard", "test item", 0);

        // ACT
        // only the tablet should be located in the starting room
        // attempting to use a different item will fail
        String itemInInventory = "tablet";
        String itemNotInInventory = "motherboard";
        testApp.validateThenGetItem(itemInInventory);
        RuntimeException except = assertThrows(
                RuntimeException.class,
                () -> testApp.validateThenUseItem(itemNotInInventory)
        );
        String expectedMessage = "java.lang.IllegalArgumentException: \n" +
                "You're not carrying a motherboard to be able to use!";

        // ASSERT
        assertTrue(except.getMessage().contentEquals(expectedMessage));
    }

    @Test
    public void useNullItem_ExpectedRuntimeException() {
        //SET-UP
        String itemNotInInventory = null;

        // ACT
        RuntimeException except = assertThrows(
                RuntimeException.class,
                () -> testApp.validateThenUseItem(itemNotInInventory)
        );
        String expectedMessage = "java.lang.IllegalArgumentException: \n" +
                "You're not carrying a null to be able to use!";

        // ASSERT
        assertTrue(except.getMessage().contentEquals(expectedMessage));
    }
}