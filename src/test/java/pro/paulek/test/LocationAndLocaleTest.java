package pro.paulek.test;

import org.apache.http.impl.execchain.ProtocolExec;
import org.junit.Assert;
import org.junit.Test;
import pro.paulek.CraftEssentials.util.LocationAndLocale;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationAndLocaleTest {

    @Test
    public void localeFinder() {
        Locale locale = LocationAndLocale.findLocaleFromCountryTag("PL");
        assert locale != null;
        Logger.getGlobal().log(Level.INFO, locale.toString());
        if(!locale.toString().equalsIgnoreCase("pl_PL")) {
            Assert.fail("Bad locale tag");
        }
    }

}
