package org.whogames.digitalminaturia;

import javax.swing.SwingUtilities;

import org.whogames.digitalminaturia.UI.SVGMapViewer;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                SVGMapViewer exs = new SVGMapViewer();
                System.err.println(getGreeting());
            } catch (Exception e) {
                System.out.println("[ERROR] Exception in main:");
            }
        });
    }
    
    public static String getGreeting() {
        return "Hello, welcome to Minaturia!";
    }
}
