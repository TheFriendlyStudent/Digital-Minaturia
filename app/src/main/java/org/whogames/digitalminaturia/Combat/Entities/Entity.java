package org.whogames.digitalminaturia.Combat.Entities;

public interface Entity {

    String getName();

    String getTypeClass();

    String getSubclass();

    String getCountry();

    int getProductionCost();

    int getLaborCost();

    int getWeight();

    Object[] getAtts();

    void setAtts(Object[] atts);

}
