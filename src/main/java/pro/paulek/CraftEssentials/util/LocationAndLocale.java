package pro.paulek.CraftEssentials.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.model.CountryResponse;
import org.bukkit.entity.Player;
import pro.paulek.CraftEssentials.user.IUser;
import pro.paulek.CraftEssentials.ICraftEssentials;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.Objects;

public class LocationAndLocale {

    public static String ipAdress(String adress) {
        String sfullip = adress;
        String[] fullip = sfullip.split("/");
        String sIpandPort = fullip[1];
        String[] ipandport = sIpandPort.split(":");
        return ipandport[0];
    }

    public static Locale findLocaleFromCountryTag(String countryCode) {
        for(Locale locale : Locale.getAvailableLocales()) {
            if(locale.getCountry().equalsIgnoreCase(countryCode)) {
                return locale;
            }
        }
        return null;
    }

    public static Locale localeFromTag(String localeTag) {
        Locale locale = null;
        if (!localeTag.isEmpty()) {
            final String[] parts = localeTag.split("[_\\.]");
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
        return locale;
    }

    public static void sendPlayerLocale(Player player, ICraftEssentials craftEssentials) {
        try (DatabaseReader client = new DatabaseReader.Builder(new File(craftEssentials.getDataFolder(), "GeoLite2-City.mmdb")).build()) {
            IUser user = craftEssentials.getUser(player.getUniqueId());
            CountryResponse response = client.country(Objects.requireNonNull(player.getAddress()).getAddress());
            Locale locale = findLocaleFromCountryTag(response.getCountry().getIsoCode());
            if(locale == user.getLocale()) {
                return;
            }
            player.sendMessage(craftEssentials.getI18n().format("countryLocale", locale, user.getLocale().getDisplayLanguage(locale), locale.getDisplayCountry(locale), response.getCountry().getIsoCode()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
