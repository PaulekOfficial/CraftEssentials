package pro.paulek.test;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;
import pro.paulek.CraftEssentials.util.AzureTranslator;
import pro.paulek.CraftEssentials.util.Translator;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TranslationTest {

    Translator translator = new AzureTranslator("https://api.cognitive.microsofttranslator.com", "f30afa09c06a42609c9c82aeb8eba77f");

    @Test
    public void testAzureSingle() {
        Translator.Translation translation = translator.translate(Locale.GERMANY,"Hello, world!");

        Logger.getGlobal().log(Level.INFO, "Translation result: " + translation);

        if(!translation.getMessage().equalsIgnoreCase("Hallo Welt!")) {
            Assert.fail("Incorrect translation, expected Hallo Welt!, get: " + translation.getMessage());
        }

    }

    @Test
    public void testAzureMulti() {
        List<Translator.Translation> translations = translator.translate(Locale.GERMANY,"Hello, world!", "Day");

        Logger.getGlobal().log(Level.INFO, "Translation result: " + translations.toString());

        if(!translations.get(0).getMessage().equalsIgnoreCase("Hallo Welt!")) {
            Assert.fail("Incorrect translation, expected Hallo Welt!, get: " + translations.get(0).getMessage());
        }
        if(!translations.get(1).getMessage().equalsIgnoreCase("Tag")) {
            Assert.fail("Incorrect translation, expected Tag, get: " + translations.get(1).getMessage());
        }

    }

    @Test
    public void ignore3ColorsTest() {
        String text = "&cHere are &bcolors";
        String res = translator.ignoreColorCodesInTranslation(text);
        Logger.getGlobal().log(Level.INFO, res);
        if(!res.equals("<div><div></div><div class=\"notranslate\">&c</div><div>Here are </div><div class=\"notranslate\">&b</div><div>colors</div></div>")) {
            Assert.fail("Incorrect color code format, expected \"<div><div></div><div class=\"notranslate\">&c</div><div>Here are </div><div class=\"notranslate\">&b</div><div>colors</div></div>\", get: " + res);
        }
        Translator.Translation translation = translator.translate(Locale.GERMANY, res);

        Logger.getGlobal().log(Level.INFO, "Translation result: " + translation.getMessage());

        if(!translation.getMessage().equals("&cHier sind &bFarben")) {
            Assert.fail("Incorrect color code format, expected \"&cHier sind &bFarben\", get: " + translation);
        }
    }

}
