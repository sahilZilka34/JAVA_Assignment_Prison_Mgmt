package localisation;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LocaleManager — demonstrates Java Localisation with ResourceBundle
 *
 * Analogy: ResourceBundle is a phrase book.
 * - The KEY is what you ask for ("welcome")
 * - The LOCALE is which language edition to use (EN or GA)
 * - The VALUE is the translation returned ("Welcome..." or "Failte...")
 *
 * Your application code never changes — only the locale switches.
 * This is the entire point of localisation: separate text from logic.
 */
public class LocaleManager {

    // The base name of your properties files (without _en / _ga / .properties)
    // Java finds messages_en.properties or messages_ga.properties automatically
    private static final String BUNDLE_NAME = "messages";

    private ResourceBundle bundle;
    private Locale currentLocale;

    // Start in English by default
    public LocaleManager() {
        setLocale(Locale.ENGLISH);
    }

    /**
     * Switch the active language
     * After calling this, all getString() calls return the new language
     */
    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        // ResourceBundle.getBundle() finds the right .properties file
        // For Locale.ENGLISH it loads messages_en.properties
        // For Locale("ga")  it loads messages_ga.properties
        this.bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    /**
     * Get a translated string by key
     *
     * @param key  the property key e.g. "welcome"
     * @return     the translated value for the current locale
     */
    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (java.util.MissingResourceException e) {
            // Fallback: return the key itself if translation is missing
            // Better than crashing — production systems always do this
            return "[" + key + "]";
        }
    }

    /**
     * Demonstrate switching between English and Irish
     * Shows all keys in both languages side by side
     */
    public void runDemo() {
        System.out.println("\n--- Localisation: ResourceBundle Demo ---\n");

        // Define the keys we want to demonstrate
        String[] keys = {
            "welcome",
            "prisoner.admitted",
            "prisoner.not.found",
            "cell.assigned",
            "cell.full",
            "report.generated",
            "incident.reported",
            "language.current",
            "goodbye"
        };

        // --- English ---
        setLocale(Locale.ENGLISH);
        System.out.println("  Locale: " + currentLocale.getDisplayLanguage());
        System.out.println("  Bundle: messages_en.properties");
        System.out.println();
        for (String key : keys) {
            System.out.printf("  %-25s -> %s%n", key, getString(key));
        }

        System.out.println();

        // --- Irish ---
        // There's no built-in Locale constant for Irish (Gaeilge)
        // so we create one with the ISO 639-1 code "ga"
        setLocale(new Locale("ga"));
        System.out.println("  Locale: " + currentLocale.getDisplayLanguage());
        System.out.println("  Bundle: messages_ga.properties");
        System.out.println();
        for (String key : keys) {
            System.out.printf("  %-25s -> %s%n", key, getString(key));
        }

        System.out.println();

        // --- Live switching demonstration ---
        System.out.println("  --- Live switching demonstration ---");
        System.out.println("  Switching language mid-session:\n");

        setLocale(Locale.ENGLISH);
        System.out.println("  [EN] " + getString("welcome"));

        setLocale(new Locale("ga"));
        System.out.println("  [GA] " + getString("welcome"));

        setLocale(Locale.ENGLISH);
        System.out.println("  [EN] " + getString("prisoner.admitted"));

        setLocale(new Locale("ga"));
        System.out.println("  [GA] " + getString("prisoner.admitted"));
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public String getCurrentLanguage() {
        return currentLocale.getDisplayLanguage();
    }
}