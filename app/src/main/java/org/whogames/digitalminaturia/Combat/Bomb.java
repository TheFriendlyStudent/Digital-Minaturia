package org.whogames.digitalminaturia.Combat;

public class Bomb implements Entity {

    private String name;
    private String type;
    private int units;
    private String country;
    private double cost;

    @Override
    public String getName() {
        return "Bomb";
    }

    @Override
    public String getTypeClass() {
        return null;
    
    }

    @Override
    public String getSubclass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCountry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getProductionCost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLaborCost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getWeight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object[] getAtts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAtts(Object[] atts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}