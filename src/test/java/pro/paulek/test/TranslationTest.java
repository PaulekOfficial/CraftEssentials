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

    Translator translator = new AzureTranslator("https://api.cognitive.microsofttranslator.com", "f30afa09c06a42609c9c82aeb8eba77f");

    @Test
    public void testAzureSingle() {
        Translator.Translation translation = translator.translate(Locale.GERMANY,"Hello, world!");

        Logger.getGlobal().log(Level.INFO, "Translation result: " + translation);

        if(!translation.getText().equalsIgnoreCase("Hallo Welt!")) {
            Assert.fail("Incorrect translation, expected Hallo Welt!, get: " + translation.getText());
        }

    }

    @Test
    public void testAzureMulti() {
        List<Translator.Translation> translations = translator.translate(Locale.GERMANY,"Hello, world!", "Day");

        Logger.getGlobal().log(Level.INFO, "Translation result: " + translations.toString());

        if(!translations.get(0).getText().equalsIgnoreCase("Hallo Welt!")) {
            Assert.fail("Incorrect translation, expected Hallo Welt!, get: " + translations.get(0).getText());
        }
        if(!translations.get(1).getText().equalsIgnoreCase("Tag")) {
            Assert.fail("Incorrect translation, expected Tag, get: " + translations.get(1).getText());
        }

    }

}
