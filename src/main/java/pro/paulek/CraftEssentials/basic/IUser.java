package pro.paulek.CraftEssentials.basic;

import org.bukkit.Location;

import java.util.List;
import java.util.Locale;

public interface IUser {

    Locale getLocale();

    void setLocale();

    boolean hasPermission(String node);

    //boolean hasCommandPermission;

    boolean isPermissionSet(String node);

    void sendMessage(String message);

    boolean isVanished();

    void setVanished(boolean vanish);

    void setLoginPoint();

    void setLogoutPoint();

    void setPlayedTime();

    long getPlayedTime();

    void setLogoutLocation();

    void setLoginLocation();

    void setLastLogin();

    void setLastQuit();

    void setAfk(boolean set);

    boolean isAfk();

    void setGotMode(boolean set);

    boolean isGodMode();

    Location getHome(String name) throws Exception;

    Location getHome(Location loc) throws Exception;

    List<String> getHomes();

    void setHome(String name, Location loc);

    void delHome(String name) throws Exception;

    boolean hasHome();

    void setIgnoreMsg(boolean ignoreMsg);

    boolean isIgnoreMsg();

    String getAfkMessage();

    void setAfkMessage(final String message);

    long getAfkSince();

}
