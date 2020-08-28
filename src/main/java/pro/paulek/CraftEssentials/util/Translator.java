package pro.paulek.CraftEssentials.util;


import java.util.List;
import java.util.Locale;

public interface Translator {

    List<Translation> translate(Locale locale, String... messages);

    Translation translate(Locale locale, String message);

    class Translation {

        private String translated;
        private String language;

        public Translation(String translated, String language) {
            this.translated = translated;
            this.language = language;
        }

        public String getTranslated() {
            return translated;
        }

        public String getLanguage() {
            return language;
        }
    }

}
