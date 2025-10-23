package org.whogames.digitalminaturia.Combat.Entities;

public abstract class Weapon implements Entity {

    public static String types[] = {"Assault Rifle", "Rifle", "Torpedo", "Depth Charge", "Howitzer", 
    "Battle Rifle", "RPG", "Handgun", "HMG", "SMG", "GPMG", "LMG", "Cannon", "Cannon Round", "Autocannon", 
    "Grenade Launcher", "Rifle Grenade", "Recoilless Gun"};

    protected int damage;

    public Weapon(int damage) {
        this.damage = damage;
    }
    
}
