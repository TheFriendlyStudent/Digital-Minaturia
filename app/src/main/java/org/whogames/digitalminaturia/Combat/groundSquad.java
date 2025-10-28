package org.whogames.digitalminaturia.Combat;

import java.util.ArrayList;
import java.util.HashMap;

import org.whogames.digitalminaturia.Combat.Entities.Vehicle;

public class groundSquad extends Squad {

    public HashMap<String, Soldier> soldiers;
    public HashMap<Vehicle, Integer> vehicles;

    private final static int minPersonnel = 2;
    private final static int maxPersonnel = 10;

    private int APDamage, ATDamage, Penetration;

    public groundSquad(int id, String name, String country) {
        super(id, name, country);
        soldiers = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            addSoldier("Rifleman", i+1);
        }
        vehicles = new HashMap<>();
    }

    public groundSquad(int id, String name, int health, String type, String country, String ammo, int personnel) {
        super(id, name, health, type, country, ammo, personnel);
        soldiers = new HashMap<>();
        vehicles = new HashMap<>();
    }

        public groundSquad(int id, String name, int health, String type, String country, String ammo, int personnel, ArrayList<Soldier> soldierList) {
        super(id, name, health, type, country, ammo, personnel);
        soldiers = new HashMap<>();
        for (Soldier soldier : soldierList) {
            soldiers.put(soldier.getType(), soldier);
        }
        vehicles = new HashMap<>();
    }

    public void addSoldier(String soldierName, int count) {
        
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
