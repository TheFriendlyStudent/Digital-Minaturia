package org.whogames.digitalminaturia;

public class Firearm implements Entity {

    private String name;
    private String type;
    private int units;
    private String country;
    private double cost;
    public static String types[] = {"Assault Rifle", "Rifle", "Howitzer", "Battle Rifle", "RPG", "Handgun", "HMG", "SMG", "GPMG", "LMG", "Cannon", "Autocannon", "Grenade Launcher", "Rifle Grenade", "Recoilless Gun"};

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

    @Override
    public int getUnits() {
        return units;
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

}
