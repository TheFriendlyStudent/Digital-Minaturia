package org.whogames.digitalminaturia.Combat;

public class Soldier {

    private final static int BASE_HEALTH = 3;
    private final int id;
    private final String type;
    private final boolean canDrive;

    private static final String types[] = {
            "Rifleman", "Grenadier", "Automatic Rifleman", "Designated Marksman", "Sniper",
            "Anti-Tank Specialist", "Medic", "Engineer", "Squad Leader", "Commanding Officer"
    };

    public Soldier(int id, String type, boolean canDrive) {
        this.id = id;
        this.type = type;
        this.canDrive = canDrive;
    }

}
