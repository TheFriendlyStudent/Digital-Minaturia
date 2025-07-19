package org.whogames.digitalminaturia;

public abstract class Squad {

    private int id;
    private String name;
    private int health;
    private String type;
    private String country;
    private String ammo;

    public Squad(int id, String name, int health, String type, String country, String ammo) {
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
