package gui;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleUtil {
    private static Locale currentLocale = new Locale("es");
    private static ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", currentLocale);

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("Etiquetas", currentLocale);
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
