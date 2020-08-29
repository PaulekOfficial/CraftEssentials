package pro.paulek.CraftEssentials.util;


import com.google.gson.Gson;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.List;
import java.util.Locale;

public interface Translator {

    List<Translation> translate(Locale locale, String... messages);

    Translation translate(Locale locale, String message);

    class Translation {

        private String message;
        private String language;

        public Translation(String message, String language) {
            this.message = message;
            this.language = language;
        }

        public Translation(AzureTranslator.AzureTranslation azureTranslation) {
            this.message = StringEscapeUtils.unescapeJava(azureTranslation.getText()).replaceAll("<div>", "").replaceAll("</div>", "").replaceAll("<div class=\"notranslate\">", "");
            this.language = azureTranslation.getTo();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    String ignoreColorCodesInTranslation(String message);

}
