package org.whogames.digitalminaturia;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                SVGMapViewer exs = new SVGMapViewer();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
    
    public String getGreeting() {
        return "Hello, welcome to Minaturia!";
    }
}
