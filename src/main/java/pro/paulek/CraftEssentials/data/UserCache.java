package pro.paulek.CraftEssentials.data;

import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import pro.paulek.CraftEssentials.data.models.mysql.UserDataModel;
import pro.paulek.CraftEssentials.user.IUser;
import pro.paulek.CraftEssentials.ICraftEssentials;
import pro.paulek.api.data.Cache;
import pro.paulek.api.data.Data;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserCache implements Cache<IUser, UUID> {

    private Data<IUser, UUID> userData;
    private final Map<UUID, IUser> userMap = new ConcurrentHashMap<>(50);
    private ICraftEssentials plugin;

    public UserCache() {
    }

    @Override
    public void init(Plugin plugin, Logger logger) {
        this.plugin = (ICraftEssentials) Objects.requireNonNull(plugin);
        switch (this.plugin.getPluginDataModel()) {
            case MYSQL:
            case FLAT:
            case SQLITE:
                userData = new UserDataModel();
        }
    }

    @Override
    public IUser get(UUID uuid) {
        if(userMap.containsKey(uuid)) {
            return userMap.get(uuid);
        }
        return userData.load(uuid);
    }

    @Override
    public void add(UUID uuid, IUser iUser) {
        userMap.put(uuid, iUser);
        this.save(uuid);
    }

    @Override
    public void delete(UUID uuid) {
        this.remove(uuid);
        userData.delete(uuid);
    }

    @Override
    public void remove(UUID uuid) {
        userMap.remove(uuid);
    }

    @Override
    public void save(UUID uuid, IUser iUser) {
        userData.save(iUser);
    }

    @Override
    public void save(UUID uuid) {
        this.save(uuid, userMap.getOrDefault(uuid, null));
    }

}
