package org.whogames.digitalminaturia;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                SVGMapViewer exs = new SVGMapViewer();
                System.err.println(getGreeting());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("[ERROR] Exception in main:");
                e.printStackTrace();
            }
        });
    }
    
    public static String getGreeting() {
        return "Hello, welcome to Minaturia!";
    }
}
