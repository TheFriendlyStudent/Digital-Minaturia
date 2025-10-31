package org.whogames.digitalminaturia.Combat.Entities;

public class Firearm extends Weapon {

    private String name;
    private String type;
    private String country;
    private double pcost;
    private double lcost;
    private Ammunition ammunition;
    private int capacity;
    private int fireRate;
    private int reloadTime;
    private double weight;

    public Firearm() {
        super(10);
    }

    public Firearm(String name, String country, String type, double pcost, double lcost, double weight, Ammunition ammunition, int capacity, int fireRate, int reloadTime) {
        super(20);
        this.name = name;
        this.type = type;
        this.country = country;
        this.pcost = pcost;
        this.capacity = capacity;
        this.lcost = lcost;
        this.weight = weight;
        this.ammunition = ammunition;
        this.capacity = capacity;
        this.fireRate = fireRate;
        this.reloadTime = reloadTime;
    }

    public Firearm(String name, String type, String country, int units, int damage, double cost) {
        super(damage);
        this.name = name;
        this.type = type;
        this.country = country;
    }

    public Firearm(String name, String type, String country, int units, double cost) {
        super(30);
        this.name = name;
        this.type = type;
        this.country = country;

    }

    @Override
    public Object[] getAtts() {
        return new Object[]{name, type, country, pcost, lcost, ammunition, capacity, fireRate, reloadTime};
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setAtts(Object[] atts) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAtts'");
    }

    @Override
    public String getName() {
        return name;
    }

    public void setType(String type) { this.type = type; }
    public void setPcost(double pcost) { this.pcost = pcost; }
    public void setLcost(double lcost) { this.lcost = lcost; }
    public void setDamage(int damage) { this.damage = damage; }
    public void setWeight(int weight) { this.weight = weight; }

    public String toString() {
        return "Firearm{"
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getPcost() {
        return pcost;
    }

    public double getLcost() {
        return lcost;
    }

    public Ammunition getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(Ammunition ammunition) {
        this.ammunition = ammunition;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

}
