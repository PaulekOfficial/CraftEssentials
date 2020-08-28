package pro.paulek.test;

import org.junit.Assert;
import org.junit.Test;
import pro.paulek.CraftEssentials.util.AzureTranslator;
import pro.paulek.CraftEssentials.util.Translator;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TranslationTest {

    @Test
    public void testAzure() {
        Translator translator = new AzureTranslator("https://api.cognitive.microsofttranslator.com", "f30afa09c06a42609c9c82aeb8eba77f");

        List<Translator.Translation> translations = translator.translate(Locale.GERMANY,"Hello, world!", "Day");

        Logger.getGlobal().log(Level.INFO, "Translation result: %s", translations.get(0).getTranslated());

        if(!translations.get(0).getTranslated().equalsIgnoreCase("Hallo Welt!")) {
            Assert.fail("Incorrect translation");
        }

    }

}
