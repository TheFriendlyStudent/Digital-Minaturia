package org.whogames.digitalminaturia.Registries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.whogames.digitalminaturia.Province;

public class provinceRegistry {
     private static final Map<Integer, Province> provinces = new HashMap<>();

    public static void register(Province province) {
        provinces.put(province.getId(), province);
    }

    public static Province get(Integer id) {
        return provinces.get(id);
    }

    public static Collection<Province> all() {
        return provinces.values();
    }

    
}
