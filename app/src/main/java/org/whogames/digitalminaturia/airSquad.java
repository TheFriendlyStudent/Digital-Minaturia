package org.whogames.digitalminaturia;

import java.util.HashMap;

public class airSquad extends Squad {

    public HashMap<String, Integer> soldiers;
    public HashMap<Firearm, Integer> firearms;
    public HashMap<Vehicle, Integer> vehicles;

    private int speed, maneuverability, AAdamage, AGdamage;

    public airSquad(int id, String name, int health, String type, String country, String ammo, int personnel) {
        super(id, name, health, type, country, ammo, personnel);
        soldiers = new HashMap<>();
        firearms = new HashMap<>();
        vehicles = new HashMap<>();
    }

    public void addSoldier(String soldierName, int count) {
        soldiers.put(soldierName, soldiers.getOrDefault(soldierName, 0) + count);
    }

    public void removeSoldier(String soldierName, int count) {
        if (soldiers.containsKey(soldierName)) {
            int currentCount = soldiers.get(soldierName);
            if (currentCount <= count) {
                soldiers.remove(soldierName);
            } else {
                soldiers.put(soldierName, currentCount - count);
            }
        }
    }

    public HashMap<String, Integer> getSoldiers() {
        return soldiers;
    }

    @Override
    public String toString() {
        return "groundSquad{"
                + "id=" + getId()
                + ", name='" + getName() + '\''
                + ", health=" + getHealth()
                + ", type='" + getType() + '\''
                + ", country='" + getCountry() + '\''
                + ", ammo='" + getAmmo() + '\''
                + ", soldiers=" + soldiers
                + '}';
    }

}
