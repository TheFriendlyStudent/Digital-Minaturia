package org.whogames.digitalminaturia.Combat.Entities;

import java.util.ArrayList;

public class Vehicle implements Entity {

    private String name;
    private String country;
    private String type;
    private double pcost;
    private double lcost;
    private double weight;
    private int crew;
    private int durability;
    private int armor;
    private boolean amphibious;
    private ArrayList<Firearm> weapons;
    public static String types[] = {"MBT", "Tankette", "Heavy Tank", "Aircraft Carrier", 
    "Disposable Anti Tank Rocket", "Jet Bomber", "Midget Submarine", "Missile Submarine", 
    "Jet Attacker", "VTOL Attacker", "Jet Interceptor", "Light Carrier", "APC", "IFV", 
    "Armored Car", "Light Tank", "Destroyer", "Cruiser", "Jet", "CAS Jet", "SPAA", 
    "Utility Helicopter", "Attack Helicopter", "Transport Helicopter", "Transport Aircraft"};

    public Vehicle(){

    }

    public Vehicle(String name, String country, String type, double pcost, double lcost, double weight, int durability,
            int armor, int crew, boolean amphibious, ArrayList<Firearm> weaponsList) {
        this.name = name;
        this.country = country;
        this.type = type;
        this.pcost = pcost;
        this.lcost = lcost;
        this.weight = weight;
        this.durability = durability;
        this.armor = armor;
        this.crew = crew;
        this.amphibious = amphibious;
        for (Firearm weapon : weaponsList) {
            if (weapons.contains(weapon)) {
                this.weapons.add(weapon);
            }
            this.weapons.add(weapon);
        }
    }




    public Vehicle(String name, String type, String country, int units, double cost) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.lcost = units;
        this.pcost = cost;
    }

    @Override
    public Object[] getAtts() {
        return new Object[]{name, type, country, lcost, pcost};
    }

    @Override
    public void setAtts(Object[] atts) {
        if (atts.length == 5) {
            this.name = (String) atts[0];
            this.type = (String) atts[1];
            this.country = (String) atts[2];
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCountry() {
        return country;
    }

    public String toString() {
        return "Vehicle{"
                + "name='" + name + '\''
                + ", type='" + type + '\''
                + ", country='" + country + '\''
                + '}';
    }

    public static String[] getTypes() {
        return types;
    }

    @Override
    public String getTypeClass() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTypeClass'");
    }

    @Override
    public String getSubclass() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSubclass'");
    }

    @Override
    public int getProductionCost() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionCost'");
    }

    @Override
    public int getLaborCost() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLaborCost'");
    }

    @Override
    public double getWeight() {
        // TODO Auto-generated method stub
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPcost() {
        return pcost;
    }

    public void setPcost(double pcost) {
        this.pcost = pcost;
    }

    public double getLcost() {
        return lcost;
    }

    public void setLcost(double lcost) {
        this.lcost = lcost;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public boolean isAmphibious() {
        return amphibious;
    }

    public void setAmphibious(boolean amphibious) {
        this.amphibious = amphibious;
    }

    public ArrayList<Firearm> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<Firearm> weapons) {
        this.weapons = weapons;
    }

    public int setCrew(int crew) {
        return this.crew = crew;
    }
    
    public int getCrew() {
        return crew;
    }

}
