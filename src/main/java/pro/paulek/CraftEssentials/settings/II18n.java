package pro.paulek.CraftEssentials.settings;

import java.util.Locale;
import java.util.ResourceBundle;

public interface II18n extends IConfiguration {

    /**
     * Translates the message according to the given language, if the language does not exist, it translates using the default machine language.
     * @param key Message key for translation
     * @param locale Target language of the translation
     * @return Translated string in given locale
     */
    String translate(String key, Locale locale);

    /**
     * Returns the language pack for the given language
     * @param locale Target language of the bundle
     * @return ResourceBundle for given locale
     */
    ResourceBundle getResourceBundle(Locale locale);

    /**
     * Returns the default language pack
     * @return Default ResourceBundle
     */
    ResourceBundle getDefaultBundle();

    /**
     * Returns the default locale
     * @return Default Locale
     */
    Locale getDefaultLocale();

//    /**
//     * Formats provide the message according to the plugin settings, and add values ​​to the message
//     * @param message Message to format
//     * @param objects Objects to replace args {0}, {1} etc.
//     * @return Formatted message
//     */
//    String format(String message, Object... objects);

    /**
     * Loads the message in the appropriate language and formats, give the message according to the plugin settings, and adds values ​​to the message
     * @param key Message key for translation
     * @param locale Target language of the message
     * @param objects Objects to replace args {0}, {1} etc.
     * @return Formatted message
     */
    String format(String key, Locale locale, Object... objects);


    /**
     * Loads ResourceBundle form given patch
     * @param path Patch to .locale file
     */
    void loadLocale(String path);

    /**
     * Creates new messages file translated in target locale
     * @param locale
     */
    void translateLocale(Locale locale);

}
