package org.whogames.digitalminaturia.Combat;

import java.util.ArrayList;

public abstract class Squad {

    private int id;
    private String name;
    protected int health;
    private String type;
    protected String country;
    private String ammo;
    private ArrayList<Soldier> soldiers = new ArrayList<>();

    public Squad() {
        this.id = 0;
        this.name = "Unnamed Squad";
        this.health = 0;
        this.type = "Infantry";
        this.country = "Unknown";
        this.ammo = "Standard";
    }

    public Squad(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.health = 0;
        this.type = "Infantry";
        this.country = country;
        this.ammo = "Standard";
    }

    public Squad(int id, String name, int health, String type, String country, String ammo, int personnel) {
        this.id = id;
        this.name = name;
        this.health = health;
        this.type = type;
        this.country = country;
        this.ammo = ammo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAmmo() {
        return ammo;
    }

    public void setAmmo(String ammo) {
        this.ammo = ammo;
    }

}
