package pro.paulek.CraftEssentials;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;;
import pro.paulek.CraftEssentials.settings.II18n;
import pro.paulek.CraftEssentials.settings.Settings;
import pro.paulek.CraftEssentials.user.IUser;
import pro.paulek.api.data.Cache;
import pro.paulek.api.data.DataModel;
import pro.paulek.api.database.Database;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

public interface ICraftEssentials extends Plugin {

    /**
     * Get current plugin storage method
     * @return
     */
    DataModel getPluginDataModel();

    /**
     * Reloads whole plugin
     */
    void reload();

    /**
     * Initializes database type
     */
    Database initDatabase();

    /**
     * Get user object (settings etc)
     * @param uuid UUID of Player
     * @return User object
     */
    IUser getUser(UUID uuid);

    /**
     * Get user object (settings etc)
     * @param nick Nick of Player
     * @return User object
     */
    IUser getUser(String nick);

    /**
     * Get user object (settings etc)
     * @param player Player object
     * @return User object
     */
    IUser getUser(Player player);

    /**
     *
     * @return I18n locale settings, each player should have different language
     */
    II18n getI18n();

    Settings getSettings();

    ExecutorService getExecutorService();

    Cache<IUser, UUID> getUserCache();

    int scheduleSyncDelayedTask(Runnable run);

    int scheduleSyncDelayedTask(Runnable run, long delay);

    int scheduleSyncRepeatingTask(Runnable run, long delay, long period);

}
