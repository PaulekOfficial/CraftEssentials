package pro.paulek.CraftEssentials;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.diorite.cfg.system.Template;
import org.diorite.cfg.system.TemplateCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.paulek.CraftEssentials.commands.Gamemode;
import pro.paulek.CraftEssentials.data.UserCache;
import pro.paulek.CraftEssentials.listeners.UserListeners;
import pro.paulek.CraftEssentials.settings.II18n;
import pro.paulek.CraftEssentials.util.AzureTranslator;
import pro.paulek.CraftEssentials.util.Translator;
import pro.paulek.api.data.Cache;
import pro.paulek.CraftEssentials.settings.I18n;
import pro.paulek.CraftEssentials.settings.Settings;
import pro.paulek.CraftEssentials.user.IUser;
import pro.paulek.CraftEssentials.util.Reflection;
import pro.paulek.api.data.DataModel;
import pro.paulek.api.database.Database;
import pro.paulek.api.database.MySQL;
import pro.paulek.api.database.SQLite;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class CraftEssentials extends JavaPlugin implements ICraftEssentials {

    static final Logger logger = LoggerFactory.getLogger(CraftEssentials.class);

    private Settings settings;
    private II18n i18n;
    private Translator translator;

    private PaperCommandManager commandManager;
    private DataModel dataModel;
    private Database database;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Map<Class, Cache> cachesMap = new HashMap<>(128);

    private final Map<Class, Listener> listenersMap = new HashMap<>(128);

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        //Init config
        settings = initSettings();
        this.reloadTranslator();

        //Init i18n
        i18n = new I18n(this, translator);
        i18n.loadLocale(settings.defaultLocale);

        //Init database
        database = this.initDatabase();

        //Init cache
        this.registerStorages();

        //Init commands
        commandManager = new PaperCommandManager(this);
        commandManager.usePerIssuerLocale(true);
        commandManager.enableUnstableAPI("help");
        this.registerCommands();

        //Init listeners
        this.getServer().getPluginManager().registerEvents(new UserListeners(this), this);
    }

    public void registerStorages() {
        Stream<Class<? extends Cache>> caches = Stream.of(
                UserCache.class
        );

        caches.forEach(cacheClazz -> {
            try {
                Cache cache = cacheClazz.getConstructor().newInstance();
                cache.init(this, logger);
                this.cachesMap.put(cacheClazz, cache);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException exception) {
                logger.error("cannot create and load cache instance {} {}", cacheClazz.getName(), exception);
            }
        });
    }

    public void registerListeners() {
        Stream<Class<? extends Listener>> listeners = Stream.of(
                UserListeners.class
        );

        listeners.forEach(listenerClazz -> {
            try {
                Listener listener = listenerClazz.getConstructor(ICraftEssentials.class, Logger.class).newInstance(this, logger);
                this.getServer().getPluginManager().registerEvents(listener, this);
                this.listenersMap.put(listenerClazz, listener);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException exception) {
                logger.error("cannot create and load cache instance {} {}", listenerClazz.getName(), exception);
            }
        });
    }

    public void registerCommands() {
        commandManager.registerCommand(new Gamemode(this));


        //Init CommandCompletions
        commandManager.getCommandCompletions().registerCompletion("gamemodes", c -> ImmutableList.of("survival", "adventure", "spectator", "creative"));
    }

    public void reloadTranslator() {
        if (!settings.useTranslator || !settings.i18n) {
            return;
        }
        switch (settings.translatorApi.toLowerCase()) {
            case "microsoft":
                new AzureTranslator(settings.translatorEndpointApi, settings.translatorKey);
                break;
            case "google":
                throw new UnsupportedOperationException("Not implemented yet.");
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void reload() {
        this.initSettings();

        this.database = this.initDatabase();

        this.reloadTranslator();

        HandlerList.unregisterAll(this);
        this.registerListeners();

        this.commandManager.unregisterCommands();
        this.registerCommands();
    }

    @Override
    public DataModel getPluginDataModel() {
        return dataModel;
    }

    private Settings initSettings() {
        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdir();
        }
        return loadConfig(new File(this.getDataFolder(), "settings.yml"), Settings.class);
    }

    @Override
    public Database initDatabase(){
        dataModel = DataModel.getModelByName(settings.storageType);
        if(dataModel.equals(DataModel.MYSQL)) {
            MySQL mySQL = new MySQL(settings.mysql);
            mySQL.init();
            return mySQL;
        }
        File databaseFile = new File(this.getDataFolder(), "database.db");
        if(!databaseFile.exists()){
            try {
                databaseFile.createNewFile();
            } catch (IOException exception){
                exception.printStackTrace();
            }
        }
        SQLite sqLite = new SQLite(databaseFile);
        sqLite.init();
        return sqLite;
    }

    @Override
    public Cache<IUser, UUID> getUserCache() {
        return cachesMap.get(UserCache.class);
    }

    public IUser getUser(UUID uuid) {
        return this.getUserCache().get(uuid);
    }

    public IUser getUser(String nick) {
        return getUser(Objects.requireNonNull(Bukkit.getPlayer(nick)));
    }

    public IUser getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public II18n getI18n() {
        return i18n;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    public int scheduleSyncDelayedTask(Runnable run) {
        return 0;
    }

    public int scheduleSyncDelayedTask(Runnable run, long delay) {
        return 0;
    }

    public int scheduleSyncRepeatingTask(Runnable run, long delay, long period) {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadConfig(File file, Class<T> implementationClass){

        Constructor<T> constructor = (Constructor<T>) Reflection.getConstructor(implementationClass);
        Template<T> template = TemplateCreator.getTemplate(implementationClass);

        T config = null;

        if(!file.exists()){
            try{
                config = template.fillDefaults(constructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }
        } else {
            try {
                config = template.load(file);
                if(config == null){
                    config = template.fillDefaults(implementationClass.newInstance());
                }
            } catch (IOException | IllegalAccessException | InstantiationException e){
                e.printStackTrace();
            }
        }

        try{
            template.dump(file, config, false);
        } catch (IOException e){
            e.printStackTrace();
        }

        return config;
    }
}
