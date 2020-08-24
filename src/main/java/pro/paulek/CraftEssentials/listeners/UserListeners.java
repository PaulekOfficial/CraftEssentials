package pro.paulek.CraftEssentials.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pro.paulek.CraftEssentials.ICraftEssentials;
import pro.paulek.CraftEssentials.user.IUser;
import pro.paulek.CraftEssentials.user.User;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class UserListeners implements Listener {

    private final ICraftEssentials plugin;

    public UserListeners(ICraftEssentials plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @EventHandler
    private void playerPreLoginLoad(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        IUser user = plugin.getUser(uuid);
        if(user == null) {
            return;
        }
        plugin.getUserCache().add(uuid, user);
        Bukkit.broadcastMessage(user.getLocale().getLanguage());
    }

    @EventHandler
    private void playerLoginProfileCreate(PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, run -> {
            UUID uuid = event.getPlayer().getUniqueId();
            IUser user = plugin.getUser(uuid);
            if(user != null) {
                return;
            }
            user = new User(uuid, Locale.forLanguageTag(event.getPlayer().getLocale().substring(0, 2)));
            plugin.getUserCache().add(uuid, user);
            plugin.getUserCache().save(uuid, user);
            Bukkit.broadcastMessage(user.getLocale().getLanguage());
            Bukkit.broadcastMessage(event.getPlayer().getLocale());
        }, 100);

    }

    @EventHandler
    private void playerQuitUnload(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        plugin.getUserCache().remove(uuid);
    }

}
