package com.biobrain;

import com.biobrain.Location;
import java.util.List;


public class S1_WeaponsChamber {
    public void sector1() {

        public Location currentLocation;
        public List<Location> locations;
        public List<String> itemsInRoom;

        locations = Location.parsedLocationsFromJson();

        if (locations != null && !locations.isEmpty()) {
            currentLocation = locations.get(0);
            itemsInRoom = currentLocation.getItems();
            System.out.printf("\nYou are currently in %s \n", currentLocation.getName());
            System.out.println("\nYou see the following items: ");
            for (String item : itemsInRoom) {
                System.out.print("\n " + item);
            }
            System.out.printf("\n\nYou can choose to go East to %s ", currentLocation.getDirections().get("east"));
            System.out.printf("\nOr you can go South to %s", currentLocation.getDirections().get("south"));

        } else {
            System.out.println("Error in getting the location");
        }
    }
}