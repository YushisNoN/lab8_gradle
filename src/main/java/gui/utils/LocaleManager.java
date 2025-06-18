package gui.utils;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleManager {
    private static Locale currentLocale = new Locale("ru");
    private static ResourceBundle bundle;

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("locale", currentLocale);
    }
    public static ResourceBundle getBundle() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("locale", currentLocale);
        }
        return bundle;
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}