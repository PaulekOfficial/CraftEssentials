package pro.paulek.CraftEssentials.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pro.paulek.CraftEssentials.ICraftEssentials;
import pro.paulek.CraftEssentials.objects.Job;
import pro.paulek.CraftEssentials.user.IUser;
import pro.paulek.CraftEssentials.user.User;
import pro.paulek.CraftEssentials.util.LocationAndLocale;

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
    }

    @EventHandler
    private void playerLoginProfileCreate(PlayerLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if(plugin.getUser(uuid) != null) {
            return;
        }
        IUser user = new User(uuid, Locale.ENGLISH);
        plugin.getUserCache().add(uuid, user);
        Bukkit.getScheduler().runTaskLater(plugin, run -> {
            user.setLocale(LocationAndLocale.localeFromTag(event.getPlayer().getLocale()));
            plugin.getI18n().loadOrTranslate(user.getLocale());
        }, 100);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, run -> {
            LocationAndLocale.sendPlayerLocale(event.getPlayer(), plugin);
        }, 20 * 30);
    }

    @EventHandler
    private void playerQuitUnload(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        plugin.getUserCache().remove(uuid);
    }

}
