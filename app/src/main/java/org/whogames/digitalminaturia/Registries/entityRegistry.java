package org.whogames.digitalminaturia.Registries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.whogames.digitalminaturia.Combat.Entities.Entity;

public class entityRegistry {
     private static final Map<String, Entity> entities = new HashMap<>();

    public static void register(Entity entity) {
        entities.put(entity.getName(), entity);
    }

    public static Entity get(String name) {
        return entities.get(name);
    }

    public static Collection<Entity> all() {
        return entities.values();
    }

    
}
