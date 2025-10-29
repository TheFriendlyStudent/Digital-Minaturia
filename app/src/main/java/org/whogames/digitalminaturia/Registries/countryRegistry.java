package org.whogames.digitalminaturia.Registries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.whogames.digitalminaturia.Country;

public class countryRegistry {
     private static final Map<String, Country> countries = new HashMap<>();

    public static void register(Country country) {
        countries.put(country.getName(), country);
    }

    public static Country get(String name) {
        return countries.get(name);
    }

    public static Collection<Country> all() {
        return countries.values();
    }

    
}
