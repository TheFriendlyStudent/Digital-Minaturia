package org.whogames.digitalminaturia.Registries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.whogames.digitalminaturia.Combat.Squad;

public class squadRegistry {
     private static final Map<String, Squad> squads = new HashMap<>();

    public static void register(Squad squad) {
        squads.put(squad.getName(), squad);
    }

    public static Squad get(String name) {
        return squads.get(name);
    }

    public static Collection<Squad> all() {
        return squads.values();
    }

    
}
