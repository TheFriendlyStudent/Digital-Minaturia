package org.whogames.digitalminaturia.Combat;

import java.util.HashMap;

import org.whogames.digitalminaturia.Country;
import org.whogames.digitalminaturia.Province;

public class Battalion {
    
    public Platoon[] platoons = new Platoon[5];
    private static HashMap<String, Battalion> battalionMap = new HashMap<>();
    public Province location;
    public String name;
    public Country country;

    public Battalion(String name, Country country, Province location) {
        this.name = name;
        this.country = country;
        this.location = location;
        battalionMap.put(name, this);
        for (int i = 0; i < platoons.length; i++) {
            platoons[i] = new Platoon(new groundSquad(i,"GS"+i, "United Republic of Stoneland"), new groundSquad(i*2, "GS"+i*2, "United Republic of Stoneland"));
        }
    }

    public void moveToProvince(Province newLocation) {
        this.location = newLocation;
    }
}
