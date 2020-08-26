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
            String localeString = event.getPlayer().getLocale();
            Locale locale = null;
            if (!localeString.isEmpty()) {
                final String[] parts = localeString.split("[_\\.]");
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
            user = new User(uuid, locale);
            plugin.getUserCache().add(uuid, user);
            plugin.getUserCache().save(uuid, user);
        }, 100);

    }

    @EventHandler
    private void playerQuitUnload(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        plugin.getUserCache().remove(uuid);
    }

}
