package org.whogames.digitalminaturia;

public class Vehicle implements Entity {

    private String name;
    private String type;
    private String country;
    private int units;
    private double cost;
    public static String types[] = {"MBT", "Tankette", "Heavy Tank", "Aircraft Carrier", "Disposable Anti Tank Rocket", "Jet Bomber", "Midget Submarine", "Missile Submarine", "Jet Attacker", "VTOL Attacker", "Jet Interceptor", "Light Carrier", "APC", "IFV", "Armored Car", "Light Tank", "Destroyer", "Cruiser", "Jet", "CAS Jet", "SPAA", "Utility Helicopter", "Attack Helicopter", "Transport Helicopter", "Transport Aircraft"};

    public Vehicle(String name, String type, String country, int units, double cost) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.units = units;
        this.cost = cost;
    }

    @Override
    public Object[] getAtts() {
        return new Object[]{name, type, country, units, cost};
    }

    @Override
    public void setAtts(Object[] atts) {
        if (atts.length == 5) {
            this.name = (String) atts[0];
            this.type = (String) atts[1];
            this.country = (String) atts[2];
            this.units = (int) atts[3];
            this.cost = (double) atts[4];
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getUnits() {
        return units;
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
                + ", units=" + units
                + ", cost=" + cost
                + '}';
    }

    public static String[] getTypes() {
        return types;
    }

}
