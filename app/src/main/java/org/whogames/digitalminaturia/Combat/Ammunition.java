package org.whogames.digitalminaturia.Combat;

public class Ammunition implements Entity {

    private String name;
    private String type;
    private String country;
    private int unit;
    private double cost;
    public static String types[] = {"Intermediate Round", "Pistol Round", "SAM", "Rifle Round", "ATGM", "Artillery Shell", "Missile", "Bomb", "Grenade", "RCL Round", "Cannon Round", "Depth Charge", "Torpedo", "AAM", "TBM"};

    public Ammunition(String name, String type, String country, int unit, double cost) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.unit = unit;
        this.cost = cost;
    }

    @Override
    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String getCountry() {
        return country;
    }

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
                + ", unit=" + unit
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
