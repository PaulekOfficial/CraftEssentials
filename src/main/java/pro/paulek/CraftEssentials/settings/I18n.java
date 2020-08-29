package pro.paulek.CraftEssentials.settings;

import org.bukkit.ChatColor;
import pro.paulek.CraftEssentials.objects.Job;
import pro.paulek.CraftEssentials.ICraftEssentials;
import pro.paulek.CraftEssentials.util.AzureTranslator;
import pro.paulek.CraftEssentials.util.Translator;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class I18n implements II18n {

    private final Translator translator = new AzureTranslator("https://api.cognitive.microsofttranslator.com", "f30afa09c06a42609c9c82aeb8eba77f");

    private final static String MESSAGES_FILE_NAME = "messages";
    private static final Pattern NO_DOUBLE_MARK = Pattern.compile("''");

    private final Locale defaultLocale = Locale.ENGLISH;
    private transient ResourceBundle defaultBundle = ResourceBundle.getBundle(MESSAGES_FILE_NAME, Locale.ENGLISH, new UTF8PropertiesControl());
    private final transient Map<Locale, ResourceBundle> resourceBundleMap = new HashMap<>();
    private final transient Map<String, MessageFormat> messageFormatMap = new HashMap<>();
    private ICraftEssentials craftEssentials;

    private List<Locale> languagesInTranslations = new ArrayList<>();

    public I18n(ICraftEssentials craftEssentials) {
        this.craftEssentials = Objects.requireNonNull(craftEssentials);
    }

    @Override
    public String translate(String key, Locale locale) {
        try {
            try {
                if(!resourceBundleMap.containsKey(locale)) {
                    this.loadLocale(locale.toString());
                }
                if(!resourceBundleMap.get(locale).getLocale().getLanguage().equalsIgnoreCase(locale.getLanguage()) && !languagesInTranslations.contains(locale)) {
                    Job<Runnable> job = new Job<>(new Runnable() {
                        @Override
                        public void run() {
                            craftEssentials.getI18n().translateLocale(locale);
                        }
                    }, true, craftEssentials);
                    craftEssentials.getJobs().add(job);
                }
                return fixColor(resourceBundleMap.get(locale).getString(key));
            } catch (MissingResourceException exception) {
                return fixColor(defaultBundle.getString(key));
            }
        } catch (MissingResourceException exception) {
            craftEssentials.getLogger().log(Level.WARNING, String.format("Missing translation key \"%s\" in translation file %s", exception.getKey(), locale.toString()), exception);
        }
        return key;
    }

    @Override
    public void translateLocale(Locale locale) {
        craftEssentials.getLogger().log(Level.INFO, String.format("Prepare to create translation file %s", locale.toString()));
        languagesInTranslations.add(locale);
        File file = new File(craftEssentials.getDataFolder(), "messages_" + locale.getLanguage() + ".properties");

        try {
            if (!file.createNewFile()) {
                craftEssentials.getLogger().log(Level.WARNING, String.format("Fail to create new translation file %s, writable: %s", locale.toString(), file.canWrite()));
                return;
            }
        } catch (IOException exception) {
            craftEssentials.getLogger().log(Level.WARNING, String.format("Critical fail to create new translation file %s", locale.toString()), exception);
            return;
        }

        Map<String,String> toBeTranslated = new LinkedHashMap<>();

        for(String key : defaultBundle.keySet()) {
            toBeTranslated.put(key, translator.ignoreColorCodesInTranslation(defaultBundle.getString(key)));
        }

        List<Translator.Translation> translations = translator.translate(locale, toBeTranslated.values().toArray(new String[]{}));

        Properties properties = new Properties();

        int i = 0;
        for(String key : toBeTranslated.keySet()) {
            properties.put(key, translations.get(i).getMessage());
            i++;
        }

        try(FileOutputStream fileInputStream = new FileOutputStream(file); Writer writer = new OutputStreamWriter(fileInputStream, StandardCharsets.UTF_8)) {
            properties.store(writer, "");
        } catch (IOException exception) {
            craftEssentials.getLogger().log(Level.WARNING, String.format("Cannot save new translation file %s", locale.toString()), exception);
            return;
        }

        languagesInTranslations.remove(locale);
        craftEssentials.getLogger().log(Level.INFO, String.format("Finished creating translation file %s", locale.toString()));

        this.loadLocale(locale.toString());
    }

    @Override
    public ResourceBundle getResourceBundle(Locale locale) {
        return resourceBundleMap.getOrDefault(locale, defaultBundle);
    }

    @Override
    public ResourceBundle getDefaultBundle() {
        return defaultBundle;
    }

    @Override
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    @Override
    public String format(final String key, Locale locale, Object... objects) {
        String format = translate(key, locale);
        MessageFormat messageFormat = messageFormatMap.get(format);
        if (messageFormat == null) {
            try {
                messageFormat = new MessageFormat(format);
            } catch (IllegalArgumentException e) {
                craftEssentials.getLogger().log(Level.WARNING, "Invalid Translation key for '" + key + "': " + e.getMessage());
                format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
                messageFormat = new MessageFormat(format);
            }
            messageFormatMap.put(format, messageFormat);
        }
        return messageFormat.format(objects).replace(' ', ' '); // replace nbsp with a space
    }

    @Override
    public void loadLocale(final String localeName) {
        Locale locale = null;
        if (localeName != null && !localeName.isEmpty()) {
            final String[] parts = localeName.split("[_\\.]");
            if (parts.length == 1) {
                locale = new Locale(parts[0]);
            }
            if (parts.length == 2) {
                locale = new Locale(parts[0], parts[1]);
            }
            if (parts.length == 3) {
                locale = new Locale(parts[0], parts[1], parts[2]);
            }
        }
        craftEssentials.getLogger().log(Level.INFO, String.format("Loading locale %s", locale));

        ResourceBundle resourceBundle = null;
        try {
            resourceBundle = ResourceBundle.getBundle(MESSAGES_FILE_NAME, locale, new FileResClassLoader(I18n.class.getClassLoader(), craftEssentials), new UTF8PropertiesControl());
        } catch (MissingResourceException exception) {
            craftEssentials.getLogger().log(Level.WARNING, "No translations file found in plugin folder for locale: " + locale.toString(), exception);
            //craftEssentials.getLogger().log(Level.WARNING, "No translations file found in plugin folder for locale: " + locale.toString());
        }

        if(resourceBundle != null) {
            resourceBundleMap.put(locale, resourceBundle);
            return;
        }

        try {
            resourceBundle = ResourceBundle.getBundle(MESSAGES_FILE_NAME, locale, new UTF8PropertiesControl());
        } catch (Exception exception) {
            //Never will happen
            craftEssentials.getLogger().log(Level.WARNING, "No translations folder found in plugin file for locale: " + locale.toString(), exception);
        }

        if(resourceBundle != null) {
            resourceBundleMap.put(locale, resourceBundle);
        }

    }

    private String fixColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public void reload() {

    }

    // <-- FROM ESSENTIALS X https://github.com/EssentialsX/Essentials/blob/d2f2140be9e2f9d75df6b910232c6df99f998318/Essentials/src/com/earth2me/essentials/I18n.java#L170 -->

    /**
     * Attempts to load properties files from the plugin directory before falling back to the jar.
     */
    private static class FileResClassLoader extends ClassLoader {
        private final transient File dataFolder;

        FileResClassLoader(final ClassLoader classLoader, final ICraftEssentials craftEssentials) {
            super(classLoader);
            this.dataFolder = craftEssentials.getDataFolder();
        }

        @Override
        public URL getResource(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return file.toURI().toURL();
                } catch (MalformedURLException ignored) {}
            }
            return null;
        }

        @Override
        public InputStream getResourceAsStream(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException ignored) {}
            }
            return null;
        }
    }

    /**
     * Reads .properties files as UTF-8 instead of ISO-8859-1, which is the default on Java 8/below.
     * Java 9 fixes this by defaulting to UTF-8 for .properties files.
     */
    private static class UTF8PropertiesControl extends ResourceBundle.Control {
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            String resourceName = toResourceName(toBundleName(baseName, locale), "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // use UTF-8 here, this is the important bit
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }

}
