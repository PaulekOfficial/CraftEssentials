package pro.paulek.CraftEssentials;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.diorite.cfg.system.Template;
import org.diorite.cfg.system.TemplateCreator;
import pro.paulek.CraftEssentials.commands.Gamemode;
import pro.paulek.CraftEssentials.settings.I18n;
import pro.paulek.CraftEssentials.settings.Settings;
import pro.paulek.CraftEssentials.user.IUser;
import pro.paulek.CraftEssentials.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class CraftEssentials extends JavaPlugin implements ICraftEssentials {

    private Settings settings;
    private PaperCommandManager commandManager;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        //Init commands
        commandManager = new PaperCommandManager(this);
        commandManager.usePerIssuerLocale(true);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new Gamemode());


        //Init CommandCompletions
        commandManager.getCommandCompletions().registerCompletion("gamemodes", c -> {
            return ImmutableList.of("survival", "adventure", "spectator", "creative");
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void reload() {

    }

    public void initDatabase() {

    }

    public IUser getUser(UUID uuid) {
        return null;
    }

    public IUser getUser(String nick) {
        return null;
    }

    public IUser getUser(Player player) {
        return null;
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

        Constructor<T> constructor = (Constructor<T>) ReflectionUtils.getConstructor(implementationClass);
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
