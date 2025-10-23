package org.whogames.digitalminaturia.Combat;

import java.util.HashMap;

import org.whogames.digitalminaturia.Country;
import org.whogames.digitalminaturia.Province;

public class Battalion {

    public Company[] companies = new Company[10];
    public static HashMap<String, Battalion> battalionMap = new HashMap<>();
    public Province location;
    public String name;
    public Country country;

    public Battalion(String name, Country country, Province location) {
        this.name = name;
        this.country = country;
        this.location = location;
        for (int i = 0; i < companies.length; i++) {
            companies[i] = new Company("Company"+i, country, location);
        }
    }

    public Province getLocation() {
        return location;
    }

    public void moveToProvince(Province newLocation) {
        this.location = newLocation;
    }
    
}
