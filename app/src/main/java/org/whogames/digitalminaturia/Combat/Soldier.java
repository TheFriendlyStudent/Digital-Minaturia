package org.whogames.digitalminaturia.Combat;

import org.whogames.digitalminaturia.Combat.Entities.Weapon;
import java.util.ArrayList;

public class Soldier {

    private final static int BASE_HEALTH = 3;
    private int health;
    private final int id;
    private final String type;
    private String kit;
    private final boolean canDrive;
    private ArrayList<Weapon> weapons;

    private static final String types[] = {
            "Rifleman", "Grenadier", "Automatic Rifleman", "Designated Marksman", "Sniper",
            "Anti-Tank Specialist", "Medic", "Engineer", "Squad Leader", "Commanding Officer"
    };

    public Soldier(int id, String type, String kit) {
        this.id = id;
        this.type = type;
        this.kit = kit;
        this.canDrive = type.equals("Mechanic");
    }

    public String getType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    }

}
