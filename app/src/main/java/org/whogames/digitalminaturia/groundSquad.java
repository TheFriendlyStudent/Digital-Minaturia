package org.whogames.digitalminaturia;

import java.util.HashMap;
import java.util.Map;

public class groundSquad extends Squad {

    public HashMap<String, Integer> soldiers;
    public HashMap<Firearm, Integer> firearms;
    public HashMap<Vehicle, Integer> vehicles;

    private int APDamage, ATDamage, Penetration;

    public groundSquad(int id, String name, int health, String type, String country, String ammo, int personnel) {
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

    public int calculateHealth(){
        for (Map.Entry<String, Integer> e : soldiers.entrySet()){
            if (e.getKey().equals("Rifleman")){
                this.health+=e.getValue()*3;
            }
        }
        return 0;
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
