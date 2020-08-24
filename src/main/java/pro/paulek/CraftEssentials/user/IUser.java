package pro.paulek.CraftEssentials.user;

import org.bukkit.Location;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

public interface IUser {

    Locale getLocale();

    void setLocale(Locale locale);

    boolean hasPermission(String node);

    //boolean hasCommandPermission;

    boolean isPermissionSet(String node);

    void sendMessage(String message);

    boolean isVanished();

    void setVanished(boolean vanish);

    void setLoginPoint(Location location);

    void setLogoutPoint(Location location);

    void setPlayedTime(long time);

    long getPlayedTime();

    void setLogoutLocation(Location location);

    void setLoginLocation(Location location);

    void setLastLogin(ZonedDateTime time);

    void setLastQuit(ZonedDateTime time);

    void setAfk(boolean set);

    boolean isAfk();

    void setGodMode(boolean set);

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
