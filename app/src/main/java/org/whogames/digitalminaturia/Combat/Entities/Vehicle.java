package org.whogames.digitalminaturia.Combat.Entities;

import java.util.HashMap;

public class Vehicle implements Entity {

    private String name;
    private String country;
    private String type;
    private double pcost;
    private double lcost;
    private int durability;
    private int armor;
    private HashMap<String, Weapon> weapons;
    public static String types[] = {"MBT", "Tankette", "Heavy Tank", "Aircraft Carrier", 
    "Disposable Anti Tank Rocket", "Jet Bomber", "Midget Submarine", "Missile Submarine", 
    "Jet Attacker", "VTOL Attacker", "Jet Interceptor", "Light Carrier", "APC", "IFV", 
    "Armored Car", "Light Tank", "Destroyer", "Cruiser", "Jet", "CAS Jet", "SPAA", 
    "Utility Helicopter", "Attack Helicopter", "Transport Helicopter", "Transport Aircraft"};

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
    public int getWeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWeight'");
    }

}
