import manager.PrisonMenu;

/**
 * Main.java - Entry point for Prison Management System
 * 
 * Launches interactive menu demonstrating all 12 user stories
 * and 29 Java features (17 fundamentals + 12 advanced)
 */
public class Main {
    
    public static void main(String[] args) {
        // Launch interactive menu system
        PrisonMenu menu = new PrisonMenu();
        menu.start();
    }
}