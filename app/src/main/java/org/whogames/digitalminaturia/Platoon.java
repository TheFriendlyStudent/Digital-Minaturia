package org.whogames.digitalminaturia;

public class Platoon {

    public Squad[] squads = new Squad[2];

    public Platoon(Squad squad1, Squad squad2) {
        this.squads[0] = squad1;
        this.squads[1] = squad2;
    }

    public Squad getSquad(int index) {
        if (index < 0 || index >= squads.length) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return squads[index];
    }
    
}
