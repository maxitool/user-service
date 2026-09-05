package org.example;

public class Main {
    static void main() {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.jboss.logging.provider", "slf4j");
        Gui gui = new Gui();
        gui.run();
    }
}
