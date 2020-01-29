package pro.paulek.CraftEssentials.cache;

import pro.paulek.CraftEssentials.settings.IConfiguration;

public interface ICache extends IConfiguration {

    void load();

    void save();

}
