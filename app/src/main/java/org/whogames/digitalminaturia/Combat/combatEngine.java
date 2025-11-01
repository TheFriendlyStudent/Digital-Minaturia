package org.whogames.digitalminaturia.Combat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.whogames.digitalminaturia.Province;
import org.whogames.digitalminaturia.ProvinceParser;
import org.whogames.digitalminaturia.UI.SVGMapViewer;

public class combatEngine {

    private ArrayList<ArrayList<Edge>> adjList = new ArrayList<>();
    private ArrayList<Province> provinces;

    public combatEngine() {
        try {
            this.adjList = ProvinceParser.parseEdges(new FileReader(new File(SVGMapViewer.dataDir, "Minaturia Edges.csv")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.provinces = new ArrayList<>(SVGMapViewer.provinceMap.values().stream().toList());

        for (Province p : provinces) {
            System.out.print(p.getId() + "(" + p.getName() + ")" + " borders: ");
                for (Province n : p.getNeighboringProvinces()) {
                    System.out.print(n.getName() + " ");
                }   
                System.out.println();
        }
    }

    public void simulateBattle(Company attacker, Company defender) {
        // Implement combat simulation logic here
    }

    public boolean simulateFirefight(Platoon attacker, Platoon defender) {
        // Implement firefight simulation logic here
        while ((attacker.squads[0].health > 0 || attacker.squads[1].health > 0) &&
               (defender.squads[0].health > 0 || defender.squads[1].health > 0)) {
            // Example logic: each squad inflicts random damage to the opposing squad
            for (int i = 0; i < 2; i++) {
                int attackDamage = (int)(Math.random() * 20) + 1; // Random damage between 1 and 20
                defender.squads[i].health -= attackDamage;
                if (defender.squads[i].health < 0) defender.squads[i].health = 0;

                attackDamage = (int)(Math.random() * 20) + 1; // Random damage between 1 and 20
                attacker.squads[i].health -= attackDamage;
                if (attacker.squads[i].health < 0) attacker.squads[i].health = 0;
            }
            
        }
        return false;
    }

    public static void moveBattalion(Battalion battalion, Province newLocation) {
        if (battalion.getLocation() == newLocation) {
            return; // No movement needed
        }
        if (battalion.getLocation().getNeighboringProvinces().contains(newLocation)){
            battalion.moveToProvince(newLocation);
        }
        else{

        }
        battalion.moveToProvince(newLocation);
    }

    
}

