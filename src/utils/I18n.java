package utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
    private static ResourceBundle bundle = ResourceBundle.getBundle("resources/messages", Locale.getDefault());

    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("resources/messages", locale);
    }

    public static String t(String key) {
        return bundle.containsKey(key) ? bundle.getString(key) : key;
    }
}