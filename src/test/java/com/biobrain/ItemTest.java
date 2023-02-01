package com.biobrain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    Item item = new Item("blaster", "This is a blaster, you can use this to fight the rouge robots", 10);

    @Test
    public void should_Return_Name_Of_Item() {
        assertEquals("blaster", item.getName());
    }

    @Test
    public void should_Return_Name_Of_Set_Item() {
        item.setName("tablet");
        assertEquals("tablet", item.getName());
    }

    @Test
    public void should_Return_Description_Of_Item() {
        assertEquals("This is a blaster, you can use this to fight the rouge robots", item.getDescription());
    }

    @Test
    public void should_Return_Item_Damage() {
        assertEquals(10, item.getDamage());
    }

    @Test
    public void should_Return_Set_Damage_Of_Item() {
        item.setDamage(20);
        assertEquals(20, item.getDamage());
    }

    @Test
    public void should_Return_All_Items_If_Present() {
        Item[] allItems = Item.getAllItems();
        assertNotNull(allItems);
    }

    @Test
    public void testGetDescriptions() {
        String description = Item.getDescriptions("blaster");
        assertEquals("This is a blaster, you can use this to fight the rouge robots", description);
    }

}