package org.whogames.digitalminaturia.Combat.Entities;

public class Ammunition implements Entity {

    private String name;
    private String type;
    private String country;
    private double pcost;
    private double lcost;
    private int damage;
    private double penetration;
    private double weight;
    public static String types[] = {"Intermediate Round", "Pistol Round", "SAM", "Rifle Round", "ATGM", 
    "Artillery Shell", "Missile", "Bomb", "Grenade", "RCL Round", "Cannon Round", "Depth Charge", 
    "Torpedo", "AAM", "TBM"};

    public Ammunition() {

    }

    public Ammunition(String name, String country, String type, double pcost, double lcost, double weight, int damage, double penetration) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.pcost = pcost;
        this.lcost = lcost;
        this.weight = weight;
        this.damage = damage;
        this.penetration = penetration;
    }

    public Ammunition(String name, String country, String type, double pcost, double lcost) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.pcost = pcost;
        this.lcost = lcost;
        this.damage = 10;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCountry() {
        return country;
    }

    public void setType(String type) { this.type = type; }
    public void setPcost(double pcost) { this.pcost = pcost; }
    public void setLcost(double lcost) { this.lcost = lcost; }
    public void setDamage(int damage) { this.damage = damage; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setPenetration(double penetration) { this.penetration = penetration; }

    @Override
    public Object[] getAtts() {
        throw new UnsupportedOperationException("Unimplemented method 'getAtts'");
    }

    @Override
    public void setAtts(Object[] atts) {
        throw new UnsupportedOperationException("Unimplemented method 'setAtts'");
    }

    public String toString() {
        return "Ammunition{"
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
        throw new UnsupportedOperationException("Unimplemented method 'getWeight'");
    }

    public int getDamage() {
        // TODO Auto-generated method stub
        return damage;
    }

    

}
