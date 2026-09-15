package org.example;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.jboss.logging.provider", "slf4j");
        Gui gui = new Gui();
        gui.run();
    }
}
