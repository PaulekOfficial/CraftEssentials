package pro.paulek.CraftEssentials.user;

import jdk.internal.jline.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class User implements IUser {

    private UUID          uuid;
    private Locale        locale;

    @Nullable
    private Player        player;

    private boolean       godMode;
    private boolean       vanish;
    private Location      lastLoginPoint;
    private Location      lastLocation;
    private String        afkMessage;
    private boolean       afk;
    private ZonedDateTime aftSince;
    private List<UUID>    ignoredPlayers;

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public boolean hasPermission(String node) {
        return player.hasPermission(node);
    }

    @Override
    public boolean isPermissionSet(String node) {
        return false;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public boolean isVanished() {
        return vanish;
    }

    @Override
    public void setVanished(boolean vanish) {
        this.vanish = vanish;
    }

    @Override
    public void setLoginPoint() {

    }

    @Override
    public void setLogoutPoint() {

    }

    @Override
    public void setPlayedTime() {

    }

    @Override
    public long getPlayedTime() {
        return 0;
    }

    @Override
    public void setLogoutLocation() {

    }

    @Override
    public void setLoginLocation() {

    }

    @Override
    public void setLastLogin() {

    }

    @Override
    public void setLastQuit() {

    }

    @Override
    public void setAfk(boolean set) {

    }

    @Override
    public boolean isAfk() {
        return false;
    }

    @Override
    public void setGodMode(boolean set) {
        this.godMode = set;
    }

    @Override
    public boolean isGodMode() {
        return godMode;
    }

    @Override
    public Location getHome(String name) throws Exception {
        return null;
    }

    @Override
    public Location getHome(Location loc) throws Exception {
        return null;
    }

    @Override
    public List<String> getHomes() {
        return null;
    }

    @Override
    public void setHome(String name, Location loc) {

    }

    @Override
    public void delHome(String name) throws Exception {

    }

    @Override
    public boolean hasHome() {
        return false;
    }

    @Override
    public void setIgnoreMsg(boolean ignoreMsg) {

    }

    @Override
    public boolean isIgnoreMsg() {
        return false;
    }

    @Override
    public String getAfkMessage() {
        return null;
    }

    @Override
    public void setAfkMessage(String message) {

    }

    @Override
    public long getAfkSince() {
        return 0;
    }
}
