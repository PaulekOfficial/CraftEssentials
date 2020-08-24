package pro.paulek.CraftEssentials;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.diorite.cfg.system.Template;
import org.diorite.cfg.system.TemplateCreator;
import pro.paulek.CraftEssentials.commands.Gamemode;
import pro.paulek.CraftEssentials.data.UserCache;
import pro.paulek.CraftEssentials.listeners.UserListeners;
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
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class CraftEssentials extends JavaPlugin implements ICraftEssentials {

    private Settings settings;
    private PaperCommandManager commandManager;
    private DataModel dataModel;
    private Database database;

    private Cache<IUser, UUID> userCache;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        //Init config
        settings = initSettings();

        //Init database
        database = initDatabase();

        //Init cache
        userCache = new UserCache(this);
        userCache.init();

        //Init commands
        commandManager = new PaperCommandManager(this);
        commandManager.usePerIssuerLocale(true);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new Gamemode());


        //Init CommandCompletions
        commandManager.getCommandCompletions().registerCompletion("gamemodes", c -> {
            return ImmutableList.of("survival", "adventure", "spectator", "creative");
        });

        //Init listeners
        this.getServer().getPluginManager().registerEvents(new UserListeners(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void reload() {

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
        return userCache;
    }

    public IUser getUser(UUID uuid) {
        return userCache.get(uuid);
    }

    public IUser getUser(String nick) {
        return getUser(Objects.requireNonNull(Bukkit.getPlayer(nick)));
    }

    public IUser getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public I18n getI18n() {
        return null;
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
