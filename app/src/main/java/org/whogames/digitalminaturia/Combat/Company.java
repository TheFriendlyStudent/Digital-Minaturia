package org.whogames.digitalminaturia.Combat;

import java.util.HashMap;

import org.whogames.digitalminaturia.Country;
import org.whogames.digitalminaturia.Province;

public class Company {
    
    public Platoon[] platoons = new Platoon[5];
    public Province location;
    public String name;
    public Country country;

    public Company(String name, Country country, Province location) {
        this.name = name;
        this.country = country;
        this.location = location;
        for (int i = 0; i < platoons.length; i++) {
            platoons[i] = new Platoon(new groundSquad(i,"GS"+i, "United Republic of Stoneland"), new groundSquad(i*2, "GS"+i*2, "United Republic of Stoneland"));
        }
    }
}
