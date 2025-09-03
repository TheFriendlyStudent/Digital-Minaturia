package org.whogames.digitalminaturia;

import java.util.HashMap;

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
    }
}
