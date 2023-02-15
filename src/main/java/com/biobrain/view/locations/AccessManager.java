package com.biobrain.view.locations;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/*
 * AccessManager handles the room/sector exits and entrances
 */
public class AccessManager {

    /**
     * Since sectors are the same size the exit will always be the same. UNLESS
     * we want to create and exit elsewhere.
     * */
    private static final Rectangle sectorExit = new Rectangle(576,570,96,48);
    public static Rectangle sectorExit(){
        return sectorExit;
    }

    /*
    * Used to return a Rectangle for the entrance of a sector based on the location
    * entrance in the labMAP (world)
    * */
    public static Rectangle getRoomEntrance(String roomShortName){
        Map<String, Rectangle> roomEntrances= new HashMap<>();
        roomEntrances.put("sector1", new Rectangle(720, 672,96 , 48));
        roomEntrances.put("sector2", new Rectangle(1632, 672, 96, 48));
        roomEntrances.put("sector3", new Rectangle(2544, 672, 96, 48));
        roomEntrances.put("sector4", new Rectangle(720, 1344, 96, 48));
        roomEntrances.put("sector5", new Rectangle(1632, 1344, 96, 48));
        roomEntrances.put("sector6", new Rectangle(2544, 1344, 96, 48));

        return roomEntrances.get(roomShortName);
    }



}