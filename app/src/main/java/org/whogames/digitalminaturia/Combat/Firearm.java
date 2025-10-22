package org.whogames.digitalminaturia.Combat;

public class Firearm implements Entity {

    private String name;
    private String type;
    private int units;
    private String country;
    private double cost;
    private int fireRate;
    private int reloadTime;
    public static String types[] = {"Assault Rifle", "Rifle", "Howitzer", "Battle Rifle", "RPG", "Handgun", "HMG", "SMG", "GPMG", "LMG", "Cannon", "Cannon Round", "Autocannon", "Grenade Launcher", "Rifle Grenade", "Recoilless Gun"};

    public Firearm(String name, String type, String country, int units, double cost) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.units = units;
        this.cost = cost;
    }

    @Override
    public Object[] getAtts() {
        return new Object[]{name, type, units, cost};
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

    public String toString() {
        return "Firearm{"
                + "name='" + name + '\''
                + ", type='" + type + '\''
                + ", units=" + units
                + ", country='" + country + '\''
                + ", cost=" + cost
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
