package org.whogames.digitalminaturia;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SVGMapViewer exs = new SVGMapViewer();
        SwingUtilities.invokeLater(() -> {
            try {
                exs.createAndShowGUI();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    public String getGreeting() {
        return "Hello, welcome to Minaturia!";
    }
}
