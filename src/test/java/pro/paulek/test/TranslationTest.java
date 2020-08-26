package pro.paulek.test;

import com.google.cloud.translate.*;
import org.junit.Assert;
import org.junit.Test;

public class TranslationTest {

    @Test
    public void translate() {
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "e4df72019af1e8444214972c5765cb438bc73f09");
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        Translation translation = translate.translate("Hello, world!", Translate.TranslateOption.targetLanguage("DE"));

        if(!translation.getTranslatedText().equalsIgnoreCase("Hallo Welt!")) {
            Assert.fail("Incorrect translation");
        }

    }

}
