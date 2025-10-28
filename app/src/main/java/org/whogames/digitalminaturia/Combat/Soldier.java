package org.whogames.digitalminaturia.Combat;

public class Soldier {

    private final static int BASE_HEALTH = 3;
    private final int id;
    private final String type;
    private String kit;
    private final boolean canDrive;

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
