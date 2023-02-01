package com.biobrain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player = Player.create();

    @Test
    void should_Return_added_Item_In_Inventory() {
        player.addItem("harpoon");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("harpoon");
        assertEquals(expected, player.getInventory());
    }

    @Test
    void should_Remove_Item_From_Inventory() {
        player.addItem("harpoon");
        player.removeItem("harpoon");
        ArrayList<String> expected = new ArrayList<>();
        assertEquals(expected, player.getInventory());
    }

    @Test
    void should_Return_Items_Added_To_Inventory() {
        player.addItem("harpoon");
        player.addItem("tablet");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("harpoon");
        expected.add("tablet");
        assertEquals(expected, player.getInventory());
    }

    @Test
    void displayPlayerInfo() {
        player.addItem("blaster");
        String expected = "\n =========== PLAYER INFO =================\n" +
                "\n      Your health is at 100.0\n" +
                "      Your current inventory: [blaster]\n" +
                "\n =========================================\n";
        assertEquals(expected, player.displayPlayerInfo());
    }
}